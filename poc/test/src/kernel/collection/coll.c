/*
 * mem.c
 *
 *  Created on: 2011.11.23.
 *      Author: lkedves
 */

#include <dust.h>
#include <kernel.h>

#include "coll.h"

Handle dustKernelCollCreate(int itemSize, int initCount);
void dustKernelCollRelease(Handle* phColl);

void dustKernelCollAccessBlock(Handle hColl, Handle hItem, void* target, DustAccOp access);

Handle dustKernelCollCreate(int itemSize, int initCount) {
	return collCreate(itemSize, initCount,0);
}

void dustKernelCollRelease(Handle* phColl) {
	dustKernelMemRelease(phColl);
}

void* dustKernelCollGetBlock(Handle hColl, Handle hItem) {
	DustColl *pColl = collGetFromHandle(hColl);

	return &(pColl->content) + (hItem * pColl->itemSize);
}

void dustKernelCollAccessBlock(Handle hColl, Handle hItem, void* target, DustAccOp access) {
	DustColl *pColl = collGetFromHandle(hColl);

	dustKernelMemAccessBlock(hColl, sizeof(DustColl) - 1 + (hItem * pColl->itemSize), pColl->itemSize, target, access);
}

DustBool dustKernelCollContains(Handle hColl, Handle hItem) {
	return DUST_FALSE;
}


void dustKernelCollSetAdd(Handle hColl, Handle hItem) {

}

Handle dustKernelCollMapGet(Handle hColl, Reference refKey) {
	return HANDLE_UNKNOWN;
}

void dustKernelCollMapPut(Handle hColl, Reference refKey, Handle hItem) {

}

void dustKernelCollManage(Handle hColl, DustCollOp op, Handle hItem, int index) {

}

Handle dustKernelCollLazyMapCreate(int initCount, dustfnLazyMapFactory pfnFactory) {
	return collCreate(sizeof(Handle), initCount, pfnFactory);
}

Handle dustKernelCollLazyMapGet(Handle hColl, Reference refKey, void *pCtx) {
	return collMapGet(collGetFromHandle(hColl), refKey, pCtx);
}

DustColl* collGetFromHandle(Handle hColl) {
	return (DustColl*) dustKernelMemGetBlock(hColl);
}

Handle collCreate(int itemSize, int initCount, dustfnLazyMapFactory pfnFactory) {
	Handle ret = dustKernelMemAlloc(itemSize * initCount + sizeof(DustColl));

	DustColl *pColl = (DustColl*) dustKernelMemGetBlock(ret);

	pColl->capacity = initCount;
	pColl->itemSize = itemSize;
	pColl->pfnFactory = pfnFactory;

	return ret;
}

void collMapPut(DustColl *pColl, Reference refKey, Handle value) {
}

Handle collMapGet(DustColl *pColl, Reference refKey, void *pCtx) {
	Handle ret = HANDLE_UNKNOWN;

	// TODO search the collection

	if ( (HANDLE_UNKNOWN == ret) && (pCtx)  && (pColl->pfnFactory) ) {
		// TODO this point should be synchronized for the whole collection
		// you should not PUT an item with the same refKey as you are creating now

		ret = pColl->pfnFactory(refKey, pCtx);
		collMapPut(pColl, refKey, ret);
	}

	return ret;
}


