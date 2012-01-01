/*
 * context.c
 *
 *  Created on: 2011.11.24.
 *      Author: lkedves
 */

#include <dust.h>
#include <kernel.h>

#include "context.h"
#include <boot.h>



// public functions

Handle dustGetReferredEntity(Reference refEntity) {
	FactoryCtxGetRefEntity ctx = { ctxGetCurrentContext(), refEntity };
	return dustKernelCollLazyMapGet(ctx.pCtx->hCollRefEntities, refEntity, &ctx);
}

Handle dustCreateEntity(Reference refType) {
	Context *pCtx = ctxGetCurrentContext();

	Handle hEntity = dustKernelUnitCreateEntity(pCtx->hUnit, refType);

	dustKernelCollSetAdd(pCtx->hCollEntities, hEntity);

	return hEntity;
}

void dustManageField(Handle hEntity, Reference refField, void* source, DustAccOp op) {
	Context *pCtx = ctxGetCurrentContext();

	ctxVerifyEntityHandle(pCtx, hEntity);

	dustKernelUnitManageField(pCtx->hUnit, hEntity, refField, source, op);
}

void dustManageLink(Handle hTargetEntity, Reference refLink, DustCollOp op, Handle hLinkedEntity, int index) {
	Context *pCtx = ctxGetCurrentContext();

	ctxVerifyEntityHandle(pCtx, hTargetEntity);

	LinkInfo* pLinkInfo = ctxGetLinkInfo(pCtx, hTargetEntity, refLink, op);

	dustKernelCollManage(pLinkInfo->hCollLinkedEntities, op, hLinkedEntity, index);
}

Handle dustGetChannel(Handle hTargetEntity, Reference refChannel, dustfnMsgProc pfnRespProc) {
	Context *pCtx = ctxGetCurrentContext();

	ctxVerifyEntityHandle(pCtx, hTargetEntity);

	Reference refChn = ctxBuildChannelRef(hTargetEntity, refChannel);

	Handle hChannel = dustKernelCollMapGet(pCtx->hCollRefChannels, refChn);

	if ( HANDLE_UNKNOWN == hChannel ) {
		hChannel = dustKernelUnitGetReferredChannel(pCtx->hUnit, refChannel);
		dustKernelCollMapPut(pCtx->hCollRefChannels, refChn, hChannel);
	}

	return hChannel;
}

Handle dustSend(Handle hChannel, Handle hDataEntity, Handle *phGroup) {
	Context *pCtx = ctxGetCurrentContext();

	ctxVerifyEntityHandle(pCtx, hDataEntity);

	Handle hNewCtx = ctxCreateContext(dustKernelThreadGetContextHandle(), 0);

	return dustKernelUnitSend(pCtx->hUnit, hChannel, hDataEntity, phGroup, hNewCtx);
}

void dustRespond(Handle hDataEntity) {
	Context *pCtx = ctxGetCurrentContext();

	ctxVerifyEntityHandle(pCtx, hDataEntity);

	dustKernelUnitSend(pCtx->hUnit, pCtx->hCallerChn, hDataEntity, HANDLE_CHN_RESPONSE, pCtx->hCallerCtx);
}

DustBool dustReleaseEntity(Handle hEntity) {
	bootTraceCall("dustReleaseEntity called");

	return DUST_TRUE;
}

void dustTransact(DustTransOp transOp) {
	bootTraceCall("dustTransact called");
}





/*
 * Private context functions and data
 */

// local variables for context operation
Handle hMapContexts;

int defRefCount = 10;


Context* ctxGetCurrentContext() {
	Handle hCurrCtx = dustKernelThreadGetContextHandle();

	Context *pCtx = (Context*) dustKernelCollGetBlock(hMapContexts, hCurrCtx);

	return pCtx;
}

void ctxVerifyEntityHandle(Context* pCtx, Handle hEntity) {
	if ( !dustKernelCollContains(pCtx->hCollEntities,  hEntity) ) {
		// should throw exception or whatever, no return...
	}

	bootTraceCall("ctxVerifyEntityHandle");
}

Handle ctxCreateContext(Handle hParentContext, void* otherData) {
	Handle ret = dustKernelMemAlloc(sizeof(Context));

	Context *pCtx = (Context*) dustKernelMemGetBlock(ret);

	pCtx->hCollRefEntities = dustKernelCollLazyMapCreate(defRefCount, ctxFactoryRefEntities);

	return ret;
}

LinkInfo* ctxGetLinkInfo(Context* pCtx, Handle hTargetEntity, Reference refLink, DustCollOp op) {
	return 0;
}

Reference ctxBuildChannelRef(Handle hTargetEntity, Reference refChannel) {
	return REFERENCE_UNKNOWN;
}


Handle ctxFactoryRefEntities(Reference refKey, void *pCtx) {
	FactoryCtxGetRefEntity *pFactCtx = (FactoryCtxGetRefEntity *) pCtx;

	Handle hEntity = dustKernelUnitGetReferredEntity(pFactCtx->pCtx->hUnit, pFactCtx->refEntity);
	dustKernelCollSetAdd(pFactCtx->pCtx->hCollEntities, hEntity);

	return hEntity;
}
