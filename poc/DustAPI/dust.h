/*
 * dust.h
 *
 *  Created on: 2011.10.24.
 *      Author: lkedves
 */

#ifndef DUST_H_
#define DUST_H_

typedef long CONST_REFERENCE;	// this is a generated constant resolved by the Context REF array
typedef long VAR_INSTANCE;	// this is an "Object" index in the ENTITY array

#define DUST_ENTITY_THIS 0;
#define DUST_CHANNEL_RESPONSE 0;

//#ifndef bool
typedef enum {
	false = 0, true = 1
} dust_bool;
//#endif


typedef enum {
	GET, SET
} ACCESS_OP;

typedef enum {
	ADD, SET, INSERT, REMOVE, CLEAR
} LINK_OP;

typedef enum {
	FIRST, CONTINUING, FINISHED, ABORTED
} CHANNEL_OP;

/* get entity instance using a generated constant reference */
VAR_INSTANCE dust_get_referred_entity(CONST_REFERENCE ref_entity);
/* create an entity instance with the given primary type */
VAR_INSTANCE dust_create_entity(CONST_REFERENCE ref_type);

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

void dust_manage_field(VAR_INSTANCE entity, CONST_REFERENCE ref_field, void* source, ACCESS_OP op);

void dust_manage_link(VAR_INSTANCE host, CONST_REFERENCE ref_link, LINK_OP op, VAR_INSTANCE target, int index);

VAR_INSTANCE dust_get_channel(VAR_INSTANCE entity, CONST_REFERENCE ref_channel, dustfn_process_message response_processor);

void dust_send_wait(VAR_INSTANCE channel, VAR_INSTANCE entity);
long dust_send_async(VAR_INSTANCE channel, VAR_INSTANCE entity, long *p_group_id);
void dust_wait(long *p_process_or_group_id);

void dust_respond(VAR_INSTANCE entity);

/* This is your entry point. When the source runs in cycle, you get the index of the item
 * as it has entered the channel. This is interesting when you process a linked array of entities;
 * and can be used to restore the sending order when the items may get shuffled along
 * the channel (like datagram networking). However, if the receiving channel supports
 * multiple senders, the indexes may be repeated or mixed. */
typedef void (*dustfn_process_message)(CHANNEL_OP channel_op, VAR_INSTANCE entity, int index);

// TODO perhaps a macro would be fine to declare the channel processor function,
// and another for the internal, implementation related channel processors?
#endif /* DUST_H_ */
