/*
 * context.h
 *
 *  Created on: 2011.11.24.
 *      Author: lkedves
 */

#ifndef CONTEXT_H_
#define CONTEXT_H_


typedef struct {
	Reference refType;
	struct Aspect *pNextAspect;
} Aspect;

typedef struct {
	Handle id;
	Handle hAspect;
} Entity;



typedef struct {
	KernelContext kernelCtx;

	Handle hCollEntities;
	Handle hCollAspects;
} Context;



#endif /* CONTEXT_H_ */
