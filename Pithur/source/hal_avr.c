
/*============================ INCLUDE =======================================*/
#include <stdlib.h>

//#include "at86rf230_registermap.h"
#include "at86rf212.h"
#include "compiler.h"
#include "hal_avr.h"
#include "hal.h"
#include "macros.h"


/*============================ MACROS ========================================*/

/*
 * Macros defined for the radio transceiver's access modes.
 *
 * These functions are implemented as macros since they are used very often and
 * we want to remove the function call overhead.
 */
#define HAL_DUMMY_READ         ( 0x00 ) //!< Dummy value for the SPI.

#define HAL_TRX_CMD_RW         ( 0xC0 ) //!< Register Write (short mode).
#define HAL_TRX_CMD_RR         ( 0x80 ) //!< Register Read (short mode).
#define HAL_TRX_CMD_FW         ( 0x60 ) //!< Frame Transmit Mode (long mode).
#define HAL_TRX_CMD_FR         ( 0x20 ) //!< Frame Receive Mode (long mode).
#define HAL_TRX_CMD_SW         ( 0x40 ) //!< SRAM Write.
#define HAL_TRX_CMD_SR         ( 0x00 ) //!< SRAM Read.
#define HAL_TRX_CMD_RADDRM     ( 0x7F ) //!< Register Address Mask.
#define SPI_DUMMY_VALUE                 (0x00)
#define HAL_CALCULATED_CRC_OK   ( 0 ) //!< CRC calculated over the frame including the CRC field should be 0.
/*============================ TYPDEFS =======================================*/
/*============================ VARIABLES =====================================*/
/*! \brief This is a file internal variable that contains the 16 MSB of the 
 *         system time.
 *
 *         The system time (32-bit) is the current time in microseconds. For the
 *         AVR microcontroller implementation this is solved by using a 16-bit 
 *         timer (Timer1) with a clock frequency of 1MHz. The hal_system_time is
 *         incremented when the 16-bit timer overflows, representing the 16 MSB.
 *         The timer value it self (TCNT1) is then the 16 LSB.
 *
 *  \see hal_get_system_time
 */
static uint16_t hal_system_time = 0;
static bool timer4_flag;

/*Flag section.*/
static uint8_t volatile hal_bat_low_flag; //!< BAT_LOW flag.
static uint8_t volatile hal_trx_ur_flag; //!< TRX_UR flag.
static uint8_t volatile hal_trx_end_flag; //!< TRX_END flag.
static uint8_t volatile hal_rx_start_flag; //!< RX_START falg;  
static uint8_t volatile hal_unknown_isr_flag; //!< Error, unknown interrupt event signaled from the radio transceiver.
static uint8_t volatile hal_pll_unlock_flag; //!< PLL_UNLOCK flag.
static uint8_t volatile hal_pll_lock_flag;   //!< PLL_LOCK flag.

/*Callbacks.*/

/*! \brief This function is called when a rx_start interrupt is signaled.
 *
 *         If this function pointer is set to something else than NULL, it will
 *         be called when a RX_START event is signaled. The function takes two
 *         parameters: timestamp in IEEE 802.15.4 symbols (16 us resolution) and 
 *         frame length. The event handler will be called in the interrupt domain, 
 *         so the function must be kept short and not be blocking! Otherwise the 
 *         system performance will be greatly degraded.
 *
 *  \see hal_set_rx_start_event_handler
 */
static hal_rx_start_isr_event_handler_t rx_start_callback;

/*! \brief This function is called when a trx_end interrupt is signaled.
 *
 *         If this function pointer is set to something else than NULL, it will
 *         be called when a TRX_END event is signaled. The function takes two
 *         parameters: timestamp in IEEE 802.15.4 symbols (16 us resolution) and 
 *         frame length. The event handler will be called in the interrupt domain, 
 *         so the function must be kept short and not be blocking! Otherwise the 
 *         interrupt performance will be greatly degraded.
 *
 *  \see hal_set_trx_end_event_handler
 */
