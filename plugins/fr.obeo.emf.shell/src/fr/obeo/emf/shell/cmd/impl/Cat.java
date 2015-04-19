package fr.obeo.emf.shell.cmd.impl;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl.ContainmentUpdatingFeatureMapEntry;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil.FeatureEList;

import fr.obeo.emf.shell.EmfShell;
import fr.obeo.emf.shell.ICmd;
import fr.obeo.emf.shell.cmd.Cmd;

public class Cat extends Cmd implements ICmd {
	
	@Override
	public String getCmdId() {
		return "cat";
	}
	
	public boolean isCmdCandidate(String cmdStr){
		return super.isCmdCandidate(cmdStr, getCmdId());
	}

	@Override
	public String eval(EObject eObject, EmfShell emfConsole) {
		String result = print(eObject);
		result = result + "\n  Long name : " + eObject.toString().replaceAll("\\(.*\\)", "");
		
		result = result + "\n  Attirbutes : ";
		EList<EAttribute> eAllAttributes = eObject.eClass().getEAllAttributes();
	    for (EAttribute eAttribute : eAllAttributes) {
	    	if (eObject.eGet(eAttribute) != null) {
	    		Object attr = eObject.eGet(eAttribute);
	    		if (attr instanceof EObject) {
	    			result = result + "\n    " + print((EObject)attr);
				} else if (attr instanceof BasicFeatureMap) {
					result = result + "\n    " + eAttribute.getName() + " : ";
					BasicFeatureMap b = (BasicFeatureMap)attr;
					for (Object element : b) {
						if (element instanceof ContainmentUpdatingFeatureMapEntry) {
							EObject myEObj = (EObject)((ContainmentUpdatingFeatureMapEntry)element).getValue();
							result = result +  "\n      " + print(myEObj);
						} else {
							result = result +  "\n      " + element.toString();
						}
					}
				} else {
					result = result + "\n    " + eAttribute.getName() + " : " + eObject.eGet(eAttribute).toString();
				}
			} else {
				result = result + "\n    " + eAttribute.getName() + " : null";
			}
	    }
	    result = result + "\n  Reference : ";
	    EList<EReference> eAllReferences = eObject.eClass().getEAllReferences();
	    for (EReference eReference: eAllReferences) {
	    	if (eObject.eGet(eReference) != null) {
	    		Object ref = eObject.eGet(eReference);
	    		if (ref instanceof EObject) {
	    			result = result + "\n    " + print((EObject)ref);
	    		} else if (ref instanceof FeatureEList) {
					result = result + "\n    " + eReference.getName() + " : ";
					@SuppressWarnings("rawtypes")
					FeatureEList f = (FeatureEList)ref;
					for (Object element : f) {
						if (element instanceof EObject) {
							result = result +  "\n      " + print((EObject)element);
						} else {
							result = result +  "\n      " + element.toString();
						}
					}
				} 
	    		
	    	}
	    }
		return result;
	}
	
	@Override
	public String help() {
		String help = "cat .\n";
		help = help + "  print current node in detail.";
		return help;
	}

	@Override
	public List<String> getCompletionList(EObject currentNode) {
		return null;
	}
}
