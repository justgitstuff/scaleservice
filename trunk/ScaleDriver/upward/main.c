#include <termios.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/signal.h>
#include <sys/types.h>

#define BAUDRATE B38400
#define MODEMDEVICE "/dev/ttyS1"
//#define _POSIX_SOURCE 1 /* POSIX 系统兼容 */
#define FALSE 0
#define TRUE 1


//volatile int STOP= FALSE;
void signal_handler_IO(int status);
int wait_flag=TRUE;

int main()
{
	
	int fd;
	struct termios oldtio,newtio;
	struct sigaction saio;
	//char buf[255];


	fd=open(MODEMDEVICE,O_RDWR | O_NOCTTY | O_NONBLOCK);
	if (fd<0)
	{
		perror(MODEMDEVICE);
		exit(-1);
	}
	
	
	 saio.sa_handler = signal_handler_IO;
	//saio.sa_mask=0;
	saio.sa_flags=0;
	saio.sa_restorer = NULL;
	sigaction(SIGIO,&saio,NULL);
	
	fcntl(fd,F_SETFL,O_ASYNC);
	fcntl(fd,F_SETOWN,getpid());
	tcgetattr(fd,&oldtio);
	newtio.c_cflag = BAUDRATE | CRTSCTS | CS8 | CLOCAL | CREAD;
	newtio.c_iflag =IGNPAR | ICRNL;
	newtio.c_oflag = 0;
	newtio.c_lflag= ICANON;
	newtio.c_cc[VMIN]=1;
	newtio.c_cc[VTIME]=0;
	tcflush(fd,TCIFLUSH);
	tcsetattr(fd,TCSANOW,&newtio);
	
	while(1)
	{
		
		if(wait_flag==FALSE)
		{
			/*res = read(fd,buf,255);
			buf[res]='\0';
			printf(":%s:%d\n",buf,res);*/
			printf("receive signal");
			wait_flag = TRUE;
		}
	}
	tcsetattr(fd,TCSANOW,&oldtio);
	return 0;
}

void signal_handler_IO (int status)
{
	//printf("received SIGIO signal.\n");
	wait_flag = FALSE;
}