static hal_trx_end_isr_event_handler_t trx_end_callback;
/*============================ PROTOTYPES ====================================*/
/*============================ IMPLEMENTATION ================================*/

/*! \brief  This function initializes the Hardware Abstraction Layer.
 *
 *  \ingroup hal_avr_api
 */
retval_t  hal_init( void )
{
    
	/*Reset variables used in file.*/
	hal_system_time = 0;
	hal_reset_flags( );

	/*IO Specific Initialization.*/
	DDR_SLP_TR |= (1 << SLP_TR); //Enable SLP_TR as output.
	DDR_RST    |= (1 << RST);    //Enable RST as output.
	HAL_DDR_DATA_LED |= (1 << DATA_LED);
	HAL_DDR_NET_LED |= (1<< NET_LED);

	/*SPI Specific Initialization.*/    
	//Set SS, CLK and MOSI as output.
	HAL_DDR_SPI  |= (1 << HAL_DD_SS) | (1 << HAL_DD_SCK) | (1 << HAL_DD_MOSI);
	HAL_PORT_SPI |= (1 << HAL_DD_SS) | (1 << HAL_DD_SCK); //Set SS and CLK high
	SPCR         = (1 << SPE) | (1 << MSTR); //Enable SPI module and master operation.
	SPSR         = (1 << SPI2X); //Enable doubled SPI speed in master mode.

	/*TIMER1 Specific Initialization.*/    
	TCCR1B = HAL_TCCR1B_CONFIG;       //Set clock prescaler  
	TIFR1 |= (1 << ICF1);             //Clear Input Capture Flag.
	HAL_ENABLE_OVERFLOW_INTERRUPT( ); //Enable Timer1 overflow interrupt. 
	hal_enable_trx_interrupt( );    //Enable interrupts from the radio transceiver.
	return SUCCESS;
}

void Timer4_disable(void)
{
	power_timer4_disable() ;
}

void Timer4_init(void)//����ת�ټ�����:1S
{
	power_timer4_enable() ;
	TCCR4A=0x00;
	TCCR4B=0X05;

	TCNT4L=0xe0;   //1S  1024
	TCNT4H=0xe3;

	TIMSK4=0x01;
	SREG|=BIT(7);
}

bool get_timer4_flag(void)
{
	return (timer4_flag);
}

void clear_timer4_flag(void)
{
	timer4_flag = false;
}



/*! \brief  This function reset the interrupt flags and interrupt event handlers 
 *          (Callbacks) to their default value.
 *
 *  \ingroup hal_avr_api
 */
void hal_reset_flags( void ){
    
    AVR_ENTER_CRITICAL_REGION( );
    
    //Reset Flags.
    hal_bat_low_flag     = 0;
    hal_trx_ur_flag      = 0;
    hal_trx_end_flag     = 0;
    hal_rx_start_flag    = 0;  
    hal_unknown_isr_flag = 0;
    hal_pll_unlock_flag  = 0;
    hal_pll_lock_flag    = 0;
    
    //Reset Associated Event Handlers.
    rx_start_callback = NULL;
    trx_end_callback  = NULL;
    
    AVR_LEAVE_CRITICAL_REGION( )
}

/*! \brief  This function returns the current value of the BAT_LOW flag.
 *
 *  The BAT_LOW flag is incremented each time a BAT_LOW event is signaled from the
 *  radio transceiver. This way it is possible for the end user to poll the flag 
 *  for new event occurances. 
 *
 *  \ingroup hal_avr_api
 */
uint8_t hal_get_bat_low_flag( void ){
    return hal_bat_low_flag;
}

/*! \brief  This function clears the BAT_LOW flag.
 *
 *  \ingroup hal_avr_api
 */
void hal_clear_bat_low_flag( void ){
    
    AVR_ENTER_CRITICAL_REGION( );
    hal_bat_low_flag = 0;
    AVR_LEAVE_CRITICAL_REGION( );
}

