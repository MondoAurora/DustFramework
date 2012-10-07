package com.icode.datacube;

import java.util.*;

import com.icode.datacube.DataCubeCollector.ConvAttDef;
import com.icode.generic.ICGenObjectDefault;
import com.icode.generic.ICGenPersistentStorage;
import com.icode.generic.base.ICGenObject;
import com.icode.generic.base.ICGenObjectDefDefault;
import com.icode.generic.conv.ICGenConvAggregation;
import com.icode.generic.conv.ICGenConverter;
import com.icode.generic.obj.ICGenObjTranslator;

public class DataCube implements DataCubeConstants {
	ICGenObject.ObDef odInputDef;
	
	ICGenObjTranslator trDataToLoc;
	ICGenObjTranslator trDataToAggr;

	ICGenObject.ObDef odAggrCell;
	ICGenObject.ObDef odLocCell;
	
	Map[] mapDimIdx;
	Map[] mapDimIdxRev;
	Map mapCell = new TreeMap();
	int dCount;

	int aCount;
	ICGenConvAggregation[] aggrs;
	

	class CellLoc implements Comparable {
		public final long[] dimIdx;

		public CellLoc() {
			this.dimIdx = new long[dCount];
		}

		public boolean fits(CellLoc loc) {
			long l1, l2;
			for (int i = 0; i < dCount; ++i) {
				l1 = loc.dimIdx[i];
				l2 = dimIdx[i];
				if ((-1 != l1) && (-1 != l2) && (l1 != l2)) {
					return false;
				}
			}
			return true;
		}

		public int compareTo(Object obj) {
			if (obj instanceof CellLoc) {
				CellLoc cl = (CellLoc) obj;
				long diff;
				for (int i = 0; i < dCount; ++i) {
					diff = dimIdx[i] - cl.dimIdx[i];
					if (0 != diff) {
						return (0 < diff) ? 1 : -1;
					}
				}
			}

			return 0;
		}
	}

	public class ContentEnumerator implements Enumeration {
		private Iterator it;
		ICGenObject data;
		ICGenObject cell;

		private ContentEnumerator() {
			it = mapCell.entrySet().iterator();
			data = new ICGenObjectDefault(odInputDef);
		}

		public Object nextElement() {
			if (it.hasNext()) {
				data.reset();
				
				Map.Entry e = (Map.Entry) it.next();
				CellLoc cl = (CellLoc) e.getKey();
				cell = (ICGenObject) e.getValue();

				for (int i = 0; i < dCount; ++i) {
					trDataToLoc.setAttByIdx(data, i, mapDimIdxRev[i].get(new Long(cl.dimIdx[i])));
				}

				for (int i = 0; i < aCount; ++i) {
					trDataToAggr.setAttByIdx(data, i, cell.getAttribObj(i));
				}

				return data;
			} else {
				return null;
			}
		}

		public boolean hasMoreElements() {
			return it.hasNext();
		}

	}

	public DataCube(ICGenObject.ObDef odInput) {
		odInputDef = odInput;
				
		ArrayList alDims = new ArrayList();
		ArrayList alAggrs = new ArrayList();
		ConvAttDef cvtAd;
		String[] names;
		
		for ( int i = 0; i < odInput.getAttCount(); ++i ) {
			cvtAd = (ConvAttDef) odInput.getAtt(i);

			if ( ICGenConverter.TYPE_LOCATION == cvtAd.loader.type ) {
				alDims.add(cvtAd.getName());
			} else {
				alAggrs.add(cvtAd.getName());				
			}
		}
		
		dCount = alDims.size();
		names = (String[]) alDims.toArray(new String[dCount]);
		odLocCell = new ICGenObjectDefDefault(odInput, names);
		trDataToLoc = new ICGenObjTranslator(names);

		mapDimIdx = new HashMap[dCount];
		mapDimIdxRev = new HashMap[dCount];
		for (int i = 0; i < dCount; ++i) {
			mapDimIdx[i] = new HashMap();
			mapDimIdxRev[i] = new HashMap();
		}

		aCount = alAggrs.size();
		names = (String[]) alAggrs.toArray(new String[aCount]);
		odAggrCell = new ICGenObjectDefDefault(odInput, names);
		trDataToAggr = new ICGenObjTranslator(names);
		
		aggrs = new ICGenConvAggregation[aCount];
		for (int i = 0; i < aCount; ++i) {
			cvtAd = (ConvAttDef) odAggrCell.getAtt(i); 
			aggrs[i] = (ICGenConvAggregation) cvtAd.loader;
		}
	}

	public void addCell(ICGenObject rawData) {
		ICGenObject loc = new ICGenObjectDefault(odLocCell);
		
		for (int i = odLocCell.getAttCount(); i-- > 0;) {
			((ConvAttDef) odLocCell.getAtt(i)).loader.convert(rawData, loc);
		}

		ICGenObject cell = findCell(loc);

		for (int i = 0; i < aCount; ++i) {
			aggrs[i].convert(rawData, cell);
		}
	}

	public void combineCell(ICGenObject cell) {
		ICGenObject intoCell = findCell(cell);

		for (int i = 0; i < aCount; ++i) {
			aggrs[i].combine(cell, intoCell);
		}
	}

	public ICGenObject findCell(ICGenObject data) {
		CellLoc cl = new CellLoc();

		long l;
		Long ll;
		Map di;
		Object val;

		for (int i = 0; i < dCount; ++i) {
			val = trDataToLoc.getAttByIdx(data, i);

			di = mapDimIdx[i];
			ll = (Long) di.get(val);
			if (null == ll) {
				l = di.size();
				ll = new Long(l);
				di.put(val, ll);
				mapDimIdxRev[i].put(ll, val);
			} else {
				l = ll.longValue();
			}

			cl.dimIdx[i] = l;
		}

		ICGenObject cell = (ICGenObject) mapCell.get(cl);

		if (null == cell) {
			cell = new ICGenObjectDefault(odAggrCell);
			mapCell.put(cl, cell);
		}

		return cell;
	}

	public void clear() {
		clear(true);
	}

	public void clear(boolean dropDimMaps) {
		mapCell.clear();

		if (dropDimMaps) {
			for (int i = 0; i < dCount; ++i) {
				mapDimIdx[i].clear();
				mapDimIdxRev[i].clear();
			}
		}
	}

	public void dump(ICGenPersistentStorage db, String name, Long segKey, long segLength) throws Exception {
		ICGenObject ob = new ICGenObjectDefault(CUBE_DATA_DEF);
		ob.setAttribObj(0, name);
		ob.setAttribObj(1, new Date(segKey.longValue()));
		ob.setAttribObj(2, new Date(segKey.longValue() + segLength));

		db.store(ob, null);
		
		for ( Enumeration content = new ContentEnumerator(); content.hasMoreElements(); ) {
			db.store((ICGenObject) content.nextElement(), ob);
		}
	}

	public void dbLoad(ICGenPersistentStorage db, ICGenObject ob) throws Exception {
		clear();
		
		for ( Enumeration content = db.find(odInputDef, ob, null, null); content.hasMoreElements(); ) {
			ICGenObject cell = (ICGenObject) content.nextElement();
			combineCell(cell);
		}
	}

	public void flushCube(DataCube into) {
		for ( Enumeration content = new ContentEnumerator(); content.hasMoreElements(); ) {
			into.combineCell((ICGenObject) content.nextElement());
		}
	}

	public ContentEnumerator enumContent() {
		return new ContentEnumerator();
	}

	public Enumeration getDimValues(int dimIdx) {
		return Collections.enumeration(mapDimIdx[dimIdx].keySet());
	}
}
