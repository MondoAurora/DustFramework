/*
 * mem.c
 *
 *  Created on: 2011.11.23.
 *      Author: lkedves
 */

#include "dust.h"
#include "kernel.h"

#include "coll.h"

Handle dustKernelCollCreate(int itemSize, int initCount);
void dustKernelCollRelease(Handle* phColl);

void* dustKernelCollGetBlock(Handle hColl, Handle hItem);
void dustKernelCollAccessBlock(Handle hColl, Handle hItem, void* target, AccessOp access);

Handle dustKernelCollCreate(int itemSize, int initCount) {
	Handle ret = dustKernelMemAlloc(itemSize * initCount + sizeof(DustColl));

	DustColl *pColl = (DustColl*) dustKernelMemGetBlock(ret);

	pColl->capacity = initCount;
	pColl->itemSize = itemSize;

	return ret;
}

void dustKernelCollRelease(Handle* phColl) {
	dustKernelMemRelease(phColl);
}

void* dustKernelCollGetBlock(Handle hColl, Handle hItem) {
	DustColl *pColl = (DustColl*) dustKernelMemGetBlock(hColl);

	return &(pColl->content) + (hItem * pColl->itemSize);
}

void dustKernelCollAccessBlock(Handle hColl, Handle hItem, void* target, AccessOp access) {
	DustColl *pColl = (DustColl*) dustKernelMemGetBlock(hColl);

	dustKernelMemAccessBlock(hColl, sizeof(DustColl) - 1 + (hItem * pColl->itemSize), pColl->itemSize, target, access);
}


