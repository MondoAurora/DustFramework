/*
 ============================================================================
 Name        : test.c
 Author      : Lorand Kedves
 Version     :
 Copyright   : MondoAurora Foundation
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include <dust.h>
#include <kernel.h>
#include <boot.h>

#include <test.h>

#include <stdio.h>
#include <stdlib.h>

#include <string.h>

char* MSG = "This is my hello world message";

int main(void) {
	setvbuf(stdout, NULL, _IONBF, 0);
	setvbuf(stderr, NULL, _IONBF, 0);

	bootTraceMsg("starting");

	Handle b = dustKernelMemAlloc(100);

	char s[100];
	dustKernelMemAccessBlock(b, 0, testStrlen(MSG) + 1, MSG, DUST_ACC_SET);
	dustKernelMemAccessBlock(b, 0, testStrlen(MSG) + 1, s, DUST_ACC_GET);

	dustKernelMemRelease(&b);

	bootTraceMsg(s);

//	dustGetReferredEntity(0);

//	Handle hEntity = dustCreateEntity(0);

	bootTraceMsg("entity created");

	return EXIT_SUCCESS;
}


void bootTrace(Reference refUnit, Reference refMsg, const char* msg) {
	printf("trace - %d/%d ", (long) refUnit, (long) refMsg);
	printf(msg);
	printf("\n");

	fflush(stdout);
}

int testStrlen(const char* msg) {
	return strlen(msg);
}
