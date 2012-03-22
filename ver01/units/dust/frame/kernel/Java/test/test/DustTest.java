package test;

import dust.frame.generic.IText;
import dust.frame.kernel.DustTestKernelDumper;
import dust.frame.stream.IStreamWrite;
import dust.kernel.DustKernel;
import dust.shared.DustConsts;
import dust.shared.DustObject;

public class DustTest implements DustConsts {

	/**
	 * @param args
	 */
	public static void test(String[] args) {
		System.out.println("Here I am!");

//		testDump();

		// testJSONExport();

		System.out.println("\n------------");
		
		testDumpEntity();

		// testJSONImport();

		// testImport();
	}

	static DustObject getAnObject() {
		DustIdentifier id = DustKernel.Environment.getId("[dust.frame.kernel.Vendor]:dust");
		return DustKernel.Environment.getInstance(id);
	}
	
	static void testDump(DustTestKernelDumper d) {
		DustObject conn = getAnObject();

//		DustStream.Out out = new DustTestStreamOut();

		d.dumpObject(conn);
		System.out.println("\n------------");
		d.dumpObject(conn);
	}

	static void testDumpEntity() {
		DustIdentifier id = DustKernel.Environment.getId("[dust.frame.stream.StreamWrite]:dump");
		IStreamWrite dump = (IStreamWrite) DustKernel.Environment.getInstance(id);
		
		IText txt = new IText() {

			@Override
			public void setText(String text) {

			}

			@Override
			public String getText() {
				return "Hello, world!";
			}
			
			@Override
			public void send(Enum<?> msgId, DustObject msgOb, boolean wait, ResponseProcessor respProc) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public DustObject getNeighbor(DustIdentifier typeId) {
				// TODO Auto-generated method stub
				return null;
			}
		};

		dump.send(IStreamWrite.Messages.write, txt, true, null);
		dump.send(IStreamWrite.Messages.endLine, null, true, null);
		
		testDump(new DustTestKernelDumper(id));
	}
}
