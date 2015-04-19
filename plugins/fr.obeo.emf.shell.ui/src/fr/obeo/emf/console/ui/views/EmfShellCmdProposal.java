package fr.obeo.emf.console.ui.views;

import java.util.List;

import org.eclipse.jface.fieldassist.IContentProposal;

import fr.obeo.emf.shell.EmfShell;
import fr.obeo.emf.shell.ICmd;

public class EmfShellCmdProposal implements IContentProposal {

	private EmfShell emfShell;
	private  ICmd command;

	/**
	 * Constructeur
	 * 
	 * @param name
	 *            le nom de la commande
	 */

	public EmfShellCmdProposal(ICmd command, EmfShell emfShell) {
		this.command = command;
		this.emfShell = emfShell;
	}

	@Override
	public String getContent() {
		// Cette methode doit retourner le contenu a placer dans le champ texte
		return command.getCmdId();
	}

	@Override
	public int getCursorPosition() {
		// Cette methode retourne la position que doit avoir le curseur apres
		// insertion
		return command.getCmdId().length();
	}

	@Override
	public String getLabel() {
		// Le texte a afficher dans la liste des elements disponibles
		return command.getCmdId();
	}

	@Override
	public String getDescription() {
		return "Command " + command.getCmdId() + " :\n" + command.help();
	}
	
	public List<String> getCompletionList(){
		return command.getCompletionList(emfShell.getCurrentNode());
	}
}