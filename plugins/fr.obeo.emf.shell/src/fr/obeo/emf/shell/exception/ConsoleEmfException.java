package fr.obeo.emf.shell.exception;

/**
 * Abap Quality Engine Exception.
 * 
 * @author <a href="mailto:olivier.haegi@obeo.fr">Olivier Haegi</a>
 * 
 */
public class ConsoleEmfException extends Exception {

	private static final long serialVersionUID = 6054708598875370290L;

	/**
	 * Exception kind.
	 */
	private ExceptionKind kind;

	/**
	 * Create class.
	 * 
	 * @param kind
	 *            exception kind
	 */
	public ConsoleEmfException(ExceptionKind kind) {
		super(kind.getMessage());
		this.kind = kind;
	}

	/**
	 * Create class with cause.
	 * 
	 * @param kind
	 *            exception kind
	 * @param cause
	 *            cause
	 */
	public ConsoleEmfException(ExceptionKind kind, Throwable cause) {
		super(kind.getMessage(), cause);
		this.kind = kind;
	}

	/**
	 * Create class with custom value.
	 * 
	 * @param kind
	 *            exception kind
	 * @param value
	 *            custom value
	 */
	public ConsoleEmfException(ExceptionKind kind, Object... args) {
		super(String.format(kind.getMessage(), args));
		// init cause if last args is an Throwable type
		if (args[args.length -1] instanceof Throwable) {
			this.initCause((Throwable)args[args.length -1]);
		}
		this.kind = kind;
	}

	/**
	 * Get code
	 * 
	 * @return code
	 */
	public int getCode() {
		return kind.getCode();
	}

}
