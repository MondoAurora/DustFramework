package org.mondoaurora.frame.kernel;

public interface MAFKernelConsts {
	
	enum FieldType {
		IDENTIFIER, BOOLEAN, VALUESET, INTEGER, DOUBLE, STRING, DATE, CONNECTOR, SET, ARRAY
	};

	char SEP_PATH_SEP = '.';
	char SEP_LOCAL_SEP = ':';
	char SEP_TYPE_START = '[';
	char SEP_TYPE_END = ']';

	char SEP_VALSET = '|';

	String ID_VENDOR_ROOT = "mondoaurora";
	String ID_DOMAIN_FRAME = "frame";
	String ID_UNIT_KERNEL = "kernel";
	
	String ID_TYPENAME_TYPE = "Type";
	String ID_TYPENAME_FIELD = "Field";
	String ID_TYPENAME_UNIT = "Unit";
	String ID_TYPENAME_DOMAIN = "Domain";
	String ID_TYPENAME_VENDOR = "Vendor";

	String ID_TYPENAME_IDENTIFIED = "Identified";

//	String[] KERNEL_PATH = new String[] {ID_VENDOR_ROOT, ID_DOMAIN_FRAME, ID_UNIT_KERNEL};
	String FRAME_PATH = MAFKernelIdentifier.buildPath(new String[] {ID_VENDOR_ROOT, ID_DOMAIN_FRAME});
	String KERNEL_PATH = MAFKernelIdentifier.buildPath(new String[] {FRAME_PATH, ID_UNIT_KERNEL});

	
	String FIELD_ID = "id";
	String FIELD_NAME = "name";
		
	String FIELD_TYPE = "type";
	String FIELD_LENGTH = "length";
	String FIELD_OBTYPE = "obType";
	String FIELD_TYPEID = "typeId";
	String FIELD_VALUES = "values";

	String FIELD_REFERRABLE = "referrable";
	String FIELD_FIELDS = "fields";

	String FIELD_TYPES = "types";
	
	String FIELD_DOMAINS = "domains";
	
	String FIELD_UNITS = "units";
	
	

	
	int ID_LENGTH = 30;
	int LONG_LENGTH = 200;


}
