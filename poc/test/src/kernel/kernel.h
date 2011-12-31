/*
 * kernel.h
 *
 *  Created on: 2011.11.23.
 *      Author: lkedves
 */

#ifndef KERNEL_H_
#define KERNEL_H_

/**************************
 *
 * Memory
 *
 **************************/

Handle dustKernelMemAlloc(int size);
Handle dustKernelMemReAlloc(int size, Handle hOrigBlock);
void dustKernelMemRelease(Handle* phBlock);

void* dustKernelMemGetBlock(Handle hBlock);
void dustKernelMemAccessBlock(Handle hBlock, int offset, int size, void* target, DustAccOp access);


/**************************
 *
 * Collection
 *
 **************************/

Handle dustKernelCollCreate(int itemSize, int initCount);
void dustKernelCollRelease(Handle* phColl);

void* dustKernelCollGetBlock(Handle hColl, Handle hItem);
void dustKernelCollAccessBlock(Handle hColl, Handle hItem, void* target, DustAccOp access);
DustBool dustKernelCollContains(Handle hColl, Handle hItem);

void dustKernelCollSetAdd(Handle hColl, Handle hItem);

Handle dustKernelCollMapGet(Handle hColl, Reference refKey);
void dustKernelCollMapPut(Handle hColl, Reference refKey, Handle hItem);

void dustKernelCollManage(Handle hColl, DustCollOp op, Handle hItem, int index);



/**************************
 *
 * Unit
 *
 **************************/

Handle dustKernelUnitCreateEntity(Handle hUnit, Reference refType);
Handle dustKernelUnitGetReferredEntity(Handle hUnit, Reference refEntity);

Handle dustKernelUnitGetReferredChannel(Handle hUnit, Reference refChannel);

void dustKernelUnitManageField(Handle hUnit, Handle hEntity, Reference refField, void* source, DustAccOp op);


/**************************
 *
 * Context
 *
 **************************/


/**************************
 *
 * Thread
 *
 **************************/

Handle dustKernelThreadGetContextHandle();

#endif /* KERNEL_H_ */
