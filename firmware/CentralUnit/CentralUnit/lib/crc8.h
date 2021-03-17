//***************************************************************************
//Library: CRC8
//Description: Calculating crc check
//Version: 1.0
//Type: Header file
//Controller: ATmega8 
//Clock: 7,3728 MHz
//Author: Vladimír Kunštár
//***************************************************************************

#ifndef CRC8_H_
#define CRC8_H_

#include <inttypes.h>

uint8_t	crc8 (uint8_t* data_in, uint16_t number_of_bytes_to_read);

#endif
