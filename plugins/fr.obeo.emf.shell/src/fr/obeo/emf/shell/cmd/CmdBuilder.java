package fr.obeo.emf.shell.cmd;

import java.util.ArrayList;
import java.util.List;

import fr.obeo.emf.shell.ICmd;
import fr.obeo.emf.shell.cmd.impl.Cat;
import fr.obeo.emf.shell.cmd.impl.Cd;
import fr.obeo.emf.shell.cmd.impl.Help;
import fr.obeo.emf.shell.cmd.impl.Ls;
import fr.obeo.emf.shell.cmd.impl.Print;
import fr.obeo.emf.shell.cmd.impl.Pwd;
import fr.obeo.emf.shell.exception.ConsoleEmfException;
import fr.obeo.emf.shell.exception.ExceptionKind;


public class CmdBuilder {
	
	public static List<ICmd> LIST_CMD = new ArrayList<ICmd>();
	static {
		LIST_CMD.add(new Ls());
		LIST_CMD.add(new Cd());
		LIST_CMD.add(new Print());
		LIST_CMD.add(new Cat());
		LIST_CMD.add(new Pwd());
		LIST_CMD.add(new Help());
	}
	
	public static ICmd getCommand(String cmdStr) throws ConsoleEmfException {
		for (ICmd cmd : LIST_CMD) {
			if (cmd.isCmdCandidate(cmdStr)) {
				if (cmd instanceof Cmd) {
					((Cmd)cmd).setArgs(cmdStr);
					return cmd;
				}
			}
		}
		throw new ConsoleEmfException(
				ExceptionKind.NOT_SUPPORTED_CMD,cmdStr);
	}
	
	public static String[] getCmdIdArray(){
		String[] result = new String[LIST_CMD.size()];
		int index = 0;
		for (ICmd cmd : LIST_CMD) {
			result[index] = cmd.getCmdId();
			index++;
		}
		return result;
	}
}
