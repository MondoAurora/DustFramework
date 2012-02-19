/*
 * context.c
 *
 *  Created on: 2011.11.24.
 *      Author: lkedves
 */

#include "dust.h"
#include "kernel.h"
#include "boot.h"

#include "context.h"


KernelContext* pContext;

KernelContext* dustKernelThreadGetContext() {
	return pContext;
}



void dustBootThreadInit() {
	pContext = dustKernelContextCreateContext(HANDLE_NULL);
}
