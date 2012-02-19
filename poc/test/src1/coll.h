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

	/** placeholder for the data */
	char content;
} DustColl;


#endif /* COLL_H_ */