/*! \brief  This function returns the current value of the TRX_UR flag.
 *
 *  The TRX_UR flag is incremented each time a TRX_UR event is signaled from the
 *  radio transceiver. This way it is possible for the end user to poll the flag 
 *  for new event occurances. 
 *
 *  \ingroup hal_avr_api
 */
uint8_t hal_get_trx_ur_flag( void ){
    return hal_trx_ur_flag;
}

/*! \brief  This function clears the TRX_UR flag.
 *
 *  \ingroup hal_avr_api
 */
void hal_clear_trx_ur_flag( void ){
    
    AVR_ENTER_CRITICAL_REGION( );
    hal_trx_ur_flag = 0;
    AVR_LEAVE_CRITICAL_REGION( );
}

/*! \brief  This function returns the current value of the TRX_END flag.
 *
 *  The TRX_END flag is incremented each time a TRX_END event is signaled from the
 *  radio transceiver. This way it is possible for the end user to poll the flag 
 *  for new event occurances. 
 *
 *  \ingroup hal_avr_api
 */
uint8_t hal_get_trx_end_flag( void ){
    return hal_trx_end_flag;
}

/*! \brief  This function clears the TRX_END flag.
 *
 *  \ingroup hal_avr_api
 */
void hal_clear_trx_end_flag( void ){
    
    AVR_ENTER_CRITICAL_REGION( );
    hal_trx_end_flag = 0;
    AVR_LEAVE_CRITICAL_REGION( );
}

/*! \brief  This function is used to set new TRX_END event handler, overriding 
 *          old handler reference.
 *
 *  \ingroup hal_avr_api
 */
hal_trx_end_isr_event_handler_t hal_get_trx_end_event_handler( void ){
    return trx_end_callback;
}

/*! \brief  This function is used to set new TRX_END event handler, overriding 
 *          old handler reference.
 *
 *  \ingroup hal_avr_api
 */
void hal_set_trx_end_event_handler( hal_trx_end_isr_event_handler_t trx_end_callback_handle ){
    
    AVR_ENTER_CRITICAL_REGION( );
    trx_end_callback = trx_end_callback_handle;
    AVR_LEAVE_CRITICAL_REGION( );
}

/*! \brief  Remove event handler reference.
 *
 *  \ingroup hal_avr_api
 */
void hal_clear_trx_end_event_handler( void ){
    
    AVR_ENTER_CRITICAL_REGION( );
    trx_end_callback = NULL;
    AVR_LEAVE_CRITICAL_REGION( );
}

/*! \brief  This function returns the current value of the RX_START flag.
 *
 *  The RX_START flag is incremented each time a RX_START event is signaled from the
 *  radio transceiver. This way it is possible for the end user to poll the flag 
 *  for new event occurances. 
 *
 *  \ingroup hal_avr_api
 */
uint8_t hal_get_rx_start_flag( void ){
    return hal_rx_start_flag;
}

/*! \brief  This function clears the RX_START flag.
 *
 *  \ingroup hal_avr_api
 */
void hal_clear_rx_start_flag( void ){
    
    AVR_ENTER_CRITICAL_REGION( );
    hal_rx_start_flag = 0;
    AVR_LEAVE_CRITICAL_REGION( );
}

/*! \brief  This function returns the active RX_START event handler
 *
 *  \return Current RX_START event handler registered.
 *  \ingroup hal_avr_api
 */
hal_rx_start_isr_event_handler_t hal_get_rx_start_event_handler( void ){
    return rx_start_callback;    
}

/*! \brief  This function is used to set new RX_START event handler, overriding 
 *          old handler reference.
 *
 *  \ingroup hal_avr_api
 */
void hal_set_rx_start_event_handler( hal_rx_start_isr_event_handler_t rx_start_callback_handle ){
    
    AVR_ENTER_CRITICAL_REGION( );
    rx_start_callback = rx_start_callback_handle;
    AVR_LEAVE_CRITICAL_REGION( );
}

