package fr.obeo.emf.shell.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.emf.ecore.EObject;

public abstract class Cmd {
	
	protected List<String> args;
	
	
	public void setArgs(String argsStr) {
		this.args = getArgs(argsStr);
	}
	
	private List<String> getArgs(String argsStr){
		List<String> result = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(argsStr);
		if (st.hasMoreTokens()) {
			st.nextToken();
		}
	     while (st.hasMoreTokens()) {
	    	 result.add(st.nextToken());
	     }
	     return result;
	}
	
	protected boolean isCmdCandidate(String cmdStr, String cmdId){
		return cmdStr.trim().startsWith(cmdId);
	}
	
	public String print(EObject eObject){
		return eObject.eClass().getName() + "@" + Integer.toHexString(System.identityHashCode(eObject));
	}
}
