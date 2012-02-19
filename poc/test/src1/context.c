/*
 * context.c
 *
 *  Created on: 2011.11.24.
 *      Author: lkedves
 */

#include "dust.h"
#include "kernel.h"

#include "context.h"

KernelContext* dustKernelContextCreateContext(Handle hParentContext) {
	Handle ret = dustKernelMemAlloc(sizeof(Context));

	Context *pCtx = (Context*) dustKernelMemGetBlock(ret);

	// init pCtx;

	return (KernelContext*) pCtx;

}

Handle dustKernelContextCreateEntity(KernelContext* pCtx_, Reference refType) {
//	Context *pCtx = (Context*) pCtx_;

	int aspSize = sizeof(Aspect) + dustKernelUnitGetTypeSize(pCtx_, refType);
	Handle hAspect = dustKernelMemAlloc(sizeof(aspSize));

	Aspect *pAspect = (Aspect*) dustKernelMemGetBlock(hAspect);
	pAspect->refType = refType;
	pAspect->pNextAspect = 0;


	Handle hEntity = dustKernelMemAlloc(sizeof(Entity));
	Entity *pEntity = (Entity*) dustKernelMemGetBlock(hEntity);

	pEntity->id = hEntity;
	pEntity->hAspect = hAspect;

	return hEntity;
}
