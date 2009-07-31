#include <stdint.h>
#include <stdbool.h>
#include "com.h"
uint8_t com_buffer_use[ 100 ]; 
uint8_t com_buffer_useful[ 100 ]; 
extern sAddr;
/**
 * 每次设备达到固定的唤醒次数时会执行此函数
 * 
 * 
 */
void routine(void)
{
	uint8_t data[5]={0xAB,0xCD,0xEF,0xAA,0xAA};
	
/*	static int m=0;
	if(++m==2)
	{
		if(sAddr==0x0000)
		{
			addSendData(0x0000,data);
		}
		m=0;
	}*/
	int i;
	//com_get_received_data();
    comPrintString("\r\nPlease enter:::::::");
	delay_ms(2000);
	com_get_received_data_use(com_buffer_use);
	for(i=0;i<=20;i++)
	{
	com_buffer_useful[i]=com_buffer_use[i]-48;
	}
    for(i=0;i<=15;i++)
		com_send_hex(com_buffer_useful[i]);
	comPrintString("\r\n");
	for(i=0;i<=15;i++)
		com_send_hex(com_buffer_use[i]);
    if(com_buffer_useful[0]==0x09&&com_buffer_useful[1]==0x09)
	{
		comPrintString("Ready to Send ~~~~~~~");
		for(i=0;i<=4;i++)
		{
			data[i]=com_buffer_useful[i+4];
		}
		addSendData((com_buffer_useful[2]+256*com_buffer_useful[3]),data);
    }
	com_reset_receiver();
}
