/*
 * test_gen_init.c
 *
 *  Created on: 2012.01.04.
 *      Author: lkedves
 */

#include <dust.h>

#include <boot.h>

#include "test_gen_const.h"

char *fieldTypes[] = { "void", "bool", "byte", "int32", "float" };

FieldDef fd001t001 = { "field1", DUST_FIELDTYPE_INT, 1, 0 };
FieldDef fd002t001 = { "field2", DUST_FIELDTYPE_INT, 1, &fd001t001 };

FieldDef fd001t002 = { "field1", DUST_FIELDTYPE_INT, 1, 0 };

TypeDef t001 = { "type1", &fd002t001, 0 };
TypeDef t002 = { "type2", &fd001t002, &t001 };

UnitDef udef = { "dust", "test", "one", &t002 };

int dustGenInit() {
	int ret = 0;

	bootTraceMsg("dustGenInit start");

	// here comes the core module initialization.
	// if there are configurable parameters to be passed to them (memory, collection, etc),
	// the init code is generated accordingly and the calls contain those values.

	Handle h = dustKernelUnitAdd(&udef);

	h = dustBootContextInit(h);

	dustBootThreadInit(h);

	// Then it's time to call the boot code of the unit
#ifdef unitInit
	ret = unitInit();
#endif

	bootTraceMsg("dustGenInit end");

	return ret;
}