/*! \brief  Remove event handler reference.
 *
 *  \ingroup hal_avr_api
 */
void hal_clear_rx_start_event_handler( void ){
    
    AVR_ENTER_CRITICAL_REGION( );
    rx_start_callback = NULL;
    AVR_LEAVE_CRITICAL_REGION( );
}

/*! \brief  This function returns the current value of the UNKNOWN_ISR flag.
 *
 *  The UNKNOWN_ISR flag is incremented each time an UNKNOWN_ISR event is signaled from the
 *  radio transceiver. This way it is possible for the end user to poll the flag 
 *  for new event occurances. 
 *
 *  \ingroup hal_avr_api
 */
uint8_t hal_get_unknown_isr_flag( void ){
    return hal_unknown_isr_flag;
}

/*! \brief  This function clears the UNKNOWN_ISR flag.
 *
 *  \ingroup hal_avr_api
 */
void hal_clear_unknown_isr_flag( void ){
    
    AVR_ENTER_CRITICAL_REGION( );
    hal_unknown_isr_flag = 0;
    AVR_LEAVE_CRITICAL_REGION( );
}

/*! \brief  This function returns the current value of the PLL_UNLOCK flag.
 *
 *  The PLL_UNLOCK flag is incremented each time a PLL_UNLOCK event is signaled from the
 *  radio transceiver. This way it is possible for the end user to poll the flag 
 *  for new event occurances. 
 *
 *  \ingroup hal_avr_api
 */
uint8_t hal_get_pll_unlock_flag( void ){
    return hal_pll_unlock_flag;
}

/*! \brief  This function clears the PLL_UNLOCK flag.
 *
 *  \ingroup hal_avr_api
 */
void hal_clear_pll_unlock_flag( void ){
    
    AVR_ENTER_CRITICAL_REGION( );
    hal_pll_unlock_flag = 0;
    AVR_LEAVE_CRITICAL_REGION( );
}

/*! \brief  This function returns the current value of the PLL_LOCK flag.
 *
 *  The PLL_LOCK flag is incremented each time a PLL_LOCK event is signaled from the
 *  radio transceiver. This way it is possible for the end user to poll the flag 
 *  for new event occurances. 
 *
 *  \ingroup hal_avr_api
 */
uint8_t hal_get_pll_lock_flag( void ){
    return hal_pll_lock_flag;
}

/*! \brief  This function clears the PLL_LOCK flag.
 *
 *  \ingroup hal_avr_api
 */
void hal_clear_pll_lock_flag( void ){
    
    AVR_ENTER_CRITICAL_REGION( );
    hal_pll_lock_flag = 0;
    AVR_LEAVE_CRITICAL_REGION( );
}

/*! \brief  This function reads data from one of the radio transceiver's registers.
 *
 *  \param  address Register address to read from. See datasheet for register 
 *                  map.
 *
 *  \see Look at the at86rf230_registermap.h file for register address definitions.
 *
 *  \returns The actual value of the read register.
 *
 *  \ingroup hal_avr_api
 */
uint8_t hal_register_read( uint8_t address ){
    
    //Add the register read command to the register address.
    address &= HAL_TRX_CMD_RADDRM;
    address |= HAL_TRX_CMD_RR;
    
    uint8_t register_value = 0;
    
    AVR_ENTER_CRITICAL_REGION( );
    
    HAL_SS_LOW( ); //Start the SPI transaction by pulling the Slave Select low.
    
    /*Send Register address and read register content.*/
    SPDR = address;
    while ((SPSR & (1 << SPIF)) == 0) {;}
    register_value = SPDR;
    
    SPDR = register_value;
    while ((SPSR & (1 << SPIF)) == 0) {;}
    register_value = SPDR;

    HAL_SS_HIGH( ); //End the transaction by pulling the Slave Select High.  
    
    AVR_LEAVE_CRITICAL_REGION( );
    
    return register_value;
}

