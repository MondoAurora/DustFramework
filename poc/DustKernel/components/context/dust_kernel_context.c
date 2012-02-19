/*
 * dust_kernel_context.c
 *
 *  Created on: 2011.10.26.
 *      Author: lkedves
 */

#include <dust.h>

#include <dust_kernel.h>

#include <dust_kernel_context.h>

Context* dust_kernel_ctx_get_context() {
	return (Context*) dust_kernel_mem_get_block(dust_thread_get_context_block());
}

ENTITY dust_kernel_ctx_get_referred_entity(Context *p_context, IDENTIFIER entity_ref) {
	ENTITY e = dust_kernel_coll_get_item(p_context->map_ref_entities, entity_ref);

	if ( !e ) {
		e = dust_deployment_get_referred_entity(entity_ref);
		dust_kernel_coll_add_item(p_context->map_ref_entities, entity_ref);
	}

	return e;
}

Entity* dust_kernel_ctx_get_entity_ptr(Context *p_context, ENTITY entity) {
	// the entity MUST BE a valid identifier in the local context instance map,
	// the caller should have accessed it beforehand by using another ctx function
	return (Entity*) dust_kernel_coll_get_block(p_ctx->coll_entities, entity);
}
