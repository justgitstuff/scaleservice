#include     <stdio.h>     

#include     <stdlib.h>  

#include     <string.h>  

#include     <unistd.h>    

#include     <sys/types.h> 

#include     <sys/stat.h>  

#include     <fcntl.h>     

#include     <termios.h>   

#include     <errno.h> 

#include "/usr/include/mysql/mysql.h"



#define FALSE  -1

#define TRUE   0

 

int speed_arr[] = { B115200,B57600,B38400, B19200, B9600, B4800, B2400, B1200, B300 };

int name_arr[] = {115200,57600,38400, 19200,  9600,  4800,  2400,  1200,  300};

MYSQL connection;
MYSQL_RES *res_ptr;
MYSQL_ROW sqlrow;
char str1[63]="call receiveData(\'";
char str2[62]="call addControlTarget(\'";
char str3[63]="call lAddrIDSensorName(\'";
char str4[64]="call lAddrIDManufacture(\'";
char str5[64]="call lAddrIDDescription(\'";


void iniconnect()
{
	mysql_init(&connection);
	mysql_real_connect(&connection,"localhost","root",NULL,"scaledb",0,NULL,0);
	
	
}

void set_speed(int fd, int speed){

         int   i;

         int   status;

         struct termios   Opt;

         tcgetattr(fd, &Opt);

         for ( i= 0;  i < sizeof(speed_arr) / sizeof(int);  i++) {

                   if  (speed == name_arr[i]) {    

                            tcflush(fd, TCIOFLUSH);    

                            cfsetispeed(&Opt, speed_arr[i]); 

                            cfsetospeed(&Opt, speed_arr[i]);  

                            status = tcsetattr(fd, TCSANOW, &Opt); 

                            if  (status != 0) {       

                                     perror("tcsetattr fd"); 

                                     return;    

                            }   

                            tcflush(fd,TCIOFLUSH);  

                   } 

         }

}

 

int set_Parity(int fd,int databits,int stopbits,int parity)

{

         struct termios options;

         if  ( tcgetattr( fd,&options)  !=  0) {

                   perror("SetupSerial 1");    

                   return(FALSE); 

         }

         options.c_cflag &= ~CSIZE;

         options.c_lflag  &= ~(ICANON | ECHO | ECHOE | ISIG); 

         options.c_oflag  &= ~OPOST;  

 

         switch (databits)

         {  

         case 7:                

                   options.c_cflag |= CS7;

                   break;

         case 8:    

                   options.c_cflag |= CS8;

                   break;  

         default:   

                   fprintf(stderr,"Unsupported data size\n"); return (FALSE); 

         }

switch (parity)

{  

         case 'n':

         case 'N':   

                   options.c_cflag &= ~PARENB;  

                   options.c_iflag &= ~INPCK;    

                   break; 

         case 'o':  

         case 'O':    

                   options.c_cflag |= (PARODD | PARENB);  

                   options.c_iflag |= INPCK;            

                   break; 

         case 'e': 

         case 'E':  

                   options.c_cflag |= PARENB;        

                   options.c_cflag &= ~PARODD;       

                   options.c_iflag |= INPCK;      

                   break;

         case 'S':

         case 's':    

             options.c_cflag &= ~PARENB;

                   options.c_cflag &= ~CSTOPB;break; 

         default:  

                   fprintf(stderr,"Unsupported parity\n");   

                   return (FALSE); 

         } 

 

switch (stopbits)

{  

         case 1:   

                   options.c_cflag &= ~CSTOPB; 

                   break; 

         case 2:   

                   options.c_cflag |= CSTOPB; 

            break;

         default:   

                    fprintf(stderr,"Unsupported stop bits\n"); 

                    return (FALSE);

}

 

if (parity != 'n')  

         options.c_iflag |= INPCK;

tcflush(fd,TCIFLUSH);

options.c_cc[VTIME] = 0;   

options.c_cc[VMIN] = 37;

if (tcsetattr(fd,TCSANOW,&options) != 0)  

{

         perror("SetupSerial 3");  

         return (FALSE); 

}

return (TRUE); 

}

 

 

 

int OpenDev(char *Dev)

