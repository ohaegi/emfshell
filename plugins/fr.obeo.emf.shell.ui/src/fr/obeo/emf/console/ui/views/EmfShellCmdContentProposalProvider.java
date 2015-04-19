package fr.obeo.emf.console.ui.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

import fr.obeo.emf.shell.EmfShell;
import fr.obeo.emf.shell.ICmd;

public class EmfShellCmdContentProposalProvider implements
		IContentProposalProvider {

	private boolean filterProposals = false;
	
	@SuppressWarnings("unused")
	private EmfShell emfShell;
	private List<EmfShellCmdProposal> elements = new ArrayList<EmfShellCmdProposal>();

	/**
	 * 
	 * Constructeur
	 * 
	 * @param filter
	 *            <code>true</code> si les elements doivent etre filtres
	 * 
	 * 
	 */

	public EmfShellCmdContentProposalProvider(boolean filter) {
		this.filterProposals = filter;
	}
	
	public void initElements(EmfShell emfShell){
		this.emfShell = emfShell;
		elements = new ArrayList<EmfShellCmdProposal>();
		for (ICmd cmd : EmfShell.getCmdList()) {
			elements.add(new EmfShellCmdProposal(cmd,emfShell));
		}
	}

	@Override
	public IContentProposal[] getProposals(String contents, int position) {
		if (filterProposals) {
			ArrayList<IContentProposal> list = new ArrayList<IContentProposal>();
			EmfShellCmdProposal emfShellCmdProposal = getCmdProposal(contents.trim());
			if (emfShellCmdProposal != null) {
				for (String strList : emfShellCmdProposal.getCompletionList()) {
					String cmdList = emfShellCmdProposal.getContent() + " " + strList;
					if (cmdList.startsWith(contents.trim())) {
						list.add(new ContentProposal(cmdList));
					}
				}
			}
			for (IContentProposal proposal : elements) {
				if (proposal.getContent().startsWith(contents.trim())) {
					// Exclude emfShellCmdProposal
					if (emfShellCmdProposal != null && proposal.getContent().equals(emfShellCmdProposal.getContent())) {
						continue;
					}
					list.add(proposal);
				}
			}
			return list.toArray(new IContentProposal[list.size()]);
		}
		return elements.toArray(new IContentProposal[elements.size()]);
	}
	
	private EmfShellCmdProposal getCmdProposal(String cmdString) {
		for (EmfShellCmdProposal emfShellCmdProposal : elements) {
			if (cmdString.startsWith(emfShellCmdProposal.getContent())) {
				return emfShellCmdProposal;
			}
		}
		return null;
	}
}
