package dust.shared;

import java.util.Date;

import dust.shared.DustLogic.Return;
import dust.shared.DustLogic.ReturnType;

public interface DustConsts {
	
	public static class DustDate {
		protected Date date;
		
		public DustDate(Date date_) {
			this.date = new Date(date_.getTime());
		}

		public Date getDate(Date into) {
			if ( null == into ) {
				return new Date(date.getTime());
			} else {
				into.setTime(date.getTime());
				return into;
			}
		}
	}
	
	public interface DustIdentifier {		
		String getName();
		String asPath();
		String asReference();
	}
	
	interface ResponseProcessor {
		boolean processResponse(DustObject target, Enum<?> msgId, DustObject msgOb, DustObject response);
	}

	enum Indent { inc, keep, dec };

	public interface DustStream {	

		interface Out extends DustStream {
			void put(String str);
			void endLine(Indent indent);		
		}
		
	}


	Return CONTINUE = new Return(ReturnType.Continue, null, true);
	
	Return SUCCESS_RETRY = new Return(ReturnType.Success, null, false);
	Return SUCCESS = new Return(ReturnType.Success, null, true);
	
	Return FAILURE = new Return(ReturnType.Failure, null, false);
	
	Return PASS_DEFAULT = new Return(ReturnType.Pass, null, false);
}
