/*
 * unit.h
 *
 *  Created on: 2011.11.24.
 *      Author: lkedves
 */

#ifndef UNIT_H_
#define UNIT_H_

/*****************
 *
 * core: the generic field type definitions, like integer, boolean, etc
 *
 *****************/

typedef struct {
	char *id;
	int dataSize;
} FieldType;

/*****************
 *
 * Unit content (field, type, channelDef, entity reference
 *
 *****************/

typedef struct {
	char *id;

	int offset;

	FieldType *pFieldType;
	void *pType;
} Field;

typedef struct {
	Reference refType;
	DustBool async;
} ChannelDef;

typedef struct {
	char *id;

	int size;

	int fieldCount;
	Field *pFields;

	int channelCount;
	ChannelDef *pChannels;
} Type;

typedef enum {
	EREFTYPE_INSTANCE, EREFTYPE_PATH
} EntityRefType;

typedef struct {
	EntityRefType refType;
	union {
		char *id; // invocation identifier
		Handle hEntity; // it is already created and stored in the context
	};
} EntityInvoker;

/*****************
 *
 * Unit instance data
 *
 *****************/

typedef enum {
	COMPTYPE_FIELD, COMPTYPE_TYPE, COMPTYPE_CHANNEL, COMPTYPE_ENTITY
} UnitComponentRefType;

typedef struct {
	int idOffset; // this is where the id can be found

	UnitComponentRefType refType;

	union {
		Field *pField;
		Type *pType;
		ChannelDef *pChannel;
		EntityInvoker *pEntityInvoker;
	};

} ComponentRef;

typedef struct {
	// identification: Vendor, Domain, Unit ID

	char *pIdPool;
	int idPoolSize;

} UnitDefinition;

typedef struct {
	UnitDefinition def;

	int typeCount;
	Type *pTypes;

	int entityCount;
	EntityInvoker *pEntityInvokers;

	int compRefCount;
	ComponentRef *pCompRefs;

	int unitRefCount;
	UnitDefinition *pUDefRefs;
} UnitDeclaration;

/*****************
 *
 * Dynamic content: Aspect, entity, channel instances
 *
 *****************/

typedef struct {
	Type *pType;
	Handle hNextAspect;
} Aspect;

typedef struct {
	ChannelDef *pDef;
// other work data
} Channel;

typedef struct {
	Handle id;
	Handle hAspectChain;
} Entity;


UnitDeclaration* unitGetUnitDecl(Handle hUnit);

ComponentRef* unitGetRef(UnitDeclaration *pUnitDecl, Reference refType, UnitComponentRefType assertType);

Aspect* unitAddAspect(Type* pType, Handle *pHandle);
Handle unitRemoveAspect(Aspect *pPrevAsp, Handle hAspect, Handle hNextAspect);

#endif /* UNIT_H_ */
