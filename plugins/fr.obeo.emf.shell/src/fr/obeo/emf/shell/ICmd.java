package fr.obeo.emf.shell;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * Command interface.
 * 
 * @author ohaegi
 *
 */
public interface ICmd {
	/**
	 * Command evaluation.
	 * 
	 * @param eObject
	 *            evaluation context
	 * @param emfConsole
	 *            emf shell reference
	 * @return return evaluation result as string
	 */
	String eval(EObject eObject, EmfShell emfConsole);

	/**
	 * Command help.
	 * 
	 * @return String representing help
	 */
	String help();

	/**
	 * Test if cmdStr is a candidate.
	 * 
	 * @param cmdStr
	 *            command line string
	 * 
	 * @return return true if cmdStr is considered as self command candidate
	 */
	boolean isCmdCandidate(String cmdStr);

	/**
	 * Get command id.
	 * 
	 * @return command id
	 */
	String getCmdId();

	/**
	 * Get string list of command completion.
	 * 
	 * @param currentNode
	 *            evaluation context
	 * @return string list of command completion in context
	 */
	List<String> getCompletionList(EObject currentNode);
}
