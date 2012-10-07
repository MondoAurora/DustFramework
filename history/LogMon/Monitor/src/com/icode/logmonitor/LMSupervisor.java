package com.icode.logmonitor;

import java.util.*;

import com.icode.datacube.DataCubeCollector;
import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;
import com.icode.generic.obj.ICGenObjTranslator;

public class LMSupervisor implements ICGenObject.Processor, ICGenDataManageable, LMConstants {

	long cutTime = -1; // events older than this will be dropped
	long startTime;
	ICGenObjTranslator tr;
	
	boolean firstEvent = true;

	Map mapCollectors = new HashMap();
	DataCubeCollector[] collectors;
	AgentController[] agentControllers;

	class AgentController {
		LMAgent agent;

		long notCheckedWindowStart = -1;

		AgentController(LMAgent agent) {
			this.agent = agent;

			for (int i = 0; i < agent.getLinkCount(); ++i) {
				LMAgent.CollectorLink cl = agent.getLink(i);
				cl.init((DataCubeCollector) mapCollectors.get(cl.collName));
			}
		}

		boolean optProcess() throws Exception {
			DataCubeCollector coll;
			long diff;

			boolean doneProcess = false;
			for (boolean doProcess = true; doProcess; ) {
				for (int i = 0; doProcess && (i < agent.getLinkCount()); ++i) {
					coll = agent.getLink(i).coll;
					diff = coll.getCurrSegment().longValue() - notCheckedWindowStart;
					if (agent.getWindowLengthMSec() >= diff) {
						doProcess = false;
					}
				}

				if (doProcess) {
					Long start = new Long(notCheckedWindowStart);
					Long end = new Long(notCheckedWindowStart + agent.getWindowLengthMSec());

					agent.process(start, end);

					notCheckedWindowStart += agent.getSegmentMSec();
					doneProcess = true;
				}
			}

			return doneProcess;
		}
	}

	public Object processObject(ICGenObject ob) throws Exception {
		boolean collectorStep = false;
		for (int i = 0; i < collectors.length; ++i) {
			collectorStep |= collectors[i].processObject(ob);
		}

		if (collectorStep) {
			long time = ((Date)tr.getAttByIdx(ob, 0)).getTime();
			
			if ( firstEvent ) {
				AgentController ac;
				for (int i = 0; i < agentControllers.length; ++i) {
					ac = agentControllers[i]; 
					ac.notCheckedWindowStart = ICGenUtilsBase.getTimeSegment(time, ac.agent.getSegmentMSec());
				}
				firstEvent = false;
			}
			
			boolean processed = false;
			for (int i = 0; i < agentControllers.length; ++i) {
				processed |= agentControllers[i].optProcess();
				long ltime = agentControllers[i].notCheckedWindowStart;
				if ( ltime < time ) {
					time = ltime;
				}
			}

			if ( processed ) {
				Long cutTime = new Long(time);
				for (int i = 0; i < collectors.length; ++i) {
					collectors[i].dropSegmentBelow(cutTime);
				}				
			}
		}

		return null;
	}

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		cutTime = node.getOptionalLong(CFG_LM_CUTTIME, -1);
		
		tr = new ICGenObjTranslator(node.getOptional(CFG_LM_CUTFIELD, EVT_TIME));

		ICGenTreeNode n = node.getChild(CFG_SUPERVISOR_COLLECTORS);
		if (null != n) {
			DataCubeCollector coll;

			for (Iterator it = n.getChildren(); it.hasNext();) {
				n = (ICGenTreeNode) it.next();
				coll = (DataCubeCollector) ICAppFrame.getComponent(n, DataCubeCollector.class);
				mapCollectors.put(coll.getName(), coll);
			}
		}
		collectors = (DataCubeCollector[]) mapCollectors.values().toArray(new DataCubeCollector[mapCollectors.size()]);

		n = node.getChild(CFG_SUPERVISOR_AGENTS);
		ArrayList agents = new ArrayList();

		if (null != n) {
			for (Iterator it = n.getChildren(); it.hasNext();) {
				n = (ICGenTreeNode) it.next();
				agents.add(ICAppFrame.getComponent(n, LMAgent.class));
			}
		}

		int count = agents.size();
		agentControllers = new AgentController[count];

		for (int i = 0; i < count; ++i) {
			agentControllers[i] = new AgentController((LMAgent) agents.get(i));
		}

	}

	public void storeDataInto(ICGenTreeNode node, Object hint) {
		node.addChild(CFG_LM_CUTTIME, String.valueOf(cutTime));
	}
}
