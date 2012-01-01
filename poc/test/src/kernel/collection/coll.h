/*
 * coll.h
 *
 *  Created on: 2011.11.24.
 *      Author: lkedves
 */

#ifndef COLL_H_
#define COLL_H_

typedef struct {
	/** number of stored items */
	int itemSize;
	/** allocated size */
	int capacity;

	/** factory function for lazy maps **/
	dustfnLazyMapFactory pfnFactory;

	/** placeholder for the data */
	char content;
} DustColl;



DustColl* collGetFromHandle(Handle hColl);

Handle collCreate(int itemSize, int initCount, dustfnLazyMapFactory pfnFactory);
void collMapPut(DustColl *pColl, Reference refKey, Handle value);
Handle collMapGet(DustColl *pColl, Reference refKey, void *pCtx);

#endif /* COLL_H_ */
