/*
 * dust.h
 *
 *  Created on: 2011.10.24.
 *      Author: lkedves
 */

#ifndef DUST_H_
#define DUST_H_

typedef long Handle;
typedef long Reference;	// this is a generated constant resolved by the Context REF array

#define HANDLE_UNKNOWN -1
#define HANDLE_THIS 0

#define REFERENCE_UNKNOWN -1

typedef enum {
	DUST_FALSE = 0, DUST_TRUE = 1
} DustBool;


typedef enum {
	DUST_ACC_GET, DUST_ACC_SET
} DustAccOp;

typedef enum {
	DUST_COLL_ADD, DUST_COLL_SET, DUST_COLL_INSERT, DUST_COLL_REMOVE, DUST_COLL_CLEAR
} DustCollOp;

typedef enum {
	DUST_CHN_FIRST, DUST_CHN_CONTINUING, DUST_CHN_FINISHED, DUST_CHN_ABORTED
} DustChnOp;

/* This is your entry point. When the source runs in cycle, you get the index of the item
 * as it has entered the channel. This is interesting when you process a linked array of entities;
 * and can be used to restore the sending order when the items may get shuffled along
 * the channel (like datagram networking). However, if the receiving channel supports
 * multiple senders, the indexes may be repeated or mixed. */
typedef void (*dustfnMsgProc)(DustChnOp channelOp, Handle entity, int index);


/* get entity instance using a generated constant reference */
Handle dustGetReferredEntity(Reference refEntity);
/* create an entity instance with the given primary type */
Handle dustCreateEntity(Reference refType);

/*
 * Probably this is also a private function inside the Entity component,
 * and the existence of Poid should stay in the kernel, not in a user-level
 * code.
 */
// typedef struct Poid Poid;
// ENTITY dust_get_entity_identified(Poid *p_poid);

/*
 * This is NOT a kernel service. A message should be sent to the Context
 * directly with the filter conditions, that should run the according actions
 * and send the found items back to the caller. This "find" otherwise should
 * have the same sync/async version and behind it the same "send" operation.
 * That is not good.
 */
// void dust_find_entity(ENTITY filter, dustfn_process_message response_processor);

void dustManageField(Handle hEntity, Reference refField, void* source, DustAccOp op);

void dustManageLink(Handle hTargetEntity, Reference refLink, DustCollOp op, Handle hLinkedEntity, int index);

Handle dustGetChannel(Handle hTargetEntity, Reference refChannel, dustfnMsgProc pfnRespProc);

Handle dustSend(Handle hChannel, Handle hDataEntity, Handle *phGroup);
void dustWait(Handle hProcOrGroup);

void dustRespond(Handle hDataEntity);


// TODO perhaps a macro would be fine to declare the channel processor function,
// and another for the internal, implementation related channel processors?
#endif /* DUST_H_ */