{

         int     fd = open( Dev, O_RDWR );

        //| O_NOCTTY | O_NDELAY         

         if (-1 == fd)        

         {                        

                   perror("Can't Open Serial Port");

                   return -1;            

         }      

         else  

                   return fd;

}

int main()
{
	    int i,j,k,m=0;

         int fd;

         int nread;

         char tempbuff[37];


         char *dev  = "/dev/ttyS0"; //串口1
	start:
         fd = OpenDev(dev);

         set_speed(fd,57600);

         if (set_Parity(fd,8,1,'N') == FALSE)  {

                   printf("Set Parity Error\n");

                   //return -1;

         }
	
	   iniconnect();

         while (1) //循环读取数据

         {  

               if((nread = read(fd, tempbuff, 37))>0)

               {

                            //printf("\nLen %d\n",nread);

                    //printf("%s",tempbuff);
					//printf("%c\n",tempbuff[1]);
					for(m=0;tempbuff[m]!='#';m++);
						m++;
					if(tempbuff[m]=='@')
					{
						m++;
						for(i=0;i<16;i++)
							str2[23+i]=tempbuff[m++];
						str2[39]='\'';
						str2[40]=',';
						str2[41]='\'';
						
						for(j=0;j<17;j++)
							str2[42+j]=tempbuff[m++];
						str2[59]='\'';
						str2[60]=')';
						str2[61]='\0';
						printf("%s\n",str2);
						if(strlen(str2)!=61)
							goto start;
						mysql_query(&connection,str2);
					}
					
					else if(tempbuff[m]=='$')
					{
						m++;
						for(i=0;i<16;i++)
							str3[24+i]=tempbuff[m++];
						str3[40]='\'';
						str3[41]=',';
						str3[42]='\'';
						
						for(j=0;j<17;j++)
							str3[43+j]=tempbuff[m++];
						str3[60]='\'';
						str3[61]=')';
						str3[62]='\0';
						printf("%s\n",str3);
						if(strlen(str3)!=62)
							goto start;
						mysql_query(&connection,str3);
					}
					
					else if(tempbuff[m]=='%')
					{
						m++;
						for(i=0;i<16;i++)
							str4[25+i]=tempbuff[m++];
						str4[41]='\'';
					    str4[42]=',';
					    str4[43]='\'';
					    
					    for(j=0;j<17;j++)
					    	str4[44+j]=tempbuff[m++];
					    str4[61]='\'';
					    str4[62]=')';
					    str4[63]='\0';
					    printf("%s\n",str4);
						if(strlen(str4)!=63)
							goto start;
					    mysql_query(&connection,str4);
					}
					
					else if(tempbuff[m]=='^')
					{
						m++;
						for(i=0;i<16;i++)
							str5[25+i]=tempbuff[m++];
						str5[41]='\'';
						str5[42]=',';
						str5[43]='\'';
						
						for(j=0;j<17;j++)
							str5[44+j]=tempbuff[m++];
						str5[61]='\'';
						str5[62]=')';
						str5[63]='\0';
						printf("%s\n",str5);
						if(strlen(str5)!=63)
							goto start;
						mysql_query(&connection,str5);
					}
						
						
					else{
					
				   		for(i=0;i<16;i++)
    					str1[18+i]=tempbuff[m++];
	    				str1[34]='\'';
	    				str1[35]=',';
	    				str1[36]='\'';
	    		
	    				for(j=0;j<8;j++)
	    					str1[37+j]=tempbuff[m++];
	    				str1[45]='\'';
	    				str1[46]=',';
	    				str1[47]='\'';
	    		
	    				for(k=0;k<10;k++)
	    					str1[48+k]=tempbuff[m++];
	    	    
	    	    			str1[58]='\'';
	    	    			str1[59]=')';
	    	    			str1[60]='\0';
	    	    		printf("%s\n",str1);
				if(strlen(str1)!=60)
					goto start;
	    	    		mysql_query(&connection,str1);
					}
    	    		
    	    		

              }
			
			

                            
         }

         //return buff;

         close(fd);
         return 0;



         //close(fd); 

         // exit (0);
}