/*! \brief  This function writes a new value to one of the radio transceiver's 
 *          registers.
 *
 *  \see Look at the at86rf230_registermap.h file for register address definitions.
 *
 *  \param  address Address of register to write.
 *  \param  value   Value to write.
 *
 *  \ingroup hal_avr_api
 */
void hal_register_write( uint8_t address, uint8_t value ){
                                       
    //Add the Register Write command to the address.
    address = HAL_TRX_CMD_RW | (HAL_TRX_CMD_RADDRM & address);
    
    AVR_ENTER_CRITICAL_REGION( );    
    
    HAL_SS_LOW( ); //Start the SPI transaction by pulling the Slave Select low.
    
    /*Send Register address and write register content.*/
    SPDR = address;
    while ((SPSR & (1 << SPIF)) == 0) {;}
    uint8_t dummy_read = SPDR;
    
    SPDR = value;
    while ((SPSR & (1 << SPIF)) == 0) {;}
    dummy_read = SPDR;
    
    HAL_SS_HIGH( ); //End the transaction by pulling the Slave Slect High.
    
    AVR_LEAVE_CRITICAL_REGION( );
}

/*! \brief  This function reads the value of a specific subregister.
 *
 *  \see Look at the at86rf230_registermap.h file for register and subregister 
 *       definitions.
 *
 *  \param  address  Main register's address.
 *  \param  mask  Bit mask of the subregister.
 *  \param  position   Bit position of the subregister
 *  \retval Value of the read subregister.
 *
 *  \ingroup hal_avr_api
 */
uint8_t hal_subregister_read( uint8_t address, uint8_t mask, uint8_t position ){
    
    //Read current register value and mask out subregister.
    uint8_t register_value = hal_register_read( address );
    register_value &= mask;
    register_value >>= position; //Align subregister value.
    
    return register_value;
}

/*! \brief  This function writes a new value to one of the radio transceiver's 
 *          subregisters.
 *
 *  \see Look at the at86rf230_registermap.h file for register and subregister 
 *       definitions.
 *
 *  \param  address  Main register's address.
 *  \param  mask  Bit mask of the subregister.
 *  \param  position  Bit position of the subregister
 *  \param  value  Value to write into the subregister.
 *
 *  \ingroup hal_avr_api
 */
void hal_subregister_write( uint8_t address, uint8_t mask, uint8_t position, 
                            uint8_t value ){
    
    //Read current register value and mask area outside the subregister.
    uint8_t register_value = hal_register_read( address );
    register_value &= ~mask;
    
    //Start preparing the new subregister value. shift in place and mask.
    value <<= position;
    value &= mask;
    
    value |= register_value; //Set the new subregister value.

    //Write the modified register value.
    hal_register_write( address, value );
}

/*! \brief  This function will upload a frame from the radio transceiver's frame 
 *          buffer.
 *          
 *          If the frame currently available in the radio transceiver's frame buffer
 *          is out of the defined bounds. Then the frame length, lqi value and crc
 *          be set to zero. This is done to indicate an error.
 *
 *  \param  rx_frame    Pointer to the data structure where the frame is stored.
 *
 *  \ingroup hal_avr_api
 */
