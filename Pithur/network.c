/*============================ 文件包含 =======================================*/
#include <stdint.h>
#include <stdbool.h>
#include "config_uart_extended.h" // See this file for all project options.
#include "compiler.h"
//#include "at86rf230.h"
#include "at86rf212.h"
#include "hal.h"                          
#include "tat.h"                
#include "com.h"
#include "hal_avr.h"
#include "sal.h"
#include "network.h"
#include "app.h"
/*============================ 宏定义 ========================================*/
/*============================ 数据类型定义 ======================================*/
/*============================ 变量定义 =====================================*/
static uint8_t tx_frame[ 127 ];//!<单次发送数据的缓存空间，一共128个字节
static uint8_t aes_key[16]  = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};//!<AES加密解密密钥
static uint8_t aes_output[16];//!<AES解密的输出量
static uint8_t rx_pool_items_free; //!< Number of free items (hal_rx_frame_t) in the pool.
static uint8_t rx_pool_items_used; // !< Number of used items.
static bool rx_pool_overflow_flag; //!< Flag that is used to signal a pool overflow.
static bool rx_flag; //!< Flag used to mask between the two possible TRX_END events.
static hal_rx_frame_t_network_form rx_pool[ RX_POOL_SIZE ]; //!< 接收无线数据的缓存池
static hal_rx_frame_t_network_form *rx_pool_start; //!< Pointer to start of pool.
static hal_rx_frame_t_network_form *rx_pool_end; //!< Pointer to end of pool.
static hal_rx_frame_t_network_form *rx_pool_head; //!< Pointer to next hal_rx_frame_t it is possible to write.
static hal_rx_frame_t_network_form *rx_pool_tail;
static hal_rx_frame_t_network_maintain rx_pool_network_maintain[RX_POOL_SIZE/2];
static uint8_t rx_pool_network_maintain_buf_head=0;
static hal_rx_frame_t rx_pool_prep;
static hal_rx_frame_t_network_prep rx_pool_network_prep[RX_POOL_SIZE/2];
static uint8_t rx_pool_network_prep_buf_head=0;
 //!< Pointer to next hal_rx_frame_t that can be read from the pool.
static uint8_t attri=0x00;
static bool sleep_flag=1;
static bool BusyTxSending=0;
static bool BusyRxSending;
static bool NewConfirmFlag=0;
static bool TailConfirmFlag=0;
static bool HeadConfirmFlag=0;
static bool NewNodeFlag=0;
static bool FirstTailFlag=0;
static bool FirstHeadFlag=0;
static bool FirstMedFlag=0;
static bool ImportantFlag=0;
static bool PrepAckFlag=0;
static bool HeadAckFlag=0;
static bool TailAckFlag=0;
static bool MaxNodeFlag;
static bool GotoStartFlag=0;
static bool NetWorkFlag=0;
static bool BreakFlag=0;
static uint8_t Comparerssi=0;
static uint8_t debug_fatal_error[] ="\r\nA fatal error. System must be reset."; //!< Debug Text.
static uint8_t search_start[]="\r\n::Start to search the neighbor nodes...";
static uint8_t search_found[]="\r\n>>Neighbor Node Found.";
static uint8_t search_sAddr[]="\r\n>>Short Address:";
static uint8_t search_rssi[]="\r\n>>RSSI:";
static uint8_t search_finish[]="\r\n::Searching Finished.Neighbor Nodes Found:";
static uint8_t scanNeighborNodes(neighborNode* neighborTable);
static void readRxPool(void);
static bool networkStateTest();
static void SendWild(uint16_t dAddr,uint8_t Wild);
static void SendMessage(uint16_t GDrAddr);
static void download();
static void SendConfirm(uint16_t DetAddr,uint8_t Confirm,uint8_t rssi);
static void receiveData();
static void receiveDataPrep();
static void receiveDatamaintain();
static void sendPrep();
static void setShortAddress(uint16_t newShortAddress);
static uint8_t min(uint8_t a,uint8_t b);
static uint8_t max(uint8_t a,uint8_t b);
static uint8_t abs(uint8_t a);
/*============================ PROTOTYPES ====================================*/
/*! \brief 初始化发送天线
 *
 * The TAT will be set up to run on the chosen operating channel, with CLKM diabled,
 * and then configure the RX_AACK and TX_ARET modes.
 *
 *  \retval true if the TRX was successfully configured.
 *  \retval false if the TRX was not configured properly.
 */
bool trx_init( void )//R212 Modify
{
	if (hal_init() != SUCCESS)
	{
		return false;
	}
	trx_config();	
	if (tat_init() != TAT_SUCCESS)
	{
		return false;
	}
	if (tat_set_operating_channel( RF212_WPAN_CHANNLE0 ) != TAT_SUCCESS) 
	{
		return false;    
	}	
	tat_set_modulation_datarate(BPSK_20);
	tat_set_tx_power_level(WPAN_TXPWR_BP10DBM, R212_PABOOST_ON);	
    /*Set up the extended modes:*/
    //RX_AACK:
    tat_set_short_address( sAddr ); //Short Address.
    tat_set_pan_id( PAN_ID ); //PAN ID.
    tat_set_device_role( false ); // No Coordintor support is necessary.     
    //TX_ARET:
    tat_configure_csma( 234, 0xE2 ); // Default CSMA_SEED_0, MIN_BE = 3, MAX_CSMA_RETRIES = , and CSMA_SEED_1 =     
    //Both Modes:
    tat_use_auto_tx_crc( true ); //Automatic CRC must be enabled.
    hal_set_trx_end_event_handler( trx_end_handler ); // Event handler for TRX_END events.       
	hal_register_read(RG_IRQ_STATUS);    /* clear pending irqs, dummy read */
	return true;
}    
/*! \brief 初始化IO模式
 */
void avr_init( void )
{
	com_init( BR_115200 );
}
/*! \brief 初始化接收池
 */
void rx_pool_init( void )
{
    rx_pool_start = rx_pool;
	rx_pool_end = &rx_pool[ RX_POOL_SIZE - 1 ];
    rx_pool_head = rx_pool_start;
	rx_pool_tail = rx_pool_end;
	rx_pool_items_free = RX_POOL_SIZE;
	rx_pool_items_used = 0;
    rx_pool_overflow_flag = false;
}
/*! \brief TRX_END时的时间处理函数
 *
 *  \param[in] time_stamp Interrupt timestamp in IEEE 802.15.4 symbols.
 */
