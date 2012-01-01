/*
 * unit.h
 *
 *  Created on: 2011.11.24.
 *      Author: lkedves
 */

#ifndef UNIT_H_
#define UNIT_H_

typedef struct {
	char *id;
	int dataSize;
} FieldType;

typedef struct {
	char *id;

	int offset;

	FieldType *pFieldType;
	struct Type *pType;
} Field;

typedef struct {
	char *id;

	int fieldCount;
	int size;
	Field *pFields;
} Type;

typedef struct {
	Reference refType;
	DustBool async;
} Channel;

typedef enum {
	COMPTYPE_FIELD, COMPTYPE_TYPE, COMPTYPE_CHANNEL
} UnitComponentRefType;

typedef struct {
	int idOffset; // this is where the id can be found

	UnitComponentRefType refType;
	union {
		Field *pField;
		Type *pType;
		Channel *pChannel;
	};
} ElementRef;

typedef struct {
	// identification: Vendor, Domain, Unit ID

	char *pIdPool;
	int id_pool_size;

} UnitDefinition;

typedef struct {
	UnitDefinition def;

	int typeCount;
	Type *pTypes;

	int fieldCount;
	Field *pFields;

	int extTypeCount;
	Type **ppExtTypes;

	int extFieldCount;
	Field **ppExtFields;

	Handle hUnitEntity;
} UnitDeclaration;

typedef struct {
	char *p_id_pool;
	int id_pool_size;

	int typeCount;
	Type *pTypes;

	int fieldCount;
	Field *pFields;

	int extTypeCount;
	Type **ppExtTypes;

	int extFieldCount;
	Field **ppExtFields;

	Handle hUnitEntity;
} Unit;

typedef struct {
	Reference refType;
	struct Aspect *pNextAspect;
} Aspect;

typedef struct {
	Handle id;
	Handle hAspect;
} Entity;


int unitGetAspectSize(Unit *pUnit, Reference refType);

#endif /* UNIT_H_ */
