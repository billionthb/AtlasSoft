package liquid.gui;

import liquid.core.GlobalVar;
import liquid.core.Interfaceable;
import liquid.core.LiquidApplication;
import liquid.engine.LiquidEngine;
import liquid.logger.LiquidLogger;

/**
 * LiquidGUI is the central class of the GUI, where all components of the GUI are initialized
 * here and available for other components to access them when necessary.
 * 
 * <p>This class also interfaces between the Logger and Engine to pass various parameter information.</p>
 */
public class LiquidGUI implements Interfaceable {
	
	// initializes all classes of the GUI
	VariousMessages message;
	LiquidGuiVariables variables;
	LiquidFrame frame;
	LiquidMenuBar menubar;
	SimulationPanel sim;
	ParameterPanel param;
	EnvironmentEditorPanel enviroeditor;
	ConsolePanel console;
	
	/**
	 * Constructor initializes the components of the GUI.
	 */
	public LiquidGUI() {
		initComponents();
	}
	
	/**
	 * Method defines the components of the GUI.
	 */
	private void initComponents() {
		message = new VariousMessages();
		variables = new LiquidGuiVariables();
		frame = new LiquidFrame();
		frame.setJMenuBar(menubar = new LiquidMenuBar());
		frame.add(sim = new SimulationPanel());
		frame.add(param = new ParameterPanel());
		param.add(enviroeditor = new EnvironmentEditorPanel());
		frame.add(console = new ConsolePanel());
		
		// loads the liquid type information into the system
		send(LiquidApplication.getLogger(), GlobalVar.Request.REQUEST_LOAD_CONFIG_FILE);
		frame.setVisible(true);
	}
	
	/**
	 * Method defines requested interactions to the Logger and Engine.
	 * <p>Current Send Interactions:
	 * <br> - REQUEST_LOAD_CONFIG_FILE - sends the Logger a request to load config file information
	 * <br> - REQUEST_LOAD_LOG_PARAM   - sends the Logger a request to load parameters from the log file
	 * <br> - REQUEST_INIT_WRITE_LOG   - sends the Logger a request to initialize writing a log file
	 * <br> - REQUEST_WRITE_LOG_PARAM  - sends the Logger the variables needed to write a log file
	 * <br>
	 * <br> - REQUEST_RUN_SIM   - sends the Engine the variables needed to begin simulation
	 * <br> - REQUEST_PAUSE_SIM - sends the Engine a request to pause the simulation
	 * <br> - REQUEST_STEP_SIM  - sends the Engine a request to step through the simulation by a frame
	 * <br> - REQUEST_END_SIM   - sends the Engine a request to end a simulation
	 */
	@Override
	public void send(Interfaceable i, GlobalVar.Request request) {
		String[] args;
		
		// sends requests to the Logger to begin to load or write a log file
		if (i instanceof LiquidLogger) {
			switch (request) {
			case REQUEST_LOAD_CONFIG_FILE:
				args = new String[1];
				i.receive(this, GlobalVar.Request.LOAD_CONFIG_FILE, args);
				break;
			case REQUEST_LOAD_LOG_PARAM:
				args = new String[1];
				args[0] = variables.filename;
				i.receive(this, GlobalVar.Request.LOAD_LOG_PARAM, args);
				break;
			case REQUEST_INIT_WRITE_LOG:
				args = new String[1];
				args[0] = variables.filename;
				i.receive(this, GlobalVar.Request.INIT_WRITE_LOG, args);
				break;
			case REQUEST_WRITE_LOG_PARAM:
				args = variables.writeArray();
				send(LiquidApplication.getLogger(), GlobalVar.Request.REQUEST_INIT_WRITE_LOG);
				i.receive(this, GlobalVar.Request.WRITE_LOG_PARAM, args);
				break;
			default:}
		}
		
		// sends requests to the Engine to run, pause, or end a simulation
		if (i instanceof LiquidEngine) {
			switch (request) {
			case REQUEST_RUN_SIM:
				args = variables.writeArray();
				i.receive(this, GlobalVar.Request.RUN_SIM, args);
				break;
			case REQUEST_PAUSE_SIM:
				args = new String[0];
				i.receive(this, GlobalVar.Request.PAUSE_SIM, args);
				break;
			case REQUEST_STEP_SIM:
				args = variables.writeArray();
				i.receive(this, GlobalVar.Request.STEP_SIM, args);
				break;
			case REQUEST_END_SIM:
				args = new String[0];
				i.receive(this, GlobalVar.Request.END_SIM, args);
				break;
			default:}
		}
	}
	
	/**
	 * Method defines requested interactions from the Logger and Engine.
	 * <p>Current Receive Interactions:
	 * <br> - SET_CONFIG    - receives information from Logger to set up liquid type information
	 * <br> - SET_LOG_PARAM - receives information from Logger to set up parameters
	 * <br>
	 * <br> - DISPLAY_SIM   - receives particle information from Engine to display
	 * <br> - PRINT_SIM     - receives information from Engine to print onto the console
	 * <br> - SIM_HAS_ENDED - receives information from Engine that simulation has finished
	 */
	@Override
	public void receive(Interfaceable i, GlobalVar.Request request, String[] args) {
		
		// receives information from the Logger to set parameters of the simulator
		if (i instanceof LiquidLogger) {
			switch (request) {
			case SET_CONFIG:
				for (int num = 0; num < args.length; num++) {
					param.liquidInfo.add(args[num]);}
				param.initComponents();
				break;
			case SET_LOG_PARAM:
				variables.readArray(args);
				param.logUpdate();
				enviroeditor.setSelectedObject();
				sim.repaint();
				console.print_to_Console("["+LiquidApplication.getGUI().variables.onlyFileName+" File Loaded.]\n");
				break;
			default:}
		}
		
		// receives information from the Engine to display particles or print information onto the console
		if (i instanceof LiquidEngine) {
			switch (request) {
			case DISPLAY_SIM:
				variables.particles = args;
				sim.repaint();
				break;
			case PRINT_SIM:
				console.print_to_Console(args[0]);
				break;
			case SIM_HAS_ENDED:
				if (variables.simulating) {
					param.run.setEnabled(false);
					param.pause.setEnabled(false);
					param.step.setEnabled(false);
					console.print_to_Console("[Simulation Finished.]\n");}
				break;
			default:}
		}
	}
	
	/**
	 * Method sends a request to the individual GUI components to disable parameters.
	 * @param enable - to enable/disable components
	 */
	public void setEnable(boolean enable) {
		menubar.setEnabled(enable);
		enviroeditor.setEnabled(enable);
		param.setEnabled(enable);
	}
	
	/**
	 * Method resets GUI to initial conditions and parameters.
	 */
	public void reset() {
		variables.reset();
		enviroeditor.reset();
		param.reset();
		frame.setTitle(LiquidApplication.getGUI().variables.onlyFileName+GlobalVar.title);
	}
	
	/**
	 * Represents a placeholder method for when necessary to dispose something.
	 */
	public void dispose() {}
}