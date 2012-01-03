/*
 * mem.c
 *
 *  Created on: 2011.11.23.
 *      Author: lkedves
 */

#include <dust.h>
#include <kernel.h>

#include "mem.h"

#include <malloc.h>
#include <string.h>


Handle dustKernelMemAlloc(int size) {
	void* ret = malloc(size);
	return (Handle) ret;
}

Handle dustKernelMemReAlloc(int size, Handle hOrigBlock) {
	void* ret = (HANDLE_UNKNOWN == hOrigBlock) ? malloc(size) : realloc(
			dustKernelMemGetBlock(hOrigBlock), size);
	return (Handle) ret;
}

void dustKernelMemRelease(Handle* phBlock) {
	void* pp = dustKernelMemGetBlock(*phBlock);
	if (pp) {
		free(pp);
		*phBlock = HANDLE_UNKNOWN;
	}
}

void* dustKernelMemGetBlock(Handle hBlock) {
	return (HANDLE_UNKNOWN == hBlock) ? 0 : (void*) hBlock;
}

void dustKernelMemFill(void* target, char fillValue, int count) {
	memset(target, fillValue, count);
}

void dustKernelMemAccessBlock(Handle hBlock, int offset, int size,
		void* target, DustAccOp access) {
	char* pp = dustKernelMemGetBlock(hBlock);
	char* p_from;
	char* p_to;

	switch (access) {
	case DUST_ACC_GET:
		p_from = pp+offset;
		p_to = target;
		break;
	case DUST_ACC_SET:
		p_from = target;
		p_to = pp+offset;
		break;
	default:
		break;
	}

	memcpy(p_to, p_from, size);
}

