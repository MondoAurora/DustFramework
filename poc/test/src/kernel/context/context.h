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
	dustfnMsgProc pfnResponseProcessor;
} Context;

typedef struct {
	Handle hSourceEntity;
	Reference refLink;

	Handle hCollLinkedEntities;
} LinkInfo;


Context* ctxGetCurrentContext();

Reference ctxBuildChannelRef(Handle hTargetEntity, Reference refChannel);

DustBool ctxVerifyEntityHandle(Context* pCtx, Handle hEntity);
LinkInfo* ctxGetLinkInfo(Context* pCtx, Handle hTargetEntity, Reference refLink, DustCollOp op);


#endif /* CONTEXT_H_ */
