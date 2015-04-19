package fr.obeo.emf.shell.cmd.impl;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import fr.obeo.emf.shell.EmfShell;
import fr.obeo.emf.shell.ICmd;
import fr.obeo.emf.shell.cmd.Cmd;

public class Print extends Cmd implements ICmd {
	
	@Override
	public String getCmdId() {
		return "print";
	}
	
	public boolean isCmdCandidate(String cmdStr){
		return super.isCmdCandidate(cmdStr, getCmdId());
	}

	@Override
	public String eval(EObject eObject, EmfShell emfConsole) {
		return print(eObject);
	}
	
	@Override
	public String help() {
		String help = "print .\n";
		help = help + "  print current node.";
		return help;
	}
	
	@Override
	public List<String> getCompletionList(EObject currentNode) {
		return null;
	}
}
