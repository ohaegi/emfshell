package fr.obeo.emf.shell.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

public class ModelHelper {
	
	public static Resource loadResourceModel(String fileName) throws FileNotFoundException, IOException {
		  XMIResourceImpl resource = new XMIResourceImpl();
		  File source = new File(fileName);
		  resource.load( new FileInputStream(source), new HashMap<Object,Object>());
		  return resource;
		}

}
