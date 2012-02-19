/*
 * kernel.h
 *
 *  Created on: 2011.11.23.
 *      Author: lkedves
 */

#ifndef KERNEL_H_
#define KERNEL_H_

typedef struct KernelContext {
	Handle hMemMgr;
	Handle hTypeMgr;
} KernelContext;

/**************************
 *
 * Memory
 *
 **************************/

Handle dustKernelMemAlloc(int size);
Handle dustKernelMemReAlloc(int size, Handle hOrigBlock);
void dustKernelMemRelease(Handle* phBlock);

void* dustKernelMemGetBlock(Handle hBlock);
void dustKernelMemAccessBlock(Handle hBlock, int offset, int size, void* target, AccessOp access);


/**************************
 *
 * Collection
 *
 **************************/

Handle dustKernelCollCreate(int itemSize, int initCount);
void dustKernelCollRelease(Handle* phColl);

void* dustKernelCollGetBlock(Handle hColl, Handle hItem);
void dustKernelCollAccessBlock(Handle hColl, Handle hItem, void* target, AccessOp access);

/**************************
 *
 * Unit
 *
 **************************/

int dustKernelUnitGetTypeSize(KernelContext* pCtx, Reference refType);

/**************************
 *
 * Context
 *
 **************************/

KernelContext* dustKernelContextCreateContext(Handle hParentContext);
Handle dustKernelContextCreateEntity(KernelContext* pCtx, Reference refType);

/**************************
 *
 * Thread
 *
 **************************/

KernelContext* dustKernelThreadGetContext();

#endif /* KERNEL_H_ */
