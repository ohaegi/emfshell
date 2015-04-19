package fr.obeo.emf.console.ui.popup.actions;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import fr.obeo.emf.console.ui.views.EmfConsoleView;

public class OpenConsole implements IObjectActionDelegate {

	@SuppressWarnings("unused")
	private Shell shell;

	/**
	 * Constructor for Action1.
	 */
	public OpenConsole() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * 
	 * @return The EMF resource set of the last active editor (if it is still
	 *         open).
	 */
	public static ResourceSet getEditorResourceSet(final IEditorPart editor) {
		// final IEditorPart editor = getLastActiveEditor();
		if (editor == null)
			return null;
		EditingDomain domain = null;
		if (editor instanceof IEditingDomainProvider) {
			domain = ((IEditingDomainProvider) editor).getEditingDomain();
		} else if (editor.getAdapter(IEditingDomainProvider.class) != null) {
			domain = ((IEditingDomainProvider) editor
					.getAdapter(IEditingDomainProvider.class))
					.getEditingDomain();
		} else if (editor.getAdapter(EditingDomain.class) != null) {
			domain = (EditingDomain) editor.getAdapter(EditingDomain.class);
		}
		if (domain == null) {
			return null;
		}

		return domain.getResourceSet();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		try {
			final String emfConsoleViewId = "fr.obeo.emf.console.ui.views.EmfConsoleView"; 
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(emfConsoleViewId);
			IViewPart view = getView(emfConsoleViewId);
			if (view instanceof EmfConsoleView) {
				EmfConsoleView emfConsoleView = (EmfConsoleView)view;
				emfConsoleView.refreshPart();
				
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	public static IViewPart getView(String id) {
		IViewReference viewReferences[] = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getViewReferences();
		for (int i = 0; i < viewReferences.length; i++) {
			if (id.equals(viewReferences[i].getId())) {
				return viewReferences[i].getView(false);
			}
		}
		return null;
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}
}
