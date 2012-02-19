/*
 * monokernel.c
 *
 *  Created on: 2011.11.21.
 *      Author: lkedves
 */

#include <stdio.h>

#include "dust.h"
#include "kernel.h"

#include "test.h"

Handle HANDLE_NULL = -1;


Handle dustGetReferredEntity(Reference refEntity) {
	testTraceMsg("dust_get_referred_entity");
	return -1;
}

Handle dustCreateEntity(Reference refType) {
	KernelContext *pCtx = dustKernelThreadGetContext();
	return dustKernelContextCreateEntity(pCtx, refType);
}

void dustManageField(Handle hEntity, Reference refField, void* source, AccessOp op) {
	testTraceMsg("dust_manage_field");
}

void dustManageLink(Handle hTargetEntity, Reference refLink, CollOp op, Handle hLinkedEntity, int index) {
	testTraceMsg("dust_manage_link");
}

Handle dustGetChannel(Handle hTargetEntity, Reference refChannel, dustfnMsgProc pfnRespProc) {
	testTraceMsg("dust_get_channel");
	return -1;
}

Handle dustSend(Handle hChannel, Handle hDataEntity, Handle *phGroup) {
	testTraceMsg("dust_send_async");
	return -1;
}

void dustWait(Handle hProcOrGroup) {
	testTraceMsg("dust_wait");
}

void dustRespond(Handle hDataEntity) {
	testTraceMsg("dust_respond");
}
