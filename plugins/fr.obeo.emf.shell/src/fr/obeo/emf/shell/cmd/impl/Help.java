package fr.obeo.emf.shell.cmd.impl;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import fr.obeo.emf.shell.EmfShell;
import fr.obeo.emf.shell.ICmd;
import fr.obeo.emf.shell.cmd.Cmd;
import fr.obeo.emf.shell.cmd.CmdBuilder;

public class Help extends Cmd implements ICmd {
	
	@Override
	public String getCmdId() {
		return "help";
	}
	
	public boolean isCmdCandidate(String cmdStr){
		return super.isCmdCandidate(cmdStr, getCmdId());
	}
	
	@Override
	public String eval(EObject eObject, EmfShell emfConsole) {
		if (args.size() == 1) {
			String helpOnCmd = args.get(0);
			for (ICmd cmd : CmdBuilder.LIST_CMD) {
				if (cmd.isCmdCandidate(helpOnCmd)) {
					return(cmd.help());
				}
			}
		}
		
		return help();
	}
	
	@Override
	public String help() {
		String help = "help .\n";
		help = help + "  show general help of emf shell.\n";
		help = help + "  for detail :\n";
		for (ICmd cmd : CmdBuilder.LIST_CMD) {
			if (!getCmdId().equals(cmd.getCmdId())) {
				help = help + "   help " + cmd.getCmdId() + "\n";
			}
		}
		return help.trim();
	}
	
	@Override
	public List<String> getCompletionList(EObject currentNode) {
		return null;
	}
}
