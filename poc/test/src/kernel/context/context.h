/*
 * context.h
 *
 *  Created on: 2011.11.24.
 *      Author: lkedves
 */

#ifndef CONTEXT_H_
#define CONTEXT_H_


typedef struct {
	Handle hMemMgr;
	Handle hTypeMgr;
	Handle hUnit;

	Handle hCollEntities;
	Handle hCollLinks;

	Handle hCollRefEntities;
	Handle hCollRefChannels;

	Handle hCallerCtx;
	Handle hCallerChn;
	dustfnMsgProc pfnResponseProcessor;
} Context;

typedef struct {
	Handle hSourceEntity;
	Reference refLink;

	Handle hCollLinkedEntities;
} LinkInfo;

typedef struct {
	Context *pCtx;
	Reference refEntity;
} FactoryCtxGetRefEntity;


Context* ctxGetCurrentContext();

Reference ctxBuildChannelRef(Handle hTargetEntity, Reference refChannel);

void ctxVerifyEntityHandle(Context* pCtx, Handle hEntity);
LinkInfo* ctxGetLinkInfo(Context* pCtx, Handle hTargetEntity, Reference refLink, DustCollOp op);

Handle ctxCreateContext(Handle hParentContext, Handle hUnit, void* otherData);

Handle ctxFactoryRefEntities(Reference refKey, void *pCtx);

#endif /* CONTEXT_H_ */
