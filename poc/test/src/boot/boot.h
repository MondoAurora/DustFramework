/*
 * boot.h
 *
 * The generated boot code should include this file, this contains
 * all the functions that the boot initializer codes can use
 *
 *  Created on: 2011.11.24.
 *      Author: lkedves
 */

#ifndef BOOT_H_
#define BOOT_H_

void bootTrace(Reference refUnit, Reference refMsg, const char* msg);

#define BOOT_UNIT_MAIN 0

#define BOOT_MSG_MAIN_TRACE 0
#define BOOT_MSG_MAIN_CALL 1

#define bootTraceMsg(msg) bootTrace(BOOT_UNIT_MAIN, BOOT_MSG_MAIN_TRACE, (msg))
#define bootTraceCall(msg) bootTrace(BOOT_UNIT_MAIN, BOOT_MSG_MAIN_CALL, (msg))


typedef struct FieldDef_ {
	char* id;
	int fieldType;
	int count;
	struct FieldDef_* next;
} FieldDef;

typedef struct TypeDef_ {
	char* id;
	FieldDef* fieldChain;
	struct TypeDef_* next;
} TypeDef;

typedef struct {
	char* vendor;
	char* domain;
	char* id;

	TypeDef* typeChain;
} UnitDef;

int dustGenInit();

Handle dustKernelUnitAdd();

#endif /* BOOT_H_ */
