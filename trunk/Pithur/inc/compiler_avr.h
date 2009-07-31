/* This file has been prepared for Doxygen automatic documentation generation.*/
/*! \file *********************************************************************
 *
 * \brief This file implements some macros that makes the IAR C-compiler and 
 *        avr-gcc work with the same code base for the AVR architecture.
 *
 * \par Application note:
 *      AVR2001: AT86RF230 Software Programmer's Guide
 *
 * \par Documentation
 *      For comprehensive code documentation, supported compilers, compiler 
 *      settings and supported devices see readme.html
 *
 * \author
 *      Atmel Corporation: http://www.atmel.com \n
 *      Support email: avr@atmel.com
 * 
 * $Name$
 * $Revision: 613 $
 * $RCSfile$
 * $Date: 2006-04-07 14:40:07 +0200 (fr, 07 apr 2006) $  \n
 *
 * Copyright (c) 2006, Atmel Corporation All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name of ATMEL may not be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY ATMEL ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE EXPRESSLY AND
 * SPECIFICALLY DISCLAIMED. IN NO EVENT SHALL ATMEL BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/

#ifndef COMPILER_AVR_H
#define COMPILER_AVR_H

/** This macro will protect the following code from interrupts.*/
#define AVR_ENTER_CRITICAL_REGION( ) {uint8_t volatile saved_sreg = SREG; cli( );

/** This macro must always be used in conjunction with AVR_ENTER_CRITICAL_REGION
    so that interrupts are enabled again.*/
#define AVR_LEAVE_CRITICAL_REGION( ) SREG = saved_sreg;}

#if defined( __ICCAVR__ )

#include <inavr.h>
#include <ioavr.h>
#include <intrinsics.h>

#include "crc16.h"

/* program memory space abstraction */
#define FLASH_DECLARE(x) __flash x
#define FLASH_STRING(x) ((__flash const char *)(x))
#define FLASH_STRING_T  char const __flash *
#define PGM_READ_BYTE(x) *(x)
#define PGM_READ_WORD(x) *(x)
#define PGM_READ_BLOCK(dst, src, len) memcpy_P((dst), (src), (len))
#define PGM_STRLEN(x) strlen_P(x)
#define PGM_STRCPY(dst, src) strcpy_P((dst), (src))
/* IAR has no vsnprintf_P(), and no %S format. */
#define HAS_PGM_VSNPRINTF 0
#define PGM_VSNPRINTF(dst, n, fmt, ap) vsnprintf((dst), (n), (fmt), (ap))
#undef PRINTF_FLASH_STRING

#define ALIGN8BIT /**/
#define SHORTENUM /**/

#define ENABLE_XRAM(void) \
char __low_level_init(void)
#define RETURN_ENABLE_XRAM return 1


#define CAN_INITIALIZE_FLEXIBLE_ARRAY_MEMBERS 0/**
   Perform a delay of \c us microseconds.

   The macro F_CPU is supposed to be defined to a constant defining the CPU 
   clock frequency (in Hertz).

   The maximal possible delay is 262.14 ms / F_CPU in MHz.

   \note For the IAR compiler, currently F_CPU must be a
   multiple of 1000000UL (1 MHz).
 */
#define delay_us( us )   ( __delay_cycles( ( F_CPU / 1000000UL ) * ( us ) ) )

/*
 * Some preprocessor magic to allow for a header file abstraction of
 * interrupt service routine declarations for the IAR compiler.  This
 * requires the use of the C99 _Pragma() directive (rather than the
 * old #pragma one that could not be used as a macro replacement), as
 * well as two different levels of preprocessor concetanations in
 * order to do both, assign the correct interrupt vector name, as well
 * as construct a unique function name for the ISR.
 *
 * Do *NOT* try to reorder the macros below, or you'll suddenly find
 * out about all kinds of IAR bugs...
 */
#define PRAGMA(x) _Pragma( #x )
#define ISR(vec) PRAGMA( vector=vec ) __interrupt void handler_##vec(void)
#define sei( ) (__enable_interrupt( ))
#define cli( ) (__disable_interrupt( ))

#define watchdog_reset( ) (__watchdog_reset( ))

#define INLINE PRAGMA( inline=forced ) static

#elif defined( __GNUC__ )

#include <avr/io.h>
#include <avr/interrupt.h>
# include <avr/pgmspace.h>

#include <util/crc16.h>
#include <util/delay.h>
#include <avr/power.h>
#include <avr/sleep.h>
#include <avr/sfr_defs.h>
#define delay_us( us )   (_delay_us( us ))
#define delay_ms( ms )   (_delay_ms( ms ))


#define INLINE static inline
#define crc_ccitt_update( crc, data ) _crc_ccitt_update( crc, data )

#define __x 
#define __z 

#else
#error Compiler not supported.
#endif
#endif
