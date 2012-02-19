/*
 * dust_kernel_entity.h
 *
 *  Created on: 2011.10.25.
 *      Author: lkedves
 */

#ifndef DUST_KERNEL_ENTITY_H_
#define DUST_KERNEL_ENTITY_H_

/* This is a 'global' collection of types; but instead of the type descriptor
 * entities, these contents are actual type declaration structures that can
 * be used directly (not through dust api functions), and have the current
 * Island related information added (data sizes, offsets, C arrays, etc.) */
IDENTIFIER coll_types;

/* The Module structure can resolve identifiers coming from a binary. When
 * loading it, it should contain compiled information block (generated source)
 * that contains the list of references in exactly the same order as the
 * identifier constants were generated. This is then resolved against the
 * current content of the Island (like Type entities, Field info, etc.)
 * In the end, this Module structure holds all the information for quick,
 * access using those identifiers as simple field indexes. */
typedef struct Module {
	IDENTIFIER coll_type_refs;
};

typedef struct Entity {
	IDENTIFIER mem_this;
	IDENTIFIER coll_aspects;
};


typedef struct Aspect {
	IDENTIFIER mem_data;
} Aspect;

typedef struct Field {
	int offset;
	int size;
} Field;

Field* entity_get_field_def(Entity *p_entity, IDENTIFIER field);
Aspect* entity_get_aspect_data(Entity *p_entity, IDENTIFIER field);

#endif /* DUST_KERNEL_ENTITY_H_ */
