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
	UnitDeclaration *pUnit = unitGetUnitDecl(hUnit);
	Type* pType = unitGetRef(pUnit, refType, COMPTYPE_TYPE)->pType;

	Handle hEntity = dustKernelMemAlloc(sizeof(Entity));
	Entity *pEntity = (Entity*) dustKernelMemGetBlock(hEntity);

	pEntity->id = hEntity;

	unitAddAspect(pType, &(pEntity->hAspectChain));

	return hEntity;
}

Handle dustKernelUnitGetReferredEntity(Handle hUnit, Reference refEntity) {
	UnitDeclaration *pUnit = unitGetUnitDecl(hUnit);
	EntityInvoker* pRefEntity = unitGetRef(pUnit, refEntity, COMPTYPE_ENTITY)->pEntityInvoker;

	if (EREFTYPE_INSTANCE == pRefEntity->refType) {
		return pRefEntity->hEntity;
	} else {
		// request the entity handle from the context
	}

	return HANDLE_UNKNOWN;
}

Handle dustKernelUnitGetReferredChannel(Handle hUnit, Reference refChannel) {
	UnitDeclaration *pUnit = unitGetUnitDecl(hUnit);
	ChannelDef* pChnDef = unitGetRef(pUnit, refChannel, COMPTYPE_CHANNEL)->pChannel;

	Handle hChannel = dustKernelMemAlloc(sizeof(Channel));

	Channel *pChannel = (Channel*) dustKernelMemGetBlock(hChannel);
	pChannel->pDef = pChnDef;

	return hChannel;
}

DustBool dustKernelUnitManageAspect(Handle hUnit, Handle hEntity, Reference *pRefTypes, int count, DustAspectOp op) {
	UnitDeclaration *pUnit = unitGetUnitDecl(hUnit);
	Type * pTypes[count];
	DustBool ret = DUST_FALSE;

	int i;

	for (i = 0; i < count; ++i) {
		pTypes[i] = unitGetRef(pUnit, pRefTypes[i], COMPTYPE_TYPE)->pType;
	}

	hEntity = dustKernelCtxLockEntity(hEntity);

	Entity *pEntity = (Entity*) dustKernelMemGetBlock(hEntity);
	Handle hAspect;
	Aspect *pPrevAsp = 0;
	Aspect *pAspect = 0;

	for (hAspect = pEntity->hAspectChain; HANDLE_UNKNOWN != hAspect; ) {
		pAspect = (Aspect*) dustKernelMemGetBlock(hAspect);
		hAspect = HANDLE_UNKNOWN;

		for (i = 0; i < count; ++i) {
			if (pAspect->pType == pTypes[i]) {
				if (DUST_ASP_REMOVE == op) {
					hAspect = unitRemoveAspect(pPrevAsp, hAspect, pAspect->hNextAspect);
					ret = DUST_TRUE;
					break;
				} else {
					pTypes[i] = 0;
				}
			}
		}

		if (HANDLE_UNKNOWN == hAspect) {
			pPrevAsp = pAspect;
			hAspect = pAspect->hNextAspect;
		}
	}

	if (DUST_ASP_ADD == op) {
		for (i = 0; i < count; ++i) {
			if (pTypes[i]) {
				pPrevAsp = unitAddAspect(pTypes[i], &(pPrevAsp->hNextAspect));
				ret = DUST_TRUE;
			}
		}
	}

	return ret;
}

DustBool dustKernelUnitManageField(Handle hUnit, Handle hEntity, Reference refField, void* pExtMem, DustAccOp op) {
	UnitDeclaration *pUnit = unitGetUnitDecl(hUnit);
	Field* pField = unitGetRef(pUnit, refField, COMPTYPE_FIELD)->pField;
	Type* pType = pField->pType;

	DustBool ret = DUST_TRUE;

	hEntity = dustKernelCtxLockEntity(hEntity);

	Entity *pEntity = (Entity*) dustKernelMemGetBlock(hEntity);
	Handle hAspect;
	Aspect *pAspect;

	for (hAspect = pEntity->hAspectChain; hAspect; hAspect = pAspect->hNextAspect) {
		pAspect = (Aspect*) dustKernelMemGetBlock(hAspect);
		if (pAspect->pType == pType) {
			break;
		}
	}

	if (!hAspect) {
		if ( DUST_ACC_GET == op ) {
			dustKernelMemFill(pExtMem, 0, pField->pFieldType->dataSize); // someone will not check the return value...
			return DUST_FALSE;
		} else {
			Aspect *pLastAspect = pAspect;
			pAspect = unitAddAspect(pType, &hAspect);
			pLastAspect->hNextAspect = hAspect;
		}
	}

	dustKernelMemAccessBlock(hAspect, sizeof(Aspect) + pField->offset, pField->pFieldType->dataSize, pExtMem, op);

	return ret;
}

Handle dustKernelUnitSend(Handle hUnit, Handle hChannel, Handle hDataEntity, Handle *phGroup, Handle hNewCtx) {
	return HANDLE_UNKNOWN;
}

UnitDeclaration* unitGetUnitDecl(Handle hUnit) {
	return (UnitDeclaration*) dustKernelMemGetBlock(hUnit);
}

ComponentRef* unitGetRef(UnitDeclaration *pUnitDecl, Reference refType, UnitComponentRefType assertType) {
	ComponentRef* pRef = &(pUnitDecl->pCompRefs[refType]);

	if (pRef->refType == assertType) {
		return pRef;
	} else {
		dustThrow(HANDLE_UNKNOWN);
		return 0;
	}
}

Handle unitRemoveAspect(Aspect *pPrevAsp, Handle hAspect, Handle hNextAspect) {
	pPrevAsp->hNextAspect = hNextAspect;

	dustKernelMemRelease(&hAspect);

	return hNextAspect;
}

Aspect * unitAddAspect(Type* pType, Handle *pHandle) {
	int aspSize = sizeof(Aspect) + pType->size;
	Handle hAspect = dustKernelMemAlloc(sizeof(aspSize));

	Aspect *pAspect = (Aspect*) dustKernelMemGetBlock(hAspect);
	pAspect->pType = pType;
	pAspect->hNextAspect = HANDLE_UNKNOWN;

	*pHandle = hAspect;

	return pAspect;
}
