/*
 * mem.c
 *
 *  Created on: 2011.11.23.
 *      Author: lkedves
 */

#include <dust.h>
#include <kernel.h>

#include "unit.h"

Handle dustKernelUnitCreateEntity(Handle hUnit, Reference refType) {
	Unit *pUnit = (Unit*) dustKernelMemGetBlock(hUnit);

	int aspSize = sizeof(Aspect) + unitGetAspectSize(pUnit, refType);
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

Handle dustKernelUnitGetReferredEntity(Handle hUnit, Reference refEntity) {
	return HANDLE_UNKNOWN;
}

Handle dustKernelUnitGetReferredChannel(Handle hUnit, Reference refChannel) {
	return HANDLE_UNKNOWN;
}

void dustKernelUnitManageField(Handle hUnit, Handle hEntity, Reference refField, void* source, DustAccOp op) {

}

int unitGetAspectSize(Unit *pUnit, Reference refType) {
	return 30;
}
