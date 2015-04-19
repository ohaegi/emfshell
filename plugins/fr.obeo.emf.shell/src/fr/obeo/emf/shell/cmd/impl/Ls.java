package fr.obeo.emf.shell.cmd.impl;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import fr.obeo.emf.shell.EmfShell;
import fr.obeo.emf.shell.ICmd;
import fr.obeo.emf.shell.cmd.Cmd;

public class Ls extends Cmd implements ICmd {
	
	@Override
	public String getCmdId() {
		return "ls";
	}
	
	public boolean isCmdCandidate(String cmdStr){
		return super.isCmdCandidate(cmdStr, getCmdId());
	}
	
	

	@Override
	public String eval(EObject eObject, EmfShell emfConsole) {
		StringBuffer sb = new StringBuffer();
		ICmd p = new Print();
		for (EObject c : eObject.eContents()) {
			sb.append(p.eval(c,emfConsole));
			sb.append('\n');
		}
		return sb.toString().trim();
	}
	
	@Override
	public String help() {
		String help = "ls .\n";
		help = help + "  ls allow to show direct contents EObject of current node.";
		return help;
	}
	
	@Override
	public List<String> getCompletionList(EObject currentNode) {
		return null;
	}
}
