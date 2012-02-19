/*
 * dust_kernel_entity.c
 *
 *  Created on: 2011.10.25.
 *      Author: lkedves
 */

#include <dust.h>

#include <dust_kernel.h>

#include <dust_kernel_entity.h>



void dust_kernel_entity_field_access(Entity *p_entity, IDENTIFIER field, void* target, ACCESS_OP access) {
	Field *p_field = entity_get_field_def(p_entity, field);
	Aspect *p_aspect = entity_get_aspect_data(p_entity, field);
	if ( ACCESS_OP.SET == access ) {
		dust_kernel_ctx_entity_lock(p_entity);
	}
	dust_kernel_mem_access_block(p_aspect->mem_data, p_field->offset, p_field->size, target, access);
}
