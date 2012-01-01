/*
 * context.c
 *
 *  Created on: 2011.11.24.
 *      Author: lkedves
 */

#include <dust.h>
#include <kernel.h>

#include <boot.h>


void dustWait(Handle hProcOrGroup) {
	bootTraceCall("dustWait");
}


Handle tempCtxHandle = 0;

Handle dustKernelThreadGetContextHandle() {
	return tempCtxHandle;
}

void dustBootThreadInit() {
}
