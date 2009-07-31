/*============================ PROTOTYPES ====================================*/
bool trx_init( void );
void avr_init( void );
void trx_end_handler( uint32_t time_stamp );
void rx_pool_init( void );
void addSendData(uint16_t dAddr,uint8_t data[]);
uint16_t sAddr=0xFFFE;//待入网零时地址
static short int dataSendBufferHead=0;
/*============================ MACROS ========================================*/
#define SCMD_SADDR_BROADCAST		      (0x00)
#define SCMD_PREP					      (0x01)
#define SCMD_TAIL_OCCUPY			      (0x03)
#define SCMD_PREP_RECEIVED			      (0x04)
#define SCMD_SADDR_BROADCAST_NEW	      (0x05)
#define SCMD_SADDR_BROADCAST_NEW_CONFIRM  (0x06)
#define SCMD_TAIL_OCCUPY_CONFIRM          (0x07)
#define SCMD_HEAD_OCCUPY                  (0x08)
#define SCMD_HEAD_OCCUPY_CONFIRM          (0x09)
#define SCMD_TAIL_FIRST_COME              (0x10)
#define SCMD_HEAD_FIRST_COME              (0x11)
#define SCMD_MED_FIRST_COME               (0x12)
//#define SCMD_IMPORTANT_ACK              (0x13)
#define PanIdH            				  (0xEE)
#define PanIdL            				  (0xCC)
#define TPU						          (300)//定义时间单位为1秒。此值越大，性能越低，稳定性越好，功耗越低
#define NETSEED
#define First_Wait_time                   (10)//此值需小于等于20
#define Search_Count_Number               (60)
#define Broad_Cover_Times                 (20)
#define New_Appling_Cover_Times           (10)
/*============================ TYPEDEFS ======================================*/
typedef struct
{
	uint8_t sAddr[2];
	uint8_t rssi;
	uint8_t attri;//0=NULL,1=TAIL
}neighborNode;

typedef struct
{
	uint8_t dAddr[2];
	uint8_t data[5];
}DataElement;

typedef struct 
{
	uint8_t rAddr[2];
}PrepReceive;
typedef struct
{
	uint8_t rAddr[2];
	uint8_t rssi;
	uint8_t attri;//0=NULL,1=TAIL
	uint8_t PreprAddr[2];
}PrepReceiveNew;
typedef struct
{
    uint8_t data[ 20 ];
} hal_rx_frame_t_network_maintain;
typedef struct
{
    uint8_t data[ 20 ]
} hal_rx_frame_t_network_form;
typedef struct
{
    uint8_t data[ 20 ]
} hal_rx_frame_t_network_prep;
static DataElement dataSendBuffer[16];
