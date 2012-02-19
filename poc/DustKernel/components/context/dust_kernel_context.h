/*
 * dust_kernel_context.h
 *
 *  Created on: 2011.10.26.
 *      Author: lkedves
 */

#ifndef DUST_KERNEL_CONTEXT_H_
#define DUST_KERNEL_CONTEXT_H_

struct Context {
	IDENTIFIER mem_this;
	IDENTIFIER coll_entities;

	IDENTIFIER map_ref_entities;
};

#endif /* DUST_KERNEL_CONTEXT_H_ */
