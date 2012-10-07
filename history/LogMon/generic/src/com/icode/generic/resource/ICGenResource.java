package com.icode.generic.resource;

import java.util.*;

import com.icode.generic.ICGenIndexer;
import com.icode.generic.ICGenIndexer.Index;
import com.icode.generic.base.*;

public abstract class ICGenResource implements ICGenConfigurable {
	private static final ICGenIndexer.Index NO_LOCALE = new ICGenIndexer.Index(3, -1);

	class ResValue implements Comparable {
		ICGenIndexer.Index idxLocale;
		String text;

		ResValue(Index idxLocale, String text) {
			this.idxLocale = idxLocale;
			this.text = text;
		}

		public int compareTo(Object o) {
			return idxLocale.compareTo(((ResValue) o).idxLocale);
		}
	}

	protected class ModuleRes {
		String moduleName;

		Map mapIDs = new HashMap();
		Set setAskedLocales = new TreeSet();

		ModuleRes(String moduleName) {
			this.moduleName = moduleName;
		}

		public void addStr(String id, ICGenIndexer.Index idxLocale, String value) {
			Set resVersions = (Set) mapIDs.get(id);
			setAskedLocales.add(idxLocale);

			if (null == resVersions) {
				resVersions = new TreeSet();
				mapIDs.put(id, resVersions);
			}

			resVersions.add(new ResValue(idxLocale, value));
		}

		void checkResLoaded(ICGenIndexer.Index idxLocale) {
			while ((0 != NO_LOCALE.compareTo(idxLocale)) && !setAskedLocales.contains(idxLocale)) {
				String resName = moduleName + locIndexer.buildPath(idxLocale, "_");
				fillModule(this, idxLocale, resName);
				setAskedLocales.add(idxLocale);
				idxLocale = idxLocale.shorten();
			}
		}

		String getValue(String id, ICGenIndexer.Index idxLocale) {
			String ret = null;

			checkResLoaded(idxLocale);

			Set resVersions = (Set) mapIDs.get(id);

			if (null != resVersions) {
				int fit = -1, f;
				for (Iterator it = resVersions.iterator(); it.hasNext();) {
					ResValue rv = ((ResValue) it.next());
					f = rv.idxLocale.contains(idxLocale);
					if (fit < f) {
						fit = f;
						ret = rv.text;
					}
				}
			}

			return ret;
		}
	}

	ICGenIndexer locIndexer = new ICGenIndexer(3) {
		protected Object getVal(Object ob, int idx) {
			String ret;

			switch (idx) {
			case 0:
				ret = ((Locale) ob).getLanguage();
				break;
			case 1:
				ret = ((Locale) ob).getCountry();
				break;
			case 2:
				ret = ((Locale) ob).getVariant();
				break;
			default:
				throw new RuntimeException("ICGenResource.locIndexer index failure!!!");
			}

			return ICGenUtilsBase.isEmpty(ret) ? null : ret;
		}
	};

	Map mapResValues = new HashMap();

	public String getResStr(String module, String resId) {
		return getResStr(module, resId, Locale.getDefault());
	}

	public ICGenIndexer.Index getLocaleIndex(Locale locale) {
		return locIndexer.indexOf(locale);
	}

	public String getResStr(String module, String resId, Locale lc) {
		ICGenIndexer.Index idxLocale = (null == lc) ? NO_LOCALE : locIndexer.indexOf(lc);

		ModuleRes modRes = (ModuleRes) mapResValues.get(module);

		if (null == modRes) {
			modRes = new ModuleRes(module);
			mapResValues.put(module, modRes);
		}

		return modRes.getValue(resId, idxLocale);
	}

	protected abstract void fillModule(ModuleRes modRes, ICGenIndexer.Index idxLocale, String resName) ;

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		ICGenTreeNode n;

		n = node.getChild("modules");
		if (null != n) {
			for (Iterator it = n.getChildren(); it.hasNext();) {
				n = (ICGenTreeNode) it.next();
				ModuleRes modRes = new ModuleRes(n.getName());
				mapResValues.put(n.getName(), modRes);
				for (Iterator it2 = n.getChildren(); it2.hasNext();) {
					n = (ICGenTreeNode) it2.next();
					modRes.addStr(n.getName(), NO_LOCALE, n.getValue());
				}
			}
		}
	}
}
