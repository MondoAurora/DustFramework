/*
 * boot.h
 *
 *  Created on: 2011.11.24.
 *      Author: lkedves
 */

#ifndef BOOT_H_
#define BOOT_H_

#include <boot.h>

void bootTrace(Reference refUnit, Reference refMsg, const char* msg);

#define BOOT_UNIT_MAIN 0

#define BOOT_MSG_MAIN_TRACE 0
#define BOOT_MSG_MAIN_CALL 1

#define bootTraceMsg(msg) bootTrace(BOOT_UNIT_MAIN, BOOT_MSG_MAIN_TRACE, (msg))
#define bootTraceCall(msg) bootTrace(BOOT_UNIT_MAIN, BOOT_MSG_MAIN_CALL, (msg))

void dustBootMemInit();

void dustBootCollInit();

void dustBootContextInit();

void dustBootThreadInit();


#endif /* BOOT_H_ */
