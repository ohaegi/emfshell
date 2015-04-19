package fr.obeo.emf.console.ui.views;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import fr.obeo.emf.shell.EmfShell;

//import fr.obeo.emf.console.EmfConsole;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class EmfConsoleView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "fr.obeo.emf.console.ui.views.EmfConsoleView";

	// private TableViewer viewer;
	private Action action1;
	private Action action2;
	// private Action doubleClickAction;

	// private List<Resource> resourceList;

	// Widgets
	private ComboViewer emfRessourceChoice;
	private Text cmd;
	private Text currentObj;
	private Text result;

	// Console Emf
	private EmfShell emfConsole = new EmfShell();
	private Resource emfResource;

	// Completion feature
	private EmfShellCmdContentProposalProvider proposalsProvider = new EmfShellCmdContentProposalProvider(true);
	private ContentProposalAdapter contentProposalAdapter = null;

	/*
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */

	class ResourceComboContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			return getLstRessource().toArray();
		}
	}

	class ResourceComboLabelProvider extends LabelProvider {

		@Override
		public String getText(Object element) {
			if (element instanceof Resource) {
				Resource resource = (Resource) element;
				return resource.getURI().toString();
			}
			return super.getText(element);
		}
	}

	/**
	 * The constructor.
	 */
	public EmfConsoleView() {

	}

	public void refreshPart() {
		emfRessourceChoice
				.setContentProvider(new ResourceComboContentProvider());
		StructuredSelection selection = new StructuredSelection(
				getCurrentRessource());
		emfRessourceChoice.setSelection(selection);

		emfRessourceChoice.refresh();
		currentObj.setText(emfConsole.fullQulifierCurrentNode());
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		container.setLayout(gridLayout);

		new Label(container, SWT.NONE).setText("Emf Ressource :");

		emfRessourceChoice = new ComboViewer(container, SWT.NONE);
		emfRessourceChoice.setLabelProvider(new ResourceComboLabelProvider());
		emfRessourceChoice
				.setContentProvider(new ResourceComboContentProvider());
		emfRessourceChoice.setInput(getLstRessource().toArray());
		emfRessourceChoice.getCombo().setLayoutData(
				new GridData(GridData.FILL, GridData.CENTER, true, false));
		emfRessourceChoice.getCombo().addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Object selectedObj = emfRessourceChoice
						.getElementAt(emfRessourceChoice.getCombo()
								.getSelectionIndex());
				if (selectedObj instanceof Resource) {
					emfResource = (Resource) selectedObj;
//					emfConsole = new EmfShell();
					emfConsole.setCurrentResource(emfResource);
					proposalsProvider.initElements(emfConsole);
//				} else {
//					emfResource = null;
//					emfConsole = null;
				}
			}
		});
		Button buttonEmfFileRefersh = new Button(container, SWT.NONE);
		buttonEmfFileRefersh.setText("Refresh list");
		buttonEmfFileRefersh.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// Reload resource choice list
				refreshPart();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		// new Label(container, SWT.sNONE).setText("(choice)");

		new Label(container, SWT.NONE).setText("Cmd :");
		cmd = new Text(container, SWT.MULTI | SWT.LEAD | SWT.BORDER | SWT.WRAP);

		cmd.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				// To no think
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ARROW_UP) {
					cmd.setText(emfConsole.getPreviousCmdHistory());
				} else if (e.keyCode == SWT.ARROW_DOWN) {
					String cmdStr = emfConsole.getNextCmdHistory();
					if (!"".equals(cmdStr.trim())) {
						cmd.setText(cmdStr);
					}
				} else if (e.keyCode == SWT.CR) {
					emfConsole.exec(cmd.getText());
					cmd.setText("");
					cmd.update();
					currentObj.setText(emfConsole.fullQulifierCurrentNode());
					result.setText(emfConsole.getMessage());
				}
			}
		});
		// init completion
		initCompletion();
		cmd.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true,
				false));
		Label infoCmd = new Label(container, SWT.NONE);
		infoCmd.setText("(cmd)");

		new Label(container, SWT.NONE).setText("Current eObject :");
		currentObj = new Text(container, SWT.MULTI | SWT.LEAD | SWT.BORDER
				| SWT.WRAP);
		currentObj.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
				true, false));
		Label infoCurrentObj = new Label(container, SWT.NONE);
		infoCurrentObj.setText("(Current EObject)");

		new Label(container, SWT.NONE).setText("Result :");
		result = new Text(container, SWT.BORDER | SWT.WRAP | SWT.MULTI);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessVerticalSpace = true;
		result.setLayoutData(gridData);
		Label infoResult = new Label(container, SWT.NONE);
		infoResult.setText("(result)");

		// Create the help context id for the viewer's control
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void initCompletion() {

		// Set decoration
		ControlDecoration deco = new ControlDecoration(cmd, SWT.TOP| SWT.LEFT);
		Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage();
		deco.setDescriptionText("Utilisez TAB pour voir la liste des mots-cles.");
		deco.setImage(image);
		deco.setShowOnlyOnFocus(true);
		
				
		String[] proposals = EmfShell.getCmdIdArray();
		String autoActivationCharacters = " ";
		for (int i = 0; i < proposals.length; i++) {
			if (proposals[i].length() == 0)
				continue;
			char c = proposals[i].charAt(0);
			if (autoActivationCharacters.indexOf(c) == -1) {
				autoActivationCharacters += c;
			}
		}

		KeyStroke ks = null;
		// Pbs avec le filter la fonction disparait après la premier utilisation
		// Sans filte la fonction reste bien
//		proposalsProvider = new EmfShellCmdContentProposalProvider(true);
//		proposalsProvider.setFiltering(true);
		try {
			ks = KeyStroke.getInstance("tab");
		} catch (ParseException e1) {
			Logger.getLogger("").log(Level.SEVERE, "KeyStroke Parse Exception");
		}

//		proposalsProvider.setFiltering(true);

		contentProposalAdapter = new ContentProposalAdapter(cmd, new TextContentAdapter(),
			     proposalsProvider, ks, autoActivationCharacters.toCharArray());
//		contentProposalAdapter = new ContentProposalAdapter(cmd, new TextContentAdapter(),
//				proposalsProvider, ks, null);

		contentProposalAdapter
				.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		
//		contentProposalAdapter.setLabelProvider(new ILabelProvider() {
//
//	          @Override
//	          public void removeListener(ILabelProviderListener listener) {}
//
//	          @Override
//	          public boolean isLabelProperty(Object element, String property) {
//	             return false;
//	          }
//
//	          @Override
//	          public void dispose() {}
//
//	          @Override
//	          public void addListener(ILabelProviderListener listener) {}
//
//	          @Override
//	          public String getText(Object element) {
//	             return ((ContentProposal)element).getLabel();
//	          }
//
//	          @Override
//	          public Image getImage(Object element) {
//	             return null;
//	          }
//	       });

//		contentProposalAdapter
//				.addContentProposalListener(new IContentProposalListener() {
//
//					@Override
//					public void proposalAccepted(IContentProposal proposal) {
//						if (cmd.getText().startsWith("cd ")) {
//							proposalsProvider.setProposals(new String[] {
//								      "cd 1", "cd 2", "cd 3" });
//						}
//						System.out.println("Coucou ... j'ai choisi .... ");
//						initCompletion();
//					}
//				});
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				EmfConsoleView.this.fillContextMenu(manager);
			}
		});
		// Menu menu = menuMgr.createContextMenu(viewer.getControl());
		// viewer.getControl().setMenu(menu);
		// getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		// doubleClickAction = new Action() {
		// public void run() {
		// ISelection selection = viewer.getSelection();
		// Object obj =
		// ((IStructuredSelection)selection).getFirstElement();
		// showMessage("Double-click detected on "+obj.toString());
		// }
		// };
	}

	private void hookDoubleClickAction() {
		// viewer.addDoubleClickListener(new IDoubleClickListener() {
		// public void doubleClick(DoubleClickEvent event) {
		// doubleClickAction.run();
		// }
		// });
	}

	private void showMessage(String message) {
		// MessageDialog.openInformation(
		// viewer.getControl().getShell(),
		// "Emf Console",
		// message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		// viewer.getControl().setFocus();
	}

	private Resource getCurrentRessource() {
		IWorkbenchPage workbenchPage = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (workbenchPage == null) {
			return null;
		}
		IEditorPart editor = workbenchPage.getActiveEditor();
		ResourceSet rs = getEditorResourceSet(editor);
		if (rs != null) {
			List<Resource> lr = rs.getResources();
			for (Resource resource : lr) {
				return resource;
			}
		}
		return null;
	}

	private List<Resource> getLstRessource() {
		List<Resource> resourceList = new ArrayList<Resource>();
		// try {
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		// .getActivePage()
		// .showView("fr.obeo.emf.console.ui.views.EmfConsoleView");i
		IWorkbenchPage workbenchPage = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (workbenchPage == null) {
			return resourceList;
		}
		IEditorReference[] l = workbenchPage.getEditorReferences();
		for (int i = 0; i < l.length; i++) {
			IEditorPart editor = l[i].getEditor(false);
			ResourceSet rs = getEditorResourceSet(editor);
			if (rs != null) {
				List<Resource> lr = rs.getResources();
				for (Resource resource : lr) {
					resourceList.add(resource);
				}
			}
		}

		// } catch (PartInitException e) {
		// e.printStackTrace();
		// }
		return resourceList;
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
}