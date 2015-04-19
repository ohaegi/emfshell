package fr.obeo.emf.shell.cmd.impl;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import fr.obeo.emf.shell.EmfShell;
import fr.obeo.emf.shell.ICmd;
import fr.obeo.emf.shell.cmd.Cmd;

public class Pwd extends Cmd implements ICmd {
	
	@Override
	public String getCmdId() {
		return "pwd";
	}
	
	public boolean isCmdCandidate(String cmdStr){
		return super.isCmdCandidate(cmdStr, getCmdId());
	}

	@Override
	public String eval(EObject eObject, EmfShell emfConsole) {
		EObject cur = eObject;
		String result = print(cur);
		while (cur.eContainer() != null) {
			cur = cur.eContainer();
			result = print(cur) + "/" + result; 
		}
		return result;
	}
	
	@Override
	public String help() {
		String help = "pwd .\n";
		help = help + "  show current node and those containers.";
		return help;
	}
	
	@Override
	public List<String> getCompletionList(EObject currentNode) {
		return null;
	}
}
