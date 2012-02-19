/*
 * dust_kernel.h
 *
 *  Created on: 2011.10.25.
 *      Author: lkedves
 */

#ifndef DUST_KERNEL_H_
#define DUST_KERNEL_H_

#include <dust.h>

#define KERNEL_ENTITYREF_CONTEXT 1

typedef struct Context Context;
typedef struct Entity Entity;
typedef struct Deployment Deployment;


typedef long MEMBLOCK;	// An actual memory block

/*
 * Forward definitions of kernel internal API:
 * the interface that different kernel components provide to each other
 */
MEMBLOCK dust_thread_get_context_block();

void* dust_kernel_mem_get_block(MEMBLOCK block_id);
void dust_kernel_mem_access_block(MEMBLOCK block_id, int offset, int size, void* target, ACCESS_OP access);

void* dust_kernel_coll_get_block(MEMBLOCK coll_block_id, VAR_INSTANCE member_id);

void dust_kernel_entity_field_access(Entity *p_entity, CONST_REFERENCE field, void* target, ACCESS_OP access);

Context* dust_kernel_ctx_get_context();
VAR_INSTANCE dust_kernel_ctx_get_referred_entity(Context *p_context, CONST_REFERENCE entity_ref);
Entity* dust_kernel_ctx_get_entity_ptr(VAR_INSTANCE entity);

void dust_kernel_ctx_entity_lock(Entity *p_entity);

MEMBLOCK dust_kernel_coll_get_item(MEMBLOCK collection, VAR_INSTANCE key);
VAR_INSTANCE dust_kernel_coll_add_item(MEMBLOCK collection, MEMBLOCK item);

MEMBLOCK dust_deployment_get_referred_entity(CONST_REFERENCE id);

#endif /* DUST_KERNEL_H_ */