__z void hal_frame_read( hal_rx_frame_t *rx_frame ){
    
    AVR_ENTER_CRITICAL_REGION( );
    
    HAL_SS_LOW( );
    
    /*Send frame read command.*/
    SPDR = HAL_TRX_CMD_FR;
    while ((SPSR & (1 << SPIF)) == 0) {;}
    uint8_t frame_length = SPDR;
    
    /*Read frame length.*/    
    SPDR = frame_length;
    while ((SPSR & (1 << SPIF)) == 0) {;}
    frame_length = SPDR;
    
    /*Check for correct frame length.*/
    if ((frame_length >= HAL_MIN_FRAME_LENGTH) && (frame_length <= HAL_MAX_FRAME_LENGTH)) {
        
        uint16_t crc = 0;
        uint8_t *rx_data = (rx_frame->data);
        
        rx_frame->length = frame_length; //Store frame length.
        
        /*Upload frame buffer to data pointer. Calculate CRC.*/
        SPDR = frame_length;
        while ((SPSR & (1 << SPIF)) == 0) {;}
            
        do {
            
            uint8_t const tempData = SPDR;
            SPDR = tempData; // Any data will do, and tempData is readily available. Saving cycles.
            
            *rx_data++ = tempData;      
            
            crc = crc_ccitt_update( crc, tempData );
            
            while ((SPSR & (1 << SPIF)) == 0) {;}
        } while (--frame_length > 0);
        
        /*Read LQI value for this frame.*/
        rx_frame->lqi = SPDR;
        
        HAL_SS_HIGH( );
        
        /*Check calculated crc, and set crc field in hal_rx_frame_t accordingly.*/
        if (crc == HAL_CALCULATED_CRC_OK) {
            rx_frame->crc = true; 
        } else { rx_frame->crc = false; }
    } else {
        
        HAL_SS_HIGH( );
        
        rx_frame->length = 0;
        rx_frame->lqi    = 0;
        rx_frame->crc    = false;    
    }
    
    AVR_LEAVE_CRITICAL_REGION( );
}

/*! \brief  This function will download a frame to the radio transceiver's frame 
 *          buffer.
 *
 *  \param  write_buffer    Pointer to data that is to be written to frame buffer.
 *  \param  length          Length of data. The maximum length is 127 bytes.
 *
 *  \ingroup hal_avr_api
 */
__z void hal_frame_write( uint8_t *write_buffer, uint8_t length ){
    
    length &= HAL_TRX_CMD_RADDRM; //Truncate length to maximum frame length.
    
    AVR_ENTER_CRITICAL_REGION( );
    
    HAL_SS_LOW( ); //Initiate the SPI transaction.
    
    /*SEND FRAME WRITE COMMAND AND FRAME LENGTH.*/
    SPDR = HAL_TRX_CMD_FW;
    while ((SPSR & (1 << SPIF)) == 0) {;}
    uint8_t dummy_read = SPDR;
        
    SPDR = length;
    while ((SPSR & (1 << SPIF)) == 0) {;}
    dummy_read = SPDR;
    
    //Download to the Frame Buffer.
    do {
        
        SPDR = *write_buffer++;
        --length;
        
        while ((SPSR & (1 << SPIF)) == 0) {;}
        
        dummy_read = SPDR;
    } while (length > 0);
    
    HAL_SS_HIGH( ); //Terminate SPI transaction.
    
    AVR_LEAVE_CRITICAL_REGION( );
}

/*! \brief Read SRAM
 *
 * This function reads from the SRAM of the radio transceiver.
 *
 * \param address Address in the TRX's SRAM where the read burst should start
 * \param length Length of the read burst
 * \param data Pointer to buffer where data is stored.
 *
 * \ingroup hal_avr_api
 */
__z void hal_sram_read( uint8_t address, uint8_t length, uint8_t *data ){
    
    AVR_ENTER_CRITICAL_REGION( );
    
    HAL_SS_LOW( ); //Initiate the SPI transaction.
    
    /*Send SRAM read command.*/
    SPDR = HAL_TRX_CMD_SR;
    while ((SPSR & (1 << SPIF)) == 0) {;}
    uint8_t dummy_read = SPDR;
    
    /*Send address where to start reading.*/    
    SPDR = address;
    while ((SPSR & (1 << SPIF)) == 0) {;}
    dummy_read = SPDR;
    
    /*Upload the chosen memory area.*/
    do {
        
        SPDR = HAL_DUMMY_READ;
        while ((SPSR & (1 << SPIF)) == 0) {;}
        *data++ = SPDR;
    } while (--length > 0);

    HAL_SS_HIGH( );
    
    AVR_LEAVE_CRITICAL_REGION( );
}

