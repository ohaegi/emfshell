package fr.obeo.emf.shell;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import fr.obeo.emf.shell.cmd.CmdBuilder;
import fr.obeo.emf.shell.cmd.impl.Pwd;
import fr.obeo.emf.shell.exception.ConsoleEmfException;

public class EmfShell {
	
	private Resource currentResource;
	private EObject currentNode;
	private EObject rootNode;
	private String message;
	private List<String> cmdHistory = new ArrayList<String>();
	private int cmdHistoryIndex;
	
	public EObject getRootNode() {
		return rootNode;
	}

	public void setRootNode(EObject rootNode) {
		this.rootNode = rootNode;
	}

	public String getPrompt() {
		return fullQulifierCurrentNode().trim() + " > ";
	}
	
	public EmfShell() {
		super();
		message = "";
	}
	
	public Resource getCurrentResource() {
		return currentResource;
	}

	public void setCurrentResource(Resource currentResource) {
		currentNode = currentResource.getContents().get(0);
		rootNode = currentNode;
		this.currentResource = currentResource;
	}

	public EObject getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(EObject currentNode) {
		this.currentNode = currentNode;
	}
	
	public void setCurrentToParent() {
		if (currentNode != rootNode) {
			currentNode = currentNode.eContainer();
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void exec(String cmdStr) {
		try {
			// Clean cmdStr
			String cleanCmdStr = cmdStr.replaceAll("[\\r\\n]", "");
			ICmd currentCmd = CmdBuilder.getCommand(cleanCmdStr);
			
			cmdHistory.add(cleanCmdStr);
			cmdHistoryIndex = cmdHistory.size() - 1;
			message = currentCmd.eval(currentNode, this);
		} catch (ConsoleEmfException e) {
			message = e.getMessage();
		}
		
	}
	
	public String fullQulifierCurrentNode() {
		ICmd pwd = new Pwd();
		return pwd.eval(currentNode, this);
	}
	
	public String getNextCmdHistory(){
		if (cmdHistoryIndex != cmdHistory.size() - 1) {
			++cmdHistoryIndex;
		} else {
			return "";
		}
		return cmdHistory.get(cmdHistoryIndex);
	}
	
	public String getPreviousCmdHistory(){
		int curIndex = cmdHistoryIndex;
		if (cmdHistoryIndex != 0) {
			--cmdHistoryIndex;
		}
		return cmdHistory.get(curIndex);
	}
	
	public static List<ICmd> getCmdList(){
		return CmdBuilder.LIST_CMD;
	}

	public static String[] getCmdIdArray(){
		return CmdBuilder.getCmdIdArray();
	}
}
