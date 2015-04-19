package fr.obeo.emf.shell.headless;

import java.io.IOException;
import java.util.Scanner;

import org.eclipse.emf.ecore.resource.Resource;

import fr.obeo.emf.shell.EmfShell;

public class ShellLoop {

	public void run(Resource chosenResource) throws IOException {
		EmfShell emfShell = new EmfShell();
		emfShell.setCurrentResource(chosenResource);
		System.out.print(emfShell.getPrompt());
		Scanner in = new Scanner(System.in);		
		String cmdLine = "";
		while ((cmdLine = in.nextLine()) != null) {
			if ("q".equalsIgnoreCase(cmdLine.trim())) {
				break;
			} else if ("".equalsIgnoreCase(cmdLine.trim())) {
			} else {
				emfShell.exec(cmdLine);
				System.out.println(emfShell.getMessage());
			}
			System.out.print(emfShell.getPrompt());
		}
		in.close();
	}
}
