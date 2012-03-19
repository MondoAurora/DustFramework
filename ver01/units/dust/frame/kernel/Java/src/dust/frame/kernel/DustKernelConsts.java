package dust.frame.kernel;

import dust.kernel.DustKernel;

public interface DustKernelConsts extends DustKernel {
	
	enum EntityState {
		NEW, NORMAL, DIRTY
	};
		
	char SEP_PATH_SEP = '.';
	char SEP_LOCAL_SEP = ':';
	char SEP_TYPE_START = '[';
	char SEP_TYPE_END = ']';

	char SEP_VALSET = ',';

	String ID_VENDOR_ROOT = "dust";
	String ID_DOMAIN_FRAME = "frame";
	String ID_UNIT_KERNEL = "kernel";
	String ID_TYPE_TYPE = "Type";
	
	String FRAME_PATH = DustKernelIdentifier.buildPath(new String[] {ID_VENDOR_ROOT, ID_DOMAIN_FRAME});
	String KERNEL_PATH = DustKernelIdentifier.buildPath(new String[] {FRAME_PATH, ID_UNIT_KERNEL});
	
	int ID_LENGTH = 30;
	int LONG_LENGTH = 200;
}