/*! \brief Write SRAM
 *
 * This function writes into the SRAM of the radio transceiver.
 *
 * \param address Address in the TRX's SRAM where the write burst should start
 * \param length  Length of the write burst
 * \param data    Pointer to an array of bytes that should be written
 *
 * \ingroup hal_avr_api
 */
__z void hal_sram_write( uint8_t address, uint8_t length, uint8_t *data ){
    
    AVR_ENTER_CRITICAL_REGION( );
        
    HAL_SS_LOW( );
    
    /*Send SRAM write command.*/
    SPDR = HAL_TRX_CMD_SW;
    while ((SPSR & (1 << SPIF)) == 0) {;}
    uint8_t dummy_read = SPDR;
    
    /*Send address where to start writing to.*/
    SPDR = address;
    while ((SPSR & (1 << SPIF)) == 0) {;}
    dummy_read = SPDR;
    
    /*Upload the chosen memory area.*/
    do {
        
        SPDR = *data++;
        while ((SPSR & (1 << SPIF)) == 0) {;}
        dummy_read = SPDR;
    } while (--length > 0);
    
    HAL_SS_HIGH( );
    
    AVR_LEAVE_CRITICAL_REGION( );
}


/**
 * @brief Writes and reads data into/from SRAM of the transceiver
 *
 * This function writes data into the SRAM of the transceiver and
 * simultaneously reads the bytes.
 *
 * @param addr Start address in the SRAM for the write operation
 * @param idata Pointer to the data written/read into/from SRAM
 * @param length Number of bytes written/read into/from SRAM
 */
__z void hal_trx_aes_wrrd(uint8_t addr, uint8_t *idata, uint8_t length)
{
    uint8_t *odata;

    delay_us(1);

    AVR_ENTER_CRITICAL_REGION();
    /* Start SPI transaction by pulling SEL low */
    HAL_SS_LOW();

    /* Send the command byte */
    SPDR = HAL_TRX_CMD_SW;
    while ((SPSR & (1 << SPIF)) == 0) {;}

    /* write SRAM start address */
    SPDR = addr;
    while ((SPSR & (1 << SPIF)) == 0) {;}

    /* now transfer data */
    odata = idata;

    /* write data byte 0 - the obtained value in SPDR is meaningless */
    SPDR = *idata++;
    while ((SPSR & (1 << SPIF)) == 0) {;}

    /* process data bytes 1...length-1: write and read */
    while (--length)
    {
        SPDR = *idata++;
        while ((SPSR & (1 << SPIF)) == 0) {;}
        *odata++ = SPDR;
    }

    /* to get the last data byte, write some dummy byte */
    SPDR = SPI_DUMMY_VALUE;
    while ((SPSR & (1 << SPIF)) == 0) {;}
    *odata++ = SPDR;

    /* Stop the SPI transaction by setting SEL high */
    HAL_SS_HIGH();
    AVR_LEAVE_CRITICAL_REGION();
}

/*! \brief This function returns the system time in symbols, as defined in the 
 *         IEEE 802.15.4 standard.
 *
 * \returns The system time with symbol resolution.
 *
 * \ingroup hal_avr_api
 */
uint32_t hal_get_system_time( void ){
    
    uint32_t system_time = hal_system_time; //Read current system time (MSB).
    system_time <<= 16;
    
    /*Disable interrupts. This is done since the TCNT1 value should not 
      be read when the timer is running.*/
    AVR_ENTER_CRITICAL_REGION( );
    
    system_time |= TCNT1; //Add 16 LSB. 
    
    AVR_LEAVE_CRITICAL_REGION( );
    
    //Return the system time in symbols.
    return ((system_time / HAL_US_PER_SYMBOL) & HAL_SYMBOL_MASK);
}
ISR( TIMER1_OVF_vect )
{
 	hal_system_time++;
}
ISR(TIMER2_OVF_vect)
{
}
ISR(TIMER3_OVF_vect)
{
}
ISR(TIMER4_OVF_vect )
{
	TCNT4L=0xE0;
	TCNT4H=0xE3;
	timer4_flag = true;
//	hal_set_slptr_low( );
}

