/*
 * dust_kernel.c
 *
 *  Created on: 2011.10.25.
 *      Author: lkedves
 *
 *  This source resolves all the public API functions, using the "kernel public"
 *  interface of the other kernel components (memory, thread, collection, context, ...)
 *  There can be utility functions and macros created here for these wrapper functions,
 *  but nothing else.
 */

#include <dust.h>

#include <dust_kernel.h>

#define LOAD_CONTEXT Context *p_ctx = dust_kernel_ctx_get_context();


void dust_manage_field(VAR_INSTANCE entity, CONST_REFERENCE ref_field, void* source, ACCESS_OP op) {
	LOAD_CONTEXT;

	Entity *p_entity = (Entity*) dust_kernel_coll_get_block(p_ctx->coll_entities, entity);

	dust_kernel_entity_field_access(p_entity, field, target, op);
}

VAR_INSTANCE dust_get_referred_entity(CONST_REFERENCE ref_entity) {
	LOAD_CONTEXT;
	return dust_kernel_ctx_get_referred_entity(p_ctx, entity_ref);
}

VAR_INSTANCE dust_create_entity(CONST_REFERENCE ref_type);

void dust_manage_link(VAR_INSTANCE host, CONST_REFERENCE ref_link, LINK_OP op, VAR_INSTANCE target, int index);

VAR_INSTANCE dust_get_channel(VAR_INSTANCE entity, CONST_REFERENCE ref_channel, dustfn_process_message response_processor);

void dust_send_wait(VAR_INSTANCE channel, VAR_INSTANCE entity);
long dust_send_async(VAR_INSTANCE channel, VAR_INSTANCE entity, long *p_group_id);
void dust_wait(long *p_process_or_group_id);
