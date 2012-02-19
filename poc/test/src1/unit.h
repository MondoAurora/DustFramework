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

#endif /* UNIT_H_ */