ISR(TIMER5_OVF_vect)
{
}


//This #if compile switch is used to provide a "standard" function body for the 
//doxygen documentation.
ISR( TIMER1_CAPT_vect ){
    
    /*The following code reads the current system time. This is done by first 
      reading the hal_system_time and then adding the 16 LSB directly from the
      TCNT1 register.
     */
    uint32_t isr_timestamp = hal_system_time;
    isr_timestamp <<= 16;
    isr_timestamp |= TCNT1; 
    
    /*Read Interrupt source.*/
    HAL_SS_LOW( );
    
    /*Send Register address and read register content.*/
    SPDR = RG_IRQ_STATUS | HAL_TRX_CMD_RR;
    
    /* This is the second part of the convertion of system time to a 16 us time 
       base. The division is moved here so we can spend less time waiting for SPI
       data.    
     */
    isr_timestamp /= HAL_US_PER_SYMBOL; //Divide so that we get time in 16us resolution.
    isr_timestamp &= HAL_SYMBOL_MASK;  
    
    while ((SPSR & (1 << SPIF)) == 0) {;}
    uint8_t interrupt_source = SPDR; //The interrupt variable is used as a dummy read.
    
    SPDR = interrupt_source;
    while ((SPSR & (1 << SPIF)) == 0) {;}	
    interrupt_source = SPDR; //The interrupt source is read.

    HAL_SS_HIGH( );

    /*Handle the incomming interrupt. Prioritized.*/
    if ((interrupt_source & HAL_RX_START_MASK)) {
        
        hal_rx_start_flag++; //Increment RX_START flag.
        
        if( rx_start_callback != NULL ){
            
            /*Read Frame length and call rx_start callback.*/
            HAL_SS_LOW( );
    
            SPDR = HAL_TRX_CMD_FR;
            while ((SPSR & (1 << SPIF)) == 0) {;}
            uint8_t frame_length = SPDR;
        
            SPDR = frame_length; //Any data will do, so frame_length is used.
            while ((SPSR & (1 << SPIF)) == 0) {;}
            frame_length = SPDR;
            
            HAL_SS_HIGH( );
            
            rx_start_callback( isr_timestamp, frame_length );
        }
    }
    else if (interrupt_source & HAL_TRX_END_MASK) {
        
        hal_trx_end_flag++; //Increment TRX_END flag.
        
        if( trx_end_callback != NULL ){
            trx_end_callback( isr_timestamp );
        }
    } else if (interrupt_source & HAL_TRX_UR_MASK) {
        hal_trx_ur_flag++; //Increment TRX_UR flag.    
    } else if (interrupt_source & HAL_PLL_UNLOCK_MASK) {
        hal_pll_unlock_flag++; //Increment PLL_UNLOCK flag.   
    } else if (interrupt_source & HAL_PLL_LOCK_MASK) {
        hal_pll_lock_flag++; //Increment PLL_LOCK flag.
    } else if (interrupt_source & HAL_BAT_LOW_MASK) {

        //Disable BAT_LOW interrupt to prevent interrupt storm. The interrupt 
        //will continously be signaled when the supply voltage is less than the 
        //user defined voltage threshold.
        uint8_t trx_isr_mask = hal_register_read( RG_IRQ_MASK );
        trx_isr_mask &= ~HAL_BAT_LOW_MASK;
        hal_register_write( RG_IRQ_MASK, trx_isr_mask );
        hal_bat_low_flag++; //Increment BAT_LOW flag.
    } else {
        hal_unknown_isr_flag++;  //Increment UNKNOWN_ISR flag.
    } 
}

/*EOF*/
