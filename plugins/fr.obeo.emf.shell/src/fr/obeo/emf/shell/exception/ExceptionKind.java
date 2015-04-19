package fr.obeo.emf.shell.exception;

public enum ExceptionKind {
	NOT_SUPPORTED_CMD("Error : the command '%s' isn't supported"),
	CANNOT_FIND_EOBJECT("Error : Unable to find EObject %s"), 
	;
	
	private String message;
	
	private ExceptionKind(String message) {
		this.message = message;
	}
	
	public int getCode() {
		return this.ordinal() + 1;
	}
	
	public String getMessage() {
		return this.message;
	}
}
