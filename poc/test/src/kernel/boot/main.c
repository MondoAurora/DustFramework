
#include <dust.h>

#include <kernel.h>
#include <kernel_boot.h>


int main(void) {
	int ret;

	bootTraceMsg("dust starting");

	ret = dustGenInit();

	bootTraceMsg("dust started");

	return ret;
}
