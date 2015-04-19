package fr.obeo.emf.shell.headless;

import java.util.Map;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class ShellApplication implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		final Map<?, ?> args = context.getArguments();
		final String[] appArgs = (String[]) args.get("application.args");
		if (appArgs.length == 0) {
			System.err.println("The application should have a first attribute with model path.");
			return null;
		}
		
		String modelPath = appArgs[0];
		ShellLoop shellLoop = new ShellLoop();
		shellLoop.run(ModelHelper.loadResourceModel(modelPath));
		
		return null;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
	}

}
