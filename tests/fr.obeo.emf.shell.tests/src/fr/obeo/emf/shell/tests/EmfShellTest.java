package fr.obeo.emf.shell.tests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.examples.extlibrary.Book;
import org.eclipse.emf.examples.extlibrary.Library;
import org.junit.Assert;
import org.junit.Test;

import fr.obeo.emf.shell.EmfShell;
import fr.obeo.emf.shell.ICmd;

public class EmfShellTest {

	private static String[] cmdIdListExpexted = { "ls", "cd", "print", "cat", "pwd",
	"help"};
	
	@Test
	public void testCreation() throws FileNotFoundException, IOException {
		EmfShell emfShell = new EmfShell();
		Assert.assertEquals("", emfShell.getMessage());
		emfShell.setMessage("Test message");
		Assert.assertEquals("Test message", emfShell.getMessage());
		String modelPath = "ressources/My.extlibrary";
		Resource resource = ModelHelper.loadResourceModel(modelPath);
		emfShell.setCurrentResource(resource);
		Assert.assertTrue(emfShell.getRootNode() instanceof Library);
		Assert.assertTrue(emfShell.getRootNode() == emfShell.getCurrentNode());
		Assert.assertTrue(emfShell.getCurrentResource() == resource);
		Assert.assertEquals("Library@XXXX > ", emfShell.getPrompt().replaceFirst("@[^ ]+ ", "@XXXX "));
		Assert.assertEquals("Library@XXXX", emfShell.fullQulifierCurrentNode().replaceFirst("@[^ ]+", "@XXXX"));
	}
	
	@Test
	public void testCurrentNodeRootNodeParent() throws FileNotFoundException, IOException {
		EmfShell emfShell = new EmfShell();
		String modelPath = "ressources/My.extlibrary";
		Resource resource = ModelHelper.loadResourceModel(modelPath);
		emfShell.setCurrentResource(resource);
		
		Library rootLib = (Library)emfShell.getCurrentNode();
		Book book1 = rootLib.getBooks().get(0);
		
		Assert.assertEquals(rootLib, emfShell.getCurrentNode());
		emfShell.setCurrentNode(book1);
		Assert.assertEquals(book1, emfShell.getCurrentNode());
		Assert.assertEquals(rootLib, emfShell.getRootNode());
		emfShell.setCurrentToParent();
		Assert.assertEquals(rootLib, emfShell.getCurrentNode());
		Assert.assertEquals(rootLib, emfShell.getRootNode());
	}

	@Test
	public void testCmdIdArray() throws FileNotFoundException, IOException {
		String[] cmdIdList = EmfShell.getCmdIdArray();
		Assert.assertEquals(6, cmdIdList.length);
		
		for (String cmdId : cmdIdList) {
			boolean findExpected = false;
			for (String cmdIdExpected : cmdIdListExpexted) {
				if (cmdIdExpected.equals(cmdId)) {
					findExpected = true;
				}
			}
			if (!findExpected) {
				String cmdIdEpectedListStr = getListToString(cmdIdListExpexted);
				Assert.fail("The command id '" + cmdId + "' is not found in expect command id list : " + cmdIdEpectedListStr);
			}
		}

		for (String cmdIdExpected : cmdIdListExpexted) {
			boolean findExpected = false;
			for (String cmdId : cmdIdList) {
				if (cmdIdExpected.equals(cmdId)) {
					findExpected = true;
				}
			}
			if (!findExpected) {
				String cmdIdListStr = getListToString(cmdIdList);
				Assert.fail("The expected command id '" + cmdIdExpected + "' is not found in expect command id list : " + cmdIdListStr);
			}
		}

	}

	@Test
	public void testCmdList() throws FileNotFoundException, IOException {
		List<ICmd> cmdList = EmfShell.getCmdList();
		Assert.assertEquals(6, cmdList.size());
		
		for (ICmd cmd : cmdList) {
			boolean findExpected = false;
			for (String cmdIdExpected : cmdIdListExpexted) {
				if (cmdIdExpected.equals(cmd.getCmdId())) {
					findExpected = true;
				}
			}
			if (!findExpected) {
				String cmdIdEpectedListStr = getListToString(cmdIdListExpexted);
				Assert.fail("The command id '" + cmd + "' is not found in expect command id list : " + cmdIdEpectedListStr);
			}
		}
		
		for (String cmdIdExpected : cmdIdListExpexted) {
			boolean findExpected = false;
			for (ICmd cmd : cmdList) {
				if (cmdIdExpected.equals(cmd.getCmdId())) {
					findExpected = true;
				}
			}
			if (!findExpected) {
				String[] cmdIdArray = new String[cmdList.size()];
				for (int i = 0; i < cmdIdArray.length; i++) {
					cmdIdArray[i] = cmdList.get(i).getCmdId();
				}
				String cmdIdListStr = getListToString(cmdIdArray);
				Assert.fail("The expected command id '" + cmdIdExpected + "' is not found in expect command id list : " + cmdIdListStr);
			}
		}

	}	
	private String getListToString(String[] list) {
		String listStr = "";
		boolean first = true;
		for (String listElt : list) {
			if (first) {
				first = false;
			} else {
				listStr = listStr + ", ";
			}
			listStr = listStr + listElt;
		}
		return listStr;
	}

}
