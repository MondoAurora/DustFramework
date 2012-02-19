/*
 * kernel_boot.h
 *
 * This is the API
 *
 *  Created on: 2011.11.24.
 *      Author: lkedves
 */

#ifndef KERNEL_BOOT_H_
#define KERNEL_BOOT_H_

#include <boot.h>

void dustBootMemInit();

void dustBootCollInit();

Handle dustBootContextInit(Handle hBootUnit);

void dustBootThreadInit(Handle initCtx);



#endif /* KERNEL_BOOT_H_ */
