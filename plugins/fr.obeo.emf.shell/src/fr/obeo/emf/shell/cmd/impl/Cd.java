package fr.obeo.emf.shell.cmd.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import fr.obeo.emf.shell.EmfShell;
import fr.obeo.emf.shell.ICmd;
import fr.obeo.emf.shell.cmd.Cmd;

public class Cd extends Cmd implements ICmd{

	@Override
	public String getCmdId() {
		return "cd";
	}
	
	@Override
	public boolean isCmdCandidate(String cmdStr){
		return super.isCmdCandidate(cmdStr, getCmdId());
	}
		
	@Override 
	public String eval(EObject eObject, EmfShell emfConsole) {
		String nodeId = "";
		if (args.size() == 0) {
			emfConsole.setCurrentNode(emfConsole.getRootNode());
		} else if (args.size()> 0) {
			nodeId = args.get(0);
		}
		if ("..".equals(nodeId)) {
			emfConsole.setCurrentToParent();
		}
		for (EObject currentNode : eObject.eContents()) {
			if (print(currentNode).equals(nodeId)
					|| print(currentNode).endsWith("@" + nodeId)) {
				emfConsole.setCurrentNode(currentNode);
				return "";
			}
		}
		return "";
	}

	@Override
	public String help() {
		String help = "cd [className@id|..].\n";
		help = help + "  cd allow to navigate in model.\n";
		help = help + "   - go to a direct node : cd className@id\n";
		help = help + "	  - go to parent node : cd ..\n";
		help = help + "	  - go to root node : cd";
		return help;
	}
	
	@Override
	public List<String> getCompletionList(EObject currentNode) {
		List<String> result = new ArrayList<String>();
		result.add("");
		result.add("..");
		for (EObject c : currentNode.eContents()) {
			result.add(print(c));
		}
		return result;
	}
}
