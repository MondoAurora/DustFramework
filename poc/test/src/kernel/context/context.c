/*
 * context.c
 *
 *  Created on: 2011.11.24.
 *      Author: lkedves
 */

#include <dust.h>
#include <kernel.h>

#include "context.h"
#include <test.h>



// public functions

Handle dustGetReferredEntity(Reference refEntity) {
	Context *pCtx = ctxGetCurrentContext();

	Handle hEntity = dustKernelCollMapGet(pCtx->hCollRefEntities, refEntity);

	if ( HANDLE_UNKNOWN == hEntity ) {
		hEntity = dustKernelUnitGetReferredEntity(pCtx->hUnit, refEntity);
		dustKernelCollMapPut(pCtx->hCollRefEntities, refEntity, hEntity);
		dustKernelCollSetAdd(pCtx->hCollEntities, hEntity);
	}

	return hEntity;
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

	ctxVerifyEntityHandle(pCtx, hEntity);

	LinkInfo* pLinkInfo = ctxGetLinkInfo(hTargetEntity, refLink, op);

	dustKernelCollManage(pLinkInfo->hCollLinkedEntities, op, hLinkedEntity, index);
}

Handle dustGetChannel(Handle hTargetEntity, Reference refChannel, dustfnMsgProc pfnRespProc) {
	Context *pCtx = ctxGetCurrentContext();

	ctxVerifyEntityHandle(pCtx, hTargetEntity);

	Reference refChn = ctxBuildChannelRef(hTargetEntity, refChannel);

	Handle hChannel = dustKernelCollMapGet(pCtx->hCollRefChannels, refChn);

	if ( HANDLE_UNKNOWN == hChannel ) {
		hChannel = dustKernelUnitGetReferredChannel(pCtx->hUnit, hTargetEntity, refChannel);
		dustKernelCollMapPut(pCtx->hCollRefChannels, refChn, hChannel);
	}

	return hChannel;
}

Handle dustSend(Handle hChannel, Handle hDataEntity, Handle *phGroup) {
	testTraceMsg("dust_send_async");
	return -1;
}

void dustRespond(Handle hDataEntity) {
	Context *pCtx = ctxGetCurrentContext();

	testTraceMsg("dust_respond");
}


/*
 * Private context functions and data
 */

// local variables for context operation
Handle hMapContexts;



Context* ctxGetCurrentContext() {
	Handle hCurrCtx = dustKernelThreadGetContextHandle();

	Context *pCtx = (Context*) dustKernelCollGetBlock(hMapContexts, hCurrCtx);

	return pCtx;
}

void ctxVerifyEntityHandle(Context* pCtx, Handle hEntity) {
	if ( !dustKernelCollContains(pCtx->hCollEntities,  hEntity) ) {
		// should throw exception or whatever, no return...
	}
}

Context* cxtCreateContext(Handle hParentContext) {
	Handle ret = dustKernelMemAlloc(sizeof(Context));

	Context *pCtx = (Context*) dustKernelMemGetBlock(ret);

	return pCtx;
}

LinkInfo* ctxGetLinkInfo(Context* pCtx, Handle hTargetEntity, Reference refLink, DustCollOp op) {
	return 0;
}

Reference ctxBuildChannelRef(Handle hTargetEntity, Reference refChannel) {
	return REFERENCE_UNKNOWN;
}
