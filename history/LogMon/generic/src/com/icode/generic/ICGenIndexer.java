package com.icode.generic;

import java.util.*;

import com.icode.generic.base.ICGenUtilsBase;

public abstract class ICGenIndexer {
	Map[] mapDimIdx;
	Map[] mapDimIdxRev;

	int dCount;

	protected abstract Object getVal(Object ob, int idx);

	public static class Index implements Comparable {
		public final long[] dimIdx;

		public Index(int dCount) {
			this.dimIdx = new long[dCount];
		}

		public Index(int dCount, long defVal) {
			this(dCount);
			for (int i = 0; i < dimIdx.length; ++i) {
				dimIdx[i] = defVal;
			}
		}

		public int contains(Index loc) {
			long l1, l2;
			int fit = 0;

			for (int i = 0; i < dimIdx.length; ++i) {
				l1 = loc.dimIdx[i];
				l2 = dimIdx[i];
				if (l1 == l2) {
					if ( -1 != l2 ){
						fit = i + 1;
					}
				} else if ( -1 != l2 ){
					return -1;
				}
			}

			return fit;
		}

		public int compareTo(Object obj) {
			if (obj instanceof Index) {
				Index cl = (Index) obj;
				long diff;
				for (int i = 0; i < dimIdx.length; ++i) {
					diff = dimIdx[i] - cl.dimIdx[i];
					if (0 != diff) {
						return (0 < diff) ? 1 : -1;
					}
				}
			}

			return 0;
		}

		public Index shorten() {
			Index ret = new Index(dimIdx.length, -1);
			boolean ok = false;
			for (int i = dimIdx.length; i-- > 0;) {
				if (ok) {
					ret.dimIdx[i] = dimIdx[i];
				} else if (-1 != dimIdx[i]) {
					ok = true;
				}
			}
			return ret;
		}
	}

	public ICGenIndexer(int count) {
		dCount = count;

		mapDimIdx = new HashMap[dCount];
		mapDimIdxRev = new HashMap[dCount];
		for (int i = 0; i < dCount; ++i) {
			mapDimIdx[i] = new HashMap();
			mapDimIdxRev[i] = new HashMap();
		}
	}

	public String buildPath(Index idx, String prefix) {
		StringBuffer b = new StringBuffer();

		for (int i = 0; i < dCount; ++i) {
			String s = (String) mapDimIdxRev[i].get(new Long(idx.dimIdx[i]));
			if (ICGenUtilsBase.isEmpty(s)) {
				break;
			}
			b.append(prefix).append(s);
		}

		return b.toString();
	}

	public Index indexOf(Object ob) {
		Index cl = new Index(dCount);

		Long ll;
		Map di;
		Object val;

		for (int i = 0; i < dCount; ++i) {
			val = getVal(ob, i);

			if (null == val) {
				cl.dimIdx[i] = -1;
			} else {
				di = mapDimIdx[i];
				ll = (Long) di.get(val);
				if (null == ll) {
					ll = new Long(di.size());
					di.put(val, ll);
					mapDimIdxRev[i].put(ll, val);
				}

				cl.dimIdx[i] = ll.longValue();
			}
		}

		return cl;
	}

	public void clear() {
		for (int i = 0; i < dCount; ++i) {
			mapDimIdx[i].clear();
			mapDimIdxRev[i].clear();
		}
	}
}
