###############################################################################
# Makefile for the project test1
###############################################################################

## General Flags
PROJECT = test1
MCU = atmega1281
TARGET = test1.elf
CC = avr-gcc

## Options common to compile, link and assembly rules
COMMON = -mmcu=$(MCU)

## Compile options common for all C compilation units.
CFLAGS = $(COMMON)
CFLAGS += -Wall -gdwarf-2 -std=gnu99   -DF_CPU=7372800UL -Os -funsigned-char -funsigned-bitfields -fpack-struct -fshort-enums
CFLAGS += -MD -MP -MT $(*F).o -MF dep/$(@F).d 

## Assembly specific flags
ASMFLAGS = $(COMMON)
ASMFLAGS += $(CFLAGS)
ASMFLAGS += -x assembler-with-cpp -Wa,-gdwarf2

## Linker flags
LDFLAGS = $(COMMON)
LDFLAGS +=  -Wl,-Map=test1.map


## Intel Hex file production flags
HEX_FLASH_FLAGS = -R .eeprom -R .fuse -R .lock -R .signature

HEX_EEPROM_FLAGS = -j .eeprom
HEX_EEPROM_FLAGS += --set-section-flags=.eeprom="alloc,load"
HEX_EEPROM_FLAGS += --change-section-lma .eeprom=0 --no-change-warnings


## Include Directories
INCLUDES = -I"C:\scale\scale\pithur\pithurnenewest\inc" 

## Library Directories
LIBDIRS = -L"C:\scale\scale\pithur\pithurnenewest\lib\avr\gcc" 

## Objects that must be built in order to link
OBJECTS = com.o hal_avr.o sal.o tat.o network.o app.o 

## Objects explicitly added by the user
LINKONLYOBJECTS = 

## Build
all: $(TARGET) test1.hex test1.eep test1.lss size

## Compile
com.o: ../source/com.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

hal_avr.o: ../source/hal_avr.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

sal.o: ../source/sal.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

tat.o: ../source/tat.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

network.o: ../network.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

app.o: ../app.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

##Link
$(TARGET): $(OBJECTS)
	 $(CC) $(LDFLAGS) $(OBJECTS) $(LINKONLYOBJECTS) $(LIBDIRS) $(LIBS) -o $(TARGET)

%.hex: $(TARGET)
	avr-objcopy -O ihex $(HEX_FLASH_FLAGS)  $< $@

%.eep: $(TARGET)
	-avr-objcopy $(HEX_EEPROM_FLAGS) -O ihex $< $@ || exit 0

%.lss: $(TARGET)
	avr-objdump -h -S $< > $@

size: ${TARGET}
	@echo
	@avr-size -C --mcu=${MCU} ${TARGET}

## Clean target
.PHONY: clean
clean:
	-rm -rf $(OBJECTS) test1.elf dep/* test1.hex test1.eep test1.lss test1.map


## Other dependencies
-include $(shell mkdir dep 2>/dev/null) $(wildcard dep/*)