void trx_end_handler( uint32_t time_stamp )
{
    int i;
	if (rx_flag == true) 
	{
        //Check if these is space left in the rx_pool.
		if (rx_pool_items_free == 0) 
		{
			rx_pool_overflow_flag = true;
		} 
		else 
		{
			//Space left, so upload the received frame.
			hal_frame_read( &rx_pool_prep);
			//Then check the CRC. Will not store frames with invalid CRC.
			if (rx_pool_prep.crc == true&&rx_pool_prep.data[3]==PanIdL&&rx_pool_prep.data[4]==PanIdH)
			{
				if(!(rx_pool_prep.data[5]==0xFF&&rx_pool_prep.data[6]==0xFF)&&
				rx_pool_prep.data[11]==SCMD_TAIL_OCCUPY||rx_pool_prep.data[11]==SCMD_SADDR_BROADCAST_NEW||
				rx_pool_prep.data[11]==SCMD_SADDR_BROADCAST_NEW_CONFIRM||rx_pool_prep.data[11]==SCMD_TAIL_OCCUPY_CONFIRM||
				rx_pool_prep.data[11]==SCMD_HEAD_OCCUPY||rx_pool_prep.data[11]==SCMD_HEAD_OCCUPY_CONFIRM||
				rx_pool_prep.data[11]==SCMD_TAIL_FIRST_COME||rx_pool_prep.data[11]==SCMD_HEAD_FIRST_COME||
				rx_pool_prep.data[11]==SCMD_MED_FIRST_COME||rx_pool_prep.data[11]==SCMD_SADDR_BROADCAST)      
				{    
					//Handle wrapping of rx_pool.
					if (rx_pool_head == rx_pool_end) 
					{
						rx_pool_head = rx_pool_start;
					}
					// end: if (rx_pool_head == rx_pool_end) ...
	                for(i=0;i<=15;i++)
					{
						rx_pool_head->data[i]=rx_pool_prep.data[i];
					}
					++rx_pool_head;
					--rx_pool_items_free;
					++rx_pool_items_used;
				}
				if(rx_pool_prep.data[11]==SCMD_SADDR_BROADCAST&&rx_pool_prep.data[5]==0xFF&&
				rx_pool_prep.data[6]==0xFF)      
				{    
					if (rx_pool_network_maintain_buf_head == RX_POOL_SIZE/2-1) 
					{
						rx_pool_network_maintain_buf_head = 0;
					}
					for(i=0;i<=15;i++)
					{
						rx_pool_network_maintain[rx_pool_network_maintain_buf_head].data[i]=rx_pool_prep.data[i];
					}
						rx_pool_network_maintain_buf_head++;
				}
				if(rx_pool_prep.data[11]==SCMD_PREP||rx_pool_prep.data[11]==SCMD_PREP_RECEIVED
				||!(rx_pool_prep.data[9]==0xFF&&rx_pool_prep.data==0xFF))      
				{    
					if (rx_pool_network_prep_buf_head == RX_POOL_SIZE/2-1) 
					{
						rx_pool_network_prep_buf_head = 0;
					}
					for(i=0;i<=18;i++)
					{
						rx_pool_network_prep[rx_pool_network_prep_buf_head].data[i]=rx_pool_prep.data[i];
					}
						rx_pool_network_prep_buf_head++;
				}
			}// end: if (rx_pool_head->crc == true) ...
		} // end: if (rx_pool_items_free == 0) ...
	} // end:  if (rx_flag == true) ...
}
void SendWild(uint16_t dAddr,uint8_t Wild)
{
	int i;
	tx_frame[0] = 0x61; //IEEE 802.15.4的标准包头帧
	tx_frame[1] = 0x88; //IEEE 802.15.4的标准包头帧
	tx_frame[2]= 0; //封包序列号
    tx_frame[3] = PAN_ID & 0xFF; //网络地址低8位
    tx_frame[4] = (PAN_ID >> 8 ) & 0xFF; //网络地址高8位
	tx_frame[5] = dAddr& 0xFF; //目标短地址低8位.
	tx_frame[6] = (dAddr>>8)& 0xFF& 0xFF; //目标短地址高8位
	tx_frame[7] = sAddr& 0xFF; //发送源短地址低8位
    tx_frame[8] = (sAddr>>8)& 0xFF; //发送源短地址高8位
    tx_frame[9] =0xFF;		   
    tx_frame[10]=0xFF;
	tx_frame[11]=Wild;
	tx_frame[12]=attri;
	tx_frame[13]=Comparerssi;
	delay_ms(50);
	if (set_trx_state( PLL_ON ) == TAT_SUCCESS) 
    {
      rx_flag = false;
	  if (tat_send_data( 16, tx_frame) == TAT_SUCCESS) 
	  {	   
		 comPrintString("\r\n<<Sendwild::::::::");
		 for(i=0;i<=13;i++)
		 {
			com_send_hex(tx_frame[i]);
			comPrintString(" ");
	 	 }
			delay_ms(20);
		 } 
		 else 
			 comPrintString("\r\n!<Sent Failed");
    }
    else 
	   comPrintString("\r\n!!State Change Failed");
    if (set_trx_state( RX_AACK_ON ) != TAT_SUCCESS)  //转移状态机至RX_AACK_ON状态,以侦听传入包
	   comPrintString("\r\n!!State Change Failed");
    rx_flag = true; // Set the flag back again. Only used to protect			
}
void SendMessage(uint16_t GDrAddr)
{
	int i;
    tx_frame[0] = 0x61; //IEEE 802.15.4的标准包头帧
    tx_frame[1] = 0x88; //IEEE 802.15.4的标准包头帧
    tx_frame[2]= 0; //封包序列号
    tx_frame[3] = PAN_ID & 0xFF; //网络地址低8位
    tx_frame[4] = (PAN_ID >> 8 ) & 0xFF; //网络地址高8位
    tx_frame[5] = GDrAddr; //目标短地址低8位.
    tx_frame[6] = (GDrAddr)>>8 & 0xFF; //目标短地址高8位
    tx_frame[7] = sAddr& 0xFF; //发送源短地址低8位
    tx_frame[8] = (sAddr>>8)& 0xFF; //发送源短地址高8位
    tx_frame[9] =dataSendBuffer[dataSendBufferHead].dAddr[0];		   
    tx_frame[10]=dataSendBuffer[dataSendBufferHead].dAddr[1];
    tx_frame[11]=dataSendBuffer[dataSendBufferHead].data[0];
    tx_frame[12]=dataSendBuffer[dataSendBufferHead].data[1];
    tx_frame[13]=dataSendBuffer[dataSendBufferHead].data[2];
    tx_frame[14]=dataSendBuffer[dataSendBufferHead].data[3];
    tx_frame[15]=dataSendBuffer[dataSendBufferHead].data[4];
    comPrintString("Sending Mess:::::::");
	comPrintString("Dest Saddr apart:::::::");
	com_send_hex(dataSendBuffer[dataSendBufferHead].dAddr[1]);  
	com_send_hex(dataSendBuffer[dataSendBufferHead].dAddr[0]); 
	comPrintString("To Dest Data is:::::::");
	com_send_hex(dataSendBuffer[dataSendBufferHead].data[0]); 
	com_send_hex(dataSendBuffer[dataSendBufferHead].data[1]); 
	com_send_hex(dataSendBuffer[dataSendBufferHead].data[2]); 
	com_send_hex(dataSendBuffer[dataSendBufferHead].data[3]); 
	com_send_hex(dataSendBuffer[dataSendBufferHead].data[4]);     
	if (set_trx_state( TX_ARET_ON ) == TAT_SUCCESS) 
    {
      rx_flag = false;
	  if (tat_send_data_with_retry( 18, tx_frame,15) == TAT_SUCCESS) 
	  {	   
		 comPrintString("\r\n<<");
		 for(i=0;i<=15;i++)
		 {
		 	 com_send_hex(tx_frame[i]);
			 comPrintString(" ");
		 }
			delay_ms(20);
		 } 
	 	 else 
			 comPrintString("\r\n!<Sent Failed");
    }
    else 
	   comPrintString("\r\n!!State Change Failed");
    if (set_trx_state( RX_AACK_ON ) != TAT_SUCCESS)  //转移状态机至RX_AACK_ON状态,以侦听传入包
	   comPrintString("\r\n!!State Change Failed");
    rx_flag = true; // Set the flag back again. Only used to protec t
}
void download()
{
	comPrintString("\r\n>>yangjieshahuhu!");
}
void addSendData(uint16_t dAddr,uint8_t data[])
{
	dataSendBuffer[dataSendBufferHead].dAddr[0]=dAddr&0xFF;
	dataSendBuffer[dataSendBufferHead].dAddr[1]=(dAddr>>8)&0xFF;
	dataSendBuffer[dataSendBufferHead].data[0]=data[0];
	dataSendBuffer[dataSendBufferHead].data[1]=data[1];
	dataSendBuffer[dataSendBufferHead].data[2]=data[2];
	dataSendBuffer[dataSendBufferHead].data[3]=data[3];
	dataSendBuffer[dataSendBufferHead].data[4]=data[4];
	dataSendBufferHead++;
}
void SendConfirm(uint16_t DetAddr,uint8_t Confirm,uint8_t rssi)
{
	int i;
	tx_frame[0] = 0x61; //IEEE 802.15.4的标准包头帧
    tx_frame[1] = 0x88; //IEEE 802.15.4的标准包头帧
    tx_frame[2]= 0; //封包序列号
    tx_frame[3] = PAN_ID & 0xFF; //网络地址低8位
    tx_frame[4] = (PAN_ID >> 8 ) & 0xFF; //网络地址高8位
    tx_frame[5] = DetAddr; //目标短地址低8位.
    tx_frame[6] = (DetAddr)>>8 & 0xFF; //目标短地址高8位
    tx_frame[7] = sAddr& 0xFF; //发送源短地址低8位
    tx_frame[8] = (sAddr>>8)& 0xFF; //发送源短地址高8位
    tx_frame[9]=0xFF;
	tx_frame[10]=0xFF;
	tx_frame[11]=Confirm;
    tx_frame[12]=rssi;
	if (set_trx_state( PLL_ON ) == TAT_SUCCESS) 
    {
     	rx_flag = false;
	  if (tat_send_data( 15, tx_frame) == TAT_SUCCESS) 
	  {	   
	     comPrintString("\r\n<<");
		 for(i=0;i<=12;i++)
		 {
				com_send_hex(tx_frame[i]);
			comPrintString(" ");
	 	 }
			delay_ms(20);
		 } 
		 else 
			comPrintString("\r\n!<Sent Failed");
    }
    else 
	   comPrintString("\r\n!!State Change Failed");
    if (set_trx_state( RX_AACK_ON ) != TAT_SUCCESS)  //转移状态机至RX_AACK_ON状态,以侦听传入包
	   comPrintString("\r\n!!State Change Failed");
    rx_flag = true; // Set the flag back again. Only used to protec t
}
void receiveData()
{
	static int j=0;
	int i,k;
	uint8_t rssi=0;
	uint8_t SNrssi=0;
	uint8_t STrssi=0;
	uint8_t Trssi=0;
	uint8_t Nrssi=0;
	uint8_t Hrssi=0;
    uint16_t NewrAddr=0xFFFC;
	uint16_t TailrAddr=0xFFFC;
    uint16_t HeadrAddr=0xFFFC;
	PrepReceiveNew n_buf[RX_POOL_SIZE];     
    PrepReceiveNew t_buf[RX_POOL_SIZE];
	PrepReceiveNew h_buf[RX_POOL_SIZE];
    short int n_buf_head=0;
	short int t_buf_head=0;    
	short int h_buf_head=0;
	comPrintString("\r\n!!into times:");
	com_send_hex(j);
	comPrintString("\r\n!!Please pay attention~~~~~~~~~~~~~~~~~~~");
	j++;
    while (rx_pool_items_used != 0) //查看接收池中是否有未处理的包
	{   
	    comPrintString("\r\n!!rx_pool_items_used::::::::::");
		com_send_hex(rx_pool_items_used);
	    readRxPool();				
		//读取缓存区，接收到的数据会被储存在rx_pool_tail->data[]中
		comPrintString("\r\n>>Data Received::");
		for(i=0;i<=15;i++)
		{
			com_send_hex(rx_pool_tail->data[i]);
			comPrintString(" ");
		}
	    if(rx_pool_tail->data[3]== PanIdL && rx_pool_tail->data[4]==PanIdH&&rx_pool_tail->data[9]== 0xFF && rx_pool_tail->data[10]==0xFF && rx_pool_tail->data[11]==SCMD_SADDR_BROADCAST_NEW_CONFIRM
		&&rx_pool_tail->data[12]==Comparerssi)//收到SCMD_SADDR_BROADCAST_NEW_CONFIRM反馈
		{
		    if(NewNodeFlag==1)
			{	
				comPrintString("\r\n>>Short Address APPLING has been Confirmed...");
				for(i=0;i<Broad_Cover_Times;i++)//连续发送Broad_Cover_Times个TPU的短地址占用广播
				{
					delay_ms(TPU);
					SendWild(sAddr,SCMD_SADDR_BROADCAST);
				}
			    cli();
				rx_pool_init( );
				sei();
	            NewConfirmFlag=1;
            }
		}
		else if(rx_pool_tail->data[3]== PanIdL && rx_pool_tail->data[4]==PanIdH&&rx_pool_tail->data[9]== 0xFF && rx_pool_tail->data[10]==0xFF && rx_pool_tail->data[11]==SCMD_TAIL_FIRST_COME
		&&rx_pool_tail->data[7]==sAddr&0xFF && rx_pool_tail->data[8]==(sAddr>>8)&0xFF)//收到SCMD_TAIL_FIRST_COME
		{
		 	if(NewNodeFlag==1)
			{
				comPrintString("\r\n>>SCMD_TAIL_FIRST_COME has been received...");
				comPrintString("\r\n>>I have to goto start.....");
			    FirstTailFlag=1;
            }
		}
		else if(rx_pool_tail->data[3]== PanIdL && rx_pool_tail->data[4]==PanIdH&&rx_pool_tail->data[9]== 0xFF && rx_pool_tail->data[10]==0xFF && rx_pool_tail->data[11]==SCMD_HEAD_FIRST_COME
		&&rx_pool_tail->data[7]==sAddr&0xFF && rx_pool_tail->data[8]==(sAddr>>8)&0xFF)//收到SCMD_HEAD_FIRST_COME
		{
		 	if(NewNodeFlag==1)
			{
				comPrintString("\r\n>>SCMD_HEAD_FIRST_COME has been received...");
				comPrintString("\r\n>>I have to goto start.....");
			    FirstHeadFlag=1;
            }
		}
		else if(rx_pool_tail->data[3]== PanIdL && rx_pool_tail->data[4]==PanIdH&&rx_pool_tail->data[9]== 0xFF && rx_pool_tail->data[10]==0xFF && rx_pool_tail->data[11]==SCMD_MED_FIRST_COME
		&&rx_pool_tail->data[7]==sAddr&0xFF && rx_pool_tail->data[8]==(sAddr>>8)&0xFF)//收到SCMD_MED_FIRST_COME
		{
		 	if(NewNodeFlag==1)
			{
				comPrintString("\r\n>>SCMD_MED_FIRST_COME has been received...");
				comPrintString("\r\n>>I have to goto start.....");
			    FirstMedFlag=1;
            }
		}
		else if(rx_pool_tail->data[3]== PanIdL && rx_pool_tail->data[4]==PanIdH&&rx_pool_tail->data[9]== 0xFF && rx_pool_tail->data[10]==0xFF && rx_pool_tail->data[11]==SCMD_TAIL_OCCUPY_CONFIRM
		&&rx_pool_tail->data[12]==Comparerssi)//收到SCMD_TAIL_OCCUPY_CONFIRM反馈
		{
		 	if(NewNodeFlag==1)
			{	
				comPrintString("\r\n>>Tail  APPLING has been Confirmed...");
				TailConfirmFlag=1;
				attri=1;
            }
		}
		else if(rx_pool_tail->data[3]== PanIdL && rx_pool_tail->data[4]==PanIdH&&rx_pool_tail->data[9]== 0xFF && rx_pool_tail->data[10]==0xFF && rx_pool_tail->data[11]==SCMD_HEAD_OCCUPY_CONFIRM
		&&rx_pool_tail->data[12]==Comparerssi)//收到SCMD_HEAD_OCCUPY_CONFIRM反馈
		{
		 	if(NewNodeFlag==1)
			{	
				comPrintString("\r\n>>Head  APPLING has been Confirmed...");
				HeadConfirmFlag=1;
            }
		}
		else if(rx_pool_tail->data[3]== PanIdL && rx_pool_tail->data[4]==PanIdH&&rx_pool_tail->data[9]== 0xFF 
		&& rx_pool_tail->data[10]==0xFF &&rx_pool_tail->data[11]==SCMD_HEAD_OCCUPY&&sAddr==0x00)//收到头节点占用广播
		{  
		 	if(NewNodeFlag==0)
			{  	   
			   comPrintString("\r\n>>HEAD OCCUPY Prep Callback Received...");
			   h_buf[h_buf_head].rAddr[0]=rx_pool_tail->data[7];
			   h_buf[h_buf_head].rAddr[1]=rx_pool_tail->data[8];
	           h_buf[h_buf_head].rssi=rx_pool_tail->data[13];
			   h_buf_head++;
           }
	    } 
		else if(rx_pool_tail->data[3]== PanIdL && rx_pool_tail->data[4]==PanIdH&&rx_pool_tail->data[9]== 0xFF 
		&& rx_pool_tail->data[10]==0xFF && rx_pool_tail->data[11]==SCMD_SADDR_BROADCAST_NEW)//收到SCMD_SADDR_BROADCAST_NEW ,立刻反馈CONFIRM
		{
			if(NewNodeFlag==0)	   
			{   
			   comPrintString("\r\n>>Prep new appling Received...");
			   n_buf[n_buf_head].rAddr[0]=rx_pool_tail->data[7];
			   n_buf[n_buf_head].rAddr[1]=rx_pool_tail->data[8];
	           n_buf[n_buf_head].rssi=rx_pool_tail->data[13];
			   n_buf_head++;
            }
		}
		else if(rx_pool_tail->data[3]== PanIdL && rx_pool_tail->data[4]==PanIdH&&attri==0x01 && 
				rx_pool_tail->data[9]== 0xFF && rx_pool_tail->data[10]==0xFF && 
				rx_pool_tail->data[11]==SCMD_TAIL_OCCUPY)//收到尾节点占用广播
		{
			if(NewNodeFlag==0) 	 
			{	 
				t_buf[t_buf_head].rAddr[0]=rx_pool_tail->data[7];
			    t_buf[t_buf_head].rAddr[1]=rx_pool_tail->data[8];
				t_buf[t_buf_head].rssi=rx_pool_tail->data[13];
			    t_buf_head++;
				comPrintString("\r\n>>Route tail Broadcast Recognized...");
           }
		}
		else if(rx_pool_tail->data[3]== PanIdL && rx_pool_tail->data[4]==PanIdH&&rx_pool_tail->data[9]== 0xFF &&
		 rx_pool_tail->data[10]==0xFF &&rx_pool_tail->data[11]==SCMD_SADDR_BROADCAST )//收到短地址点占用广播
		{
			if(NewNodeFlag==0)
			{	
				if(rx_pool_tail->data[7]==sAddr&0xFF && rx_pool_tail->data[8]==(sAddr>>8)&0xFF )	
				{	
					comPrintString("\r\n>>Short Address Broadcast Recognized...");
					setShortAddress(sAddr+1);
					for(i=0;i<20;i++)//连续发送20个TPU的短地址占用广播
					{
						delay_ms(TPU);
						SendWild(sAddr,SCMD_SADDR_BROADCAST);
					}
					cli();
					rx_pool_init( );
					sei();
                }
            }
		}		
		if(rx_pool_items_used == 0 &&FirstTailFlag==1&&TailConfirmFlag==1)
		{
			FirstTailFlag=0;
		}
		if(rx_pool_items_used == 0 &&FirstHeadFlag==1&&HeadConfirmFlag==1)
		{
			FirstHeadFlag=0;
		}
		if(rx_pool_items_used == 0 &&FirstMedFlag==1&&NewConfirmFlag==1)
		{
			FirstMedFlag=0;
		}
	    if(rx_pool_items_used == 0 && n_buf_head>0)//当接收池空了时，处理收到的SCMD_SADDR_BROADCAST_NEW
		{
			if(NewNodeFlag==0) 	
			{	
				while(n_buf_head>0)
				{
					n_buf_head--;
					comPrintString("\r\n<<Sending New Prep confirm11111...");
					if(n_buf[n_buf_head].rssi>Nrssi)
					{	
						comPrintString("\r\n<<Sending New Prep confirm22222...");
						NewrAddr=n_buf[n_buf_head].rAddr[0]+n_buf[n_buf_head].rAddr[1]*256;
						Nrssi=n_buf[n_buf_head].rssi;
	                }
			    }
	            if(NewrAddr==(sAddr+1))
				{
					comPrintString("\r\n<<Sending New Prep confirm33333...");
				    SendConfirm(NewrAddr,SCMD_SADDR_BROADCAST_NEW_CONFIRM,Nrssi);
					delay_ms(Broad_Cover_Times*TPU);
				}
			}			
		}
		if(rx_pool_items_used == 0 && t_buf_head>0)//当池子空了处理收到的SCMD_TAIL_OCCUPY
		{
			if(NewNodeFlag==0)	
			{	
				comPrintString("\r\n!!Please pay attention~~~~~~~~~~~~~~~~~~~~~");
				comPrintString("\r\n!!into used==0!!!!!!!!!!!!!!!!!!!!");
				while(t_buf_head>0)
				{
					t_buf_head--;
					comPrintString("\r\n<<Sending Tail Prep confirm00001...");
					if(t_buf[t_buf_head].rssi>Trssi)
					{	
						comPrintString("\r\n<<Sending Tail Prep confirm22222...");
						TailrAddr=t_buf[t_buf_head].rAddr[0]+t_buf[t_buf_head].rAddr[1]*256;
						Trssi=t_buf[t_buf_head].rssi;
	                }
				 }
	             comPrintString("\r\n<<Sending Tail Prep confirm11111...");
				 if(TailrAddr==(sAddr+1))        
				 {
					comPrintString("\r\n<<Sending Tail Prep confirm33333...");
					SendConfirm(TailrAddr,SCMD_TAIL_OCCUPY_CONFIRM,Trssi);
					attri=0x00;
				 }
			}	 		
		}
		if(rx_pool_items_used == 0 && h_buf_head>0)//当池子空了处理收到的SCMD_TAIL_OCCUPY
		{   
			if(NewNodeFlag==0) 	
			{	
			    while(h_buf_head>0)
				{
					h_buf_head--;
					comPrintString("\r\n<<Sending Head prep confirm00001...");
					if(h_buf[h_buf_head].rssi>Hrssi)
					{	
						HeadrAddr=h_buf[h_buf_head].rAddr[0]+h_buf[h_buf_head].rAddr[1]*256;
						Hrssi=h_buf[h_buf_head].rssi;
	                }
				}
	            comPrintString("\r\n<<Sending Head Prep confirm11111...");
				if(sAddr==0x0000)
				{
					comPrintString("\r\n<<Sending Head Prep confirm33333...");
					SendConfirm(HeadrAddr,SCMD_HEAD_OCCUPY_CONFIRM,Hrssi);
				}
			}				
		}
	}  

	if (rx_pool_overflow_flag == true) //检查接收池是否溢出
	{
		cli();
		rx_pool_init( );
		comPrintString("\r\n!!RX Pool overflow.");
		sei();
	}
}
void receiveDataPrep()
{
	static int j=0;
	int i,k;
	uint16_t rAddr=0xFFFC;
    PrepReceive p_buf[RX_POOL_SIZE/2];
    short int p_buf_head=0;
    comPrintString("\r\n!!into Prep times:");
	com_send_hex(j);
	comPrintString("\r\n!!Please pay attention~~~~~~~~~~~~~~~~~~~");
	j++;
    while (rx_pool_network_prep_buf_head != 0) //查看接收池中是否有未处理的包
	{   
	    comPrintString("\r\n!!rx_pool_network_prep_buf_head used::::::::::");
		com_send_hex(rx_pool_network_prep_buf_head);
	    rx_pool_network_prep_buf_head--;				
		//读取缓存区，接收到的数据会被储存在rx_pool_tail->data[]中
		comPrintString("\r\n>>Data Received::");
		for(i=0;i<=15;i++)
		{
			com_send_hex(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[i]);
			comPrintString(" ");
		}
	    if(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[3]== PanIdL && rx_pool_network_prep[rx_pool_network_prep_buf_head].data[4]==PanIdH&&
		rx_pool_network_prep[rx_pool_network_prep_buf_head].data[9]== 0xFF &&rx_pool_network_prep[rx_pool_network_prep_buf_head].data[10]==0xFF && 
		rx_pool_network_prep[rx_pool_network_prep_buf_head].data[11]==SCMD_PREP_RECEIVED)//收到Prep反馈，发送正常数据
		{
			if(NewNodeFlag==0)	   
			{   
			   comPrintString("\r\n>>Prep Callback Received...");
			   p_buf[p_buf_head].rAddr[0]=rx_pool_network_prep[rx_pool_network_prep_buf_head].data[7];
			   p_buf[p_buf_head].rAddr[1]=rx_pool_network_prep[rx_pool_network_prep_buf_head].data[8];
			   p_buf_head++;
            }
	    } 
	    else if(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[3]== PanIdL && rx_pool_network_prep[rx_pool_network_prep_buf_head].data[4]==PanIdH&&
		rx_pool_network_prep[rx_pool_network_prep_buf_head].data[9]== 0xFF &&rx_pool_network_prep[rx_pool_network_prep_buf_head].data[10]==0xFF && 
		rx_pool_network_prep[rx_pool_network_prep_buf_head].data[11]==SCMD_PREP)//收到PREP,立刻反馈PrepReceived
		{
		   if(((rx_pool_network_prep[rx_pool_network_prep_buf_head].data[7]+rx_pool_network_prep[rx_pool_network_prep_buf_head].data[8]*256)<=sAddr)&&
		       (sAddr<=(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[12]+rx_pool_network_prep[rx_pool_network_prep_buf_head].data[13]*256))||
		      ((rx_pool_network_prep[rx_pool_network_prep_buf_head].data[7]+rx_pool_network_prep[rx_pool_network_prep_buf_head].data[8]*256)>=sAddr)&&
		       (sAddr>=(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[12]+rx_pool_network_prep[rx_pool_network_prep_buf_head].data[13]*256)))
		   {
				if(NewNodeFlag==0)   		
				{	
				    comPrintString("\r\n>>Prep Received...");
				    SendWild(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[7]+rx_pool_network_prep[rx_pool_network_prep_buf_head].data[8]*256,SCMD_PREP_RECEIVED);
				    sleep_flag=0;
			    }				
		   }
		}	
		else if(!(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[9]==0xFF && rx_pool_network_prep[rx_pool_network_prep_buf_head].data[10]==0xFF))//收到一般信息
        {
			if(NewNodeFlag==0)
			{	
				comPrintString("\r\n>>Data Package Received");
				if(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[3]== PanIdL && rx_pool_network_prep[rx_pool_network_prep_buf_head].data[4]==PanIdH&&
				rx_pool_network_prep[rx_pool_network_prep_buf_head].data[9]==tat_get_short_address()&& rx_pool_network_prep[rx_pool_network_prep_buf_head].data[10]==(tat_get_short_address()>>8))
				{
				    comPrintString("within normal receive~~~");
					download();
				}		
				if(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[3]== PanIdL && rx_pool_network_prep[rx_pool_network_prep_buf_head].data[4]==PanIdH&&
				(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[9]!=tat_get_short_address()|| rx_pool_network_prep[rx_pool_network_prep_buf_head].data[10]!=(tat_get_short_address()>>8)))
				{
					if(BusyTxSending==0)
					{
						comPrintString("\r\n<<Transfroming data now is my sending data...");
						addSendData(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[9]+rx_pool_network_prep[rx_pool_network_prep_buf_head].data[10]*256,&(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[11]));
	                }
	                 if(BusyTxSending==1)
					 {
						 comPrintString("\r\n<<I have Received but time is not good...");
		                 comPrintString("\r\n<<Dest shotAddr apart is:::::...");
						 com_send_hex(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[10]);
						 com_send_hex(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[9]);
						 comPrintString("\r\n<<TO Dest  data is:::::...");
						 com_send_hex(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[11]);
						 com_send_hex(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[12]);
						 com_send_hex(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[13]);
						 com_send_hex(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[14]);
	       			     com_send_hex(rx_pool_network_prep[rx_pool_network_prep_buf_head].data[15]);
	                  }
				}
            }  
		}
		if(rx_pool_network_prep_buf_head == 0 && p_buf_head>0)//当接收池空了时，处理收到的Prep_RECEIVED
		{
			if(NewNodeFlag==0)   
			{   
			   if((dataSendBuffer[dataSendBufferHead].dAddr[0]+dataSendBuffer[dataSendBufferHead].dAddr[1]*256)<sAddr)//将要发送的数据上行
			   {
					comPrintString("\r\n<<Sending Data Upward...");
	                rAddr=sAddr;
					while(p_buf_head>0)
					{
						p_buf_head--;
						if((p_buf[p_buf_head].rAddr[0]+p_buf[p_buf_head].rAddr[1]*256)<rAddr)
							rAddr=(p_buf[p_buf_head].rAddr[0]+p_buf[p_buf_head].rAddr[1]*256);
					}
			   }
			   else
			   {
					comPrintString("\r\n<<Sending Data Downward...");
					rAddr=sAddr;
					while(p_buf_head>0)
					{
						p_buf_head--;
						if((p_buf[p_buf_head].rAddr[0]+p_buf[p_buf_head].rAddr[1]*256)>rAddr)
							rAddr=(p_buf[p_buf_head].rAddr[0]+p_buf[p_buf_head].rAddr[1]*256);
					}
			   }
			   comPrintString("\r\n<<To node :::::::.");
			   com_send_hex(rAddr);
			   SendMessage(rAddr);
			   BusyTxSending=0;
			   PrepAckFlag=0;
            }
		}	   
	}  
}
void receiveDatamaintain()
{
	while(rx_pool_network_maintain_buf_head!=0)
    {
		rx_pool_network_maintain_buf_head--;
		if(rx_pool_network_maintain[rx_pool_network_maintain_buf_head].data[3]== PanIdL && rx_pool_network_maintain[rx_pool_network_maintain_buf_head].data[4]==PanIdH
		&&rx_pool_network_maintain[rx_pool_network_maintain_buf_head].data[9]== 0xFF &&rx_pool_network_maintain[rx_pool_network_maintain_buf_head].data[10]==0xFF &&
		rx_pool_network_maintain[rx_pool_network_maintain_buf_head].data[11]==SCMD_SADDR_BROADCAST )//收到短地址广播	
		{	
			if((rx_pool_network_maintain[rx_pool_network_maintain_buf_head].data[7]+rx_pool_network_maintain[rx_pool_network_maintain_buf_head].data[8]*256)<sAddr
			&&rx_pool_network_maintain[rx_pool_network_maintain_buf_head].data[12]==1)
			{ 
				GotoStartFlag=1; 
			}
			if(((rx_pool_network_maintain[rx_pool_network_maintain_buf_head].data[7]+rx_pool_network_maintain[rx_pool_network_maintain_buf_head].data[8]*256)==sAddr+1))
			{
				MaxNodeFlag=0;
				attri==0;
				comPrintString("\r\n>>I am not the biggest~~~");
			}
			if(((rx_pool_network_maintain[rx_pool_network_maintain_buf_head].data[7]+rx_pool_network_maintain[rx_pool_network_maintain_buf_head].data[8]*256)==sAddr-1)&&sAddr!=0x0000)
			{
				NetWorkFlag=1;
			}
		}		
	}		
}
void sendPrep()
{
	int i,j;
	int k=0;
	BusyTxSending=1;
	if(dataSendBufferHead>0)
	{
		dataSendBufferHead--;
		if((dataSendBuffer[dataSendBufferHead].dAddr[0]+dataSendBuffer[dataSendBufferHead].dAddr[1]*256)==sAddr)
		{
			delay_ms(100);
			comPrintString("amazing within Prep~~~~");
			download();
		}
		else
		{
			PrepAckFlag=1;
			tx_frame[0] = 0x61; //IEEE 802.15.4的标准包头帧
			tx_frame[1] = 0x88; //IEEE 802.15.4的标准包头帧
			tx_frame[2]= 0; //封包序列号
			tx_frame[3] = PAN_ID & 0xFF; //网络地址低8位
			tx_frame[4] = (PAN_ID >> 8 ) & 0xFF; //网络地址高8位
			tx_frame[5] = 0xFF; //目标短地址低8位.
			tx_frame[6] = 0xFF; //目标短地址高8位
			tx_frame[7] = sAddr & 0xFF; //发送源短地址低8位
			tx_frame[8] = (sAddr>>8)& 0xFF;//发送源短地址高8位
			tx_frame[9] =0xFF;		   
			tx_frame[10]=0xFF;
			tx_frame[11]=SCMD_PREP;
			tx_frame[12]=dataSendBuffer[dataSendBufferHead].dAddr[0];
			tx_frame[13]=dataSendBuffer[dataSendBufferHead].dAddr[1];
			for(i=0;i<3;i++)
			{
				comPrintString("\r\n<<Sending Prep...");
				if (set_trx_state( PLL_ON ) == TAT_SUCCESS) 
				{
					rx_flag = false;
					tat_send_data( 16, tx_frame);
					comPrintString("\r\n<<");
					for(j=0;j<=13;j++)
					{
						com_send_hex(tx_frame[j]);
						comPrintString(" ");
					}
					delay_ms(20);//状态转换时间
				}
				else 
					comPrintString("\r\n!!State Change Failed");
				if (set_trx_state( RX_AACK_ON ) != TAT_SUCCESS)  //转移状态机至RX_AACK_ON状态,以侦听传入包
					comPrintString("\r\n!!State Change Failed");
				rx_flag = true;
				delay_ms(2*TPU/3-10);
			}
			sleep_flag=0;
			delay_ms(2*TPU);
			receiveDataPrep();
			while(PrepAckFlag)
			{
				 comPrintString("\r\n!!retry times:::::");
				 k++;
				 com_send_hex(k);
				 com_send_hex(k);
				 if(k<=25)
				 {
				 	tx_frame[0] = 0x61; //IEEE 802.15.4的标准包头帧
					tx_frame[1] = 0x88; //IEEE 802.15.4的标准包头帧
					tx_frame[2]= 0; //封包序列号
					tx_frame[3] = PAN_ID & 0xFF; //网络地址低8位
					tx_frame[4] = (PAN_ID >> 8 ) & 0xFF; //网络地址高8位
					tx_frame[5] = 0xFF; //目标短地址低8位.
					tx_frame[6] = 0xFF; //目标短地址高8位
					tx_frame[7] = sAddr & 0xFF; //发送源短地址低8位
					tx_frame[8] = (sAddr>>8)& 0xFF;//发送源短地址高8位
					tx_frame[9] =0xFF;		   
					tx_frame[10]=0xFF;
					tx_frame[11]=SCMD_PREP;
					tx_frame[12]=dataSendBuffer[dataSendBufferHead].dAddr[0];
					tx_frame[13]=dataSendBuffer[dataSendBufferHead].dAddr[1];
					for(i=0;i<3;i++)
					{
						comPrintString("\r\n<<Sending Prep...");
						if (set_trx_state( PLL_ON ) == TAT_SUCCESS) 
						{
							rx_flag = false;
							tat_send_data( 16, tx_frame);
							comPrintString("\r\n<<");
							for(j=0;j<=13;j++)
							{
								com_send_hex(tx_frame[j]);
								comPrintString(" ");
							}
							delay_ms(20);//状态转换时间
						}
						else 
							comPrintString("\r\n!!State Change Failed");
						if (set_trx_state( RX_AACK_ON ) != TAT_SUCCESS)  //转移状态机至RX_AACK_ON状态,以侦听传入包
							comPrintString("\r\n!!State Change Failed");
						rx_flag = true;
						delay_ms(2*TPU/3-10);
					}
					delay_ms(2*TPU);
					receiveDataPrep();
					SendWild(0xFFFF,SCMD_SADDR_BROADCAST);
			     }
	             if(k>25)
				 	break;
			}
		}
	}
    BusyTxSending=0;
}
void setShortAddress(uint16_t newShortAddress)
{
	tat_set_short_address(newShortAddress);
	sAddr=tat_get_short_address();
}
uint8_t min(uint8_t a,uint8_t b)
{
	return a>b?b:a;
}
uint8_t max(uint8_t a,uint8_t b)
{
	return a>b?a:b;
}
uint8_t abs(uint8_t a)
{
	return a<0?-a:a;
}
bool networkStateTest()
{
    int i;
	bool networkOK=true;
	comPrintString("\r\n!!Now is testing time~~~~~~~~~~");
	MaxNodeFlag=1;//是否应该把自己定为尾节点
	GotoStartFlag=0;//是否需要重起
	NetWorkFlag=0;//是否找到前驱节点
	rx_pool_network_maintain_buf_head=0;
	if(sAddr!=0x0000)
	{
		for(i=0;i<=4;i++)
		{	
			delay_ms(8*TPU);
			SendWild(0xFFFF,SCMD_SADDR_BROADCAST);
			delay_ms(2*TPU);
			receiveDatamaintain();
			if(GotoStartFlag)
				break;
			if(NetWorkFlag)
				break;       
		}
		if(NetWorkFlag==0)
		{
	     	comPrintString("\r\n!!My parant Lost~~~~~~~~~~~~");
			networkOK=false;
		}
	}
	if(networkOK)
	{
		for(i=0;i<=4;i++)
		{	
			delay_ms(8*TPU);
			SendWild(0xFFFF,SCMD_SADDR_BROADCAST);
			delay_ms(2*TPU);
			receiveDatamaintain();
			if(GotoStartFlag)
				break;
			if(MaxNodeFlag==0x00)
				break;    
		}
		if(MaxNodeFlag==0x01)
		{
	     	attri=0x01;
			comPrintString("\r\n!!I am the New Tail~~~~~~~~~~~");
			SendWild(sAddr,SCMD_SADDR_BROADCAST);
		}
	}
	if(GotoStartFlag!=0x00)
	{
		GotoStartFlag=0;
		comPrintString("\r\n!!Net work has sth wrong I have to go to start~~~~~~~~~~");
		networkOK=false;
	}
	return networkOK;
}
int main( void )     
{            
    neighborNode neighborTable[62];
	uint8_t tlength;
	int Num;
	uint8_t rssi_h,rssi_m,rssi_t;
	uint16_t sAddr_m,sAddr_t;
	int i,j;
	int k=0;
start:
	if(k>0)
	{
		for(i=0;i<=4;i++)
			delay_ms(15*TPU);
    }
	k++;
	Num=0;
	NewNodeFlag=1;
    rx_flag = true;    
	cli();
	rx_pool_init( );
	sei();
	avr_init( );
	trx_init( );
	delay_ms(20);
	if (set_trx_state( RX_AACK_ON ) != TAT_SUCCESS) //转移状态机至RX_AACK_ON状态,以侦听传入包
		com_send_string( debug_fatal_error, sizeof( debug_fatal_error ) );
	sei( );
	Timer4_init();//初始化发送延迟计数器
	hal_set_net_led();//亮灯,表示网络就绪
    tlength=scanNeighborNodes(neighborTable);//搜索到附近的节点
	rssi_h=0;
	rssi_m=0;
	rssi_t=0;
	for(i=0;i<tlength;i++)//寻找头结点场强
	{
		if(neighborTable[i].sAddr[0]==0x00 && neighborTable[i].sAddr[1]==0x00)
		{
			rssi_h=neighborTable[i].rssi;
		}
		if(neighborTable[i].attri==0x01)
		{
			rssi_t=neighborTable[i].rssi;
			sAddr_t=neighborTable[i].sAddr[0]+neighborTable[i].sAddr[1]*256;
		}
		for(j=i;j<tlength;j++)
		{
			if(neighborTable[i].sAddr[1]==neighborTable[j].sAddr[1] && abs(neighborTable[i].sAddr[0]-neighborTable[j].sAddr[0])==1
				&& min(neighborTable[i].rssi,neighborTable[j].rssi)>rssi_m)
			{
				rssi_m=min(neighborTable[i].rssi,neighborTable[j].rssi);
				sAddr_m=neighborTable[i].sAddr[1]*256+max(neighborTable[i].sAddr[0],neighborTable[j].sAddr[0]);
			}
		}
	}
	comPrintString("\r\n>>Head ED:");
	com_send_hex(rssi_h);
	comPrintString("\r\n>>Middle ED:");
	com_send_hex(rssi_m);
	comPrintString("\r\n>>TAIL ED:");
	com_send_hex(rssi_t);
	if(rssi_h==0 && rssi_m==0 && rssi_t==0)//未找到网络
	{
		#ifdef NETSEED//如果是NETSEED,就开启新网络
			comPrintString("\r\n!!Creating New Network...");
			setShortAddress(0x0000);
			NewNodeFlag=0;
			attri=0x01;
		#else//如果不是NETSEED，等待5秒后重新尝试入网
			delay_ms(1000);
			goto start:
		#endif
	}
	else if(rssi_t>=rssi_h && rssi_t>=rssi_m)//如果是rssi_t最大，执行尾插入网，发送尾节点占用广播
	{
		NewNodeFlag=1;
		Comparerssi=rssi_t;
		comPrintString("\r\n!!Current Tail Node Short Address is:");
		attri=0x0001;
		com_send_dec((sAddr_t>>8) & 0xFF);
		comPrintString(".");
		com_send_dec(sAddr_t&0xFF);
		setShortAddress(sAddr_t+1);
		receiveData();
		if(FirstTailFlag==1)
		{
			attri=0;
			FirstTailFlag=0;
			goto start;
        }
		comPrintString("\r\nBY first juege I am the First Tail~~~");
		SendWild(sAddr,SCMD_TAIL_FIRST_COME);
		comPrintString("\r\n<<Broadcast tail occupation...");
		for(i=0;i<New_Appling_Cover_Times;i++)
		{
			delay_ms(TPU);
			SendWild(sAddr,SCMD_TAIL_FIRST_COME);
			SendWild(sAddr-1,SCMD_TAIL_OCCUPY);//发送短地址占用广播
		}
		delay_ms(First_Wait_time*TPU);
		SendWild(sAddr,SCMD_TAIL_FIRST_COME);
		receiveData();
		if(FirstTailFlag==1)
		{		
			attri=0;
			FirstTailFlag=0;
			goto start;
        }
		if(TailConfirmFlag==0)
		{	
			attri=0;
			goto start;
        }
        NewNodeFlag=0;
		comPrintString("\r\nFinally I am the First Tail~~~");
		SendWild(sAddr,SCMD_SADDR_BROADCAST);
	}
	else if(rssi_m>=rssi_t && rssi_m>=rssi_h)//执行中插入网
	{
		NewNodeFlag=1;
		Comparerssi=rssi_m;
		comPrintString("\r\n!!Occupy Short Address:");
		com_send_dec((sAddr_m>>8) & 0xFF);
		comPrintString(".");
		com_send_dec(sAddr_m&0xFF);
		setShortAddress(sAddr_m);
		receiveData();
		if(FirstMedFlag==1)
		{		
			FirstMedFlag=0;
			goto start;
        }
		comPrintString("\r\n<<BY first juege I am the First Med...");
		SendWild(sAddr,SCMD_MED_FIRST_COME);
        receiveData();
		comPrintString("\r\n<<Med Broadcast shortAddress occupation...");
		setShortAddress(sAddr_m);
		for(i=0;i<New_Appling_Cover_Times;i++)
		{
			delay_ms(TPU);
			SendWild(sAddr,SCMD_MED_FIRST_COME);
			SendWild(sAddr-1,SCMD_SADDR_BROADCAST_NEW);
		}
		delay_ms(First_Wait_time*TPU);
		SendWild(sAddr,SCMD_MED_FIRST_COME);
		receiveData();
	    if(NewConfirmFlag==0)
		{	
			goto start;
		}
        if(FirstMedFlag==1)
		{
			FirstMedFlag=0;
			goto start;
        }
		NewNodeFlag=0;
	   	comPrintString("\r\nFinally I am the First Med~~~");
		for(i=0;i<=Broad_Cover_Times;i++)
		{
			delay_ms(TPU);
			SendWild(sAddr,SCMD_MED_FIRST_COME);
			SendWild(sAddr,SCMD_SADDR_BROADCAST);
        }
		rx_pool_init();
    }
	else if(rssi_h>=rssi_t && rssi_h>=rssi_m)//如果头结点场强最大，执行头插入网，发送头结点占用广播
	{
		NewNodeFlag=1;
		Comparerssi=rssi_h;
		setShortAddress(0x0000);
		receiveData();
		if(FirstHeadFlag==1)
		{
			FirstHeadFlag=0;
			goto start;
        }
		comPrintString("\r\nBY first juege I am the First Head~~~");
		SendWild(sAddr,SCMD_HEAD_FIRST_COME);
        receiveData();
		if(FirstHeadFlag==1)
		{
			FirstHeadFlag=0;
			goto start;
        }
		comPrintString("\r\n<<Broadcast head occupation...");
		for(i=0;i<New_Appling_Cover_Times;i++)
		{
			delay_ms(TPU);
			SendWild(sAddr,SCMD_HEAD_FIRST_COME);
			SendWild(sAddr,SCMD_HEAD_OCCUPY);
	    }	
		delay_ms(First_Wait_time*TPU);
		SendWild(sAddr,SCMD_HEAD_FIRST_COME);
		receiveData();
	    if(FirstHeadFlag==1)
		{
			FirstHeadFlag=0;
			goto start;
        }
		if(HeadConfirmFlag==0)
		{
			goto start;
		}
        NewNodeFlag=0;
		comPrintString("\r\nFinally I am the First Head~~~");
        for(i=0;i<Broad_Cover_Times;i++)
		{
			delay_ms(TPU);
			SendWild(sAddr,SCMD_HEAD_FIRST_COME);
			SendWild(sAddr,SCMD_SADDR_BROADCAST);
        }	
		rx_pool_init();
	}
	while (true)//主循环
	{
	    if(sleep_flag)
		{
			if(tat_enter_sleep_mode()==TAT_SUCCESS)
			{
				comPrintString("\r\n!!Enter sleep mode");
				routine();
				delay_ms(1*TPU);
				tat_leave_sleep_mode();
			}//睡眠8个TPU后唤醒
			trx_init( );
		}
		if (set_trx_state( RX_AACK_ON ) != TAT_SUCCESS) //转移状态机至RX_AACK_ON状态,以侦听传入包
			com_send_string( debug_fatal_error, sizeof( debug_fatal_error ) );
		else
			comPrintString("\r\n!!Waked Up.");
		if(!sleep_flag)
		{
			delay_ms(1*TPU);
			sleep_flag=1;
		}
		comPrintString("\r\n!!Current short Address is:");
		com_send_dec((sAddr>>8)&0xFF);
		comPrintString(".");
		com_send_dec(sAddr&0xFF);
		comPrintString("\r\n!!Net work NUM::::::::::");
		com_send_hex(Num);
		if(Num==5)
		{
			Num=0;
	    	if(!networkStateTest())
				goto start;
		}
		Num++;
		/*------处理发送池的信息------*/
		/*------处理接收到的信息------*/
		delay_ms(9*TPU);
		sendPrep();
		receiveData();
		receiveDataPrep();
		receiveDatamaintain();	
	
		hal_set_data_led();//亮灯,表示发送数据
		SendWild(0xFFFF,SCMD_SADDR_BROADCAST);
		com_reset_receiver();
		cli();
		rx_pool_init( );
		sei();
	}
	return 0;
}
uint8_t scanNeighborNodes(neighborNode* neighborTable)
{
	uint8_t i=0;
	uint8_t rssi=0;
	short int searchCount=0;
	comPrintString("\r\n!!Start to search the neighbor nodes...");
	delay_ms(20);
	if (set_trx_state( RX_ON ) != TAT_SUCCESS) //转移状态机至RX_AACK_ON状态,以侦听传入包
	{
		comPrintString("\r\n!!FETAL ERROR");
	} 
	while(searchCount<Search_Count_Number)
	{
		if (rx_pool_items_used != 0) //查看接收池中是否有未处理的包
		{
			readRxPool();
			if(rx_pool_tail->data[9]== 0xFF && rx_pool_tail->data[10]==0xFF && rx_pool_tail->data[11]==SCMD_SADDR_BROADCAST)//收到短地址广播
			{
				neighborTable[i].sAddr[0]=rx_pool_tail->data[7];
				neighborTable[i].sAddr[1]=rx_pool_tail->data[8];
				tat_do_ed_scan(&rssi);
				neighborTable[i].rssi=rssi;
				neighborTable[i].attri=rx_pool_tail->data[12];
				com_send_string(search_found,sizeof(search_found));
				com_send_string(search_sAddr,sizeof(search_sAddr));
				com_send_dec(neighborTable[i].sAddr[1]);
				comPrintString(".");
				com_send_dec(neighborTable[i].sAddr[0]);
				if(neighborTable[i].attri==0x01)
				{
					comPrintString("(is Tail)");
				}
				com_send_string(search_rssi,sizeof(search_rssi));
				com_send_hex(neighborTable[i].rssi);
				i++;
			}
		}
		else
		{
			delay_ms(4*TPU/5);
		}
		searchCount++;
	}
	comPrintString("\r\n!!Searching Finished.Number of Neighbor Nodes Found:");
	com_send_dec(i);
	delay_ms(20);
	if (set_trx_state( RX_AACK_ON ) != TAT_SUCCESS)  //转移状态机至RX_AACK_ON状态,以侦听传入包
	{
		comPrintString("\r\n!!FETAL ERROR");
	}
	return i;
}
void readRxPool(void)
{
	hal_set_data_led();//亮灯,表示收到信息
	if (rx_pool_tail == rx_pool_end) 
	{
		rx_pool_tail = rx_pool_start;
	} 
	else 
	{
		++rx_pool_tail;
	} 
	cli( );
	++rx_pool_items_free;
	--rx_pool_items_used;
	sei( );//接收池中数据出队
	hal_clear_data_led();//灭灯
}
 /*EOF*/
