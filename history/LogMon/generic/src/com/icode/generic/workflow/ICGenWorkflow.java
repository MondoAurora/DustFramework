package com.icode.generic.workflow;

import java.util.*;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.ICGenConfigurable;
import com.icode.generic.base.ICGenTreeNode;

public class ICGenWorkflow implements ICGenConfigurable {

	public static abstract class State implements ICGenConfigurable {
		private String id;
		boolean endState;

		public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
			id = node.getName();
			endState = Boolean.valueOf(node.getOptional("end", "")).booleanValue();
		}

		// process the external event and return the id of the next state to which
		// the workflow should get
		protected abstract String processMessage(Object ob) throws Exception;

		protected void reset() {
		}

		public final String getId() {
			return id;
		}

		public final boolean isEndState() {
			return endState;
		}
	}
	
	public static class StateEnd extends State {
		protected String processMessage(Object ob) throws Exception {
			throw new RuntimeException("The endstate cannot continue!");
		}
		public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
			super.loadDataFrom(node, hint);
			endState = true;
		}
	}
	

	private String startAt;
	private Map mapStates = new HashMap();

	private State currState;

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		
		State s = null;
		ICGenTreeNode n = node.getChild("states");
		for (Iterator it = n.getChildren(); it.hasNext(); ) {
			s = (State) ICAppFrame.getComponent((ICGenTreeNode) it.next(), State.class);
			mapStates.put(s.id, s);
		}

		if ( 1 < mapStates.size() ) {
			startAt = node.getMandatory("start");
		} else {
			startAt = s.getId();
		}

		reset();
	}

	public State processMessage(Object ob) throws Exception {
		State s = (State) mapStates.get(currState.processMessage(ob));
		if ( null != s ) {
			currState = s;
		}
		return s;
	}

	public State reset() {
		return currState = (State) mapStates.get(startAt);
	}

	public State getState() {
		return currState;
	}

	public boolean isFinished() {
		return currState.isEndState();
	}

}
