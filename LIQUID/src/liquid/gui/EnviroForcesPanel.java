package liquid.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import liquid.core.LiquidApplication;

/**
 * Class is a branch of the EnvironmentEditorPanel. Here, all elements linked
 * to creating a force are present, such as the force in the X/Y directions. 
 */
public class EnviroForcesPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	// list of components needed to create a force
	JComboBox<String> forceType;
	JComboBox<Float> forceX;
	JComboBox<Float> forceY;
	JComboBox<Float> forceXComp;
	JComboBox<Float> forceYComp;
	String[] foType = {"Source"};
	
	/**
	 * Constructor creates the Force (or Initial Force) section of the EnvironmentEditorPanel.
	 */
	public EnviroForcesPanel() {
		initComponents();
	}
	
	/**
	 * Method creates the labels and drop-downs associated with creating forces.
	 */
	public void initComponents() {
		setBounds(5,30,240,175);
		setBackground(Color.LIGHT_GRAY);
		setLayout(null);
		
		// makes labels specific to creating forces
		JLabel l = new JLabel("Object Type:");
		l.setBounds(35,5,(this.getWidth()/2),25);
		add(l);
		
		l = new JLabel("X-Coordinate:");
		l.setBounds(5,30,(this.getWidth()/2),25);
		add(l);
		
		l = new JLabel("Y-Coordinate:");
		l.setBounds(125,30,(this.getWidth()/2),25);
		add(l);
		
		l = new JLabel("X-Force:");
		l.setBounds(5,80,(this.getWidth()/2),25);
		add(l);
		
		l = new JLabel("Y-Force:");
		l.setBounds(125,80,(this.getWidth()/2),25);
		add(l);
		
		// makes the drop-downs needed to create forces
		forceType = new JComboBox<String>(foType);
		forceType.setBounds(115,5,(this.getWidth()/2),25);
		add(forceType);
			
		forceX = new JComboBox<Float>();
		forceY = new JComboBox<Float>();
		forceXComp = new JComboBox<Float>();
		forceYComp = new JComboBox<Float>();
		forcesParam(); // populates the drop-down information
		createForces(); // makes a Create button
	}
	
	/**
	 * Method adjusts the force's settings to be within the limit of the environment's size. It also provides
	 * a real-time update of the force parameters to prevent them from exceeding the environment's boundaries.
	 */
	public void forcesParam() {
		// each drop-down first gets all items removed from it, then gets
		// populated with items all dependent on the environment boundaries
		forceX.removeAllItems();
		for (int i = 0; i <= EnvironmentEditorPanel.enviroLenLimit; i++) {
			forceX.addItem(Float.valueOf(i));}
		forceX.setBounds(5,55,(int)(this.getWidth()/2.2),25);
		add(forceX);
		
		forceY.removeAllItems();
		for (int i = 0; i <= EnvironmentEditorPanel.enviroWidLimit; i++) {
			forceY.addItem(Float.valueOf(i));}
		forceY.setBounds(125,55,(int)(this.getWidth()/2.2),25);
		add(forceY);
		
		forceXComp.removeAllItems();
		for (int i = 0; i <= EnvironmentEditorPanel.enviroLenLimit; i++) {
			forceXComp.addItem(Float.valueOf(i));}
		forceXComp.setBounds(5,105,(int)(this.getWidth()/2.2),25);
		add(forceXComp);
		
		forceYComp.removeAllItems();
		for (int i = 0; i <= EnvironmentEditorPanel.enviroWidLimit; i++) {
			forceYComp.addItem(Float.valueOf(i));}
		forceYComp.setBounds(125,105,(int)(this.getWidth()/2.2),25);
		add(forceYComp);
		
		// sets the default parameters even after an environment size change
		resetForces();
	}
	
	/**
	 * Method used to call the editor panel to make a Create button for Forces.
	 */
	public void createForces() {
		// button creates the obstacle according to the parameters set
		JButton create = new JButton("Create");
		create.setBounds(65,140,(int)(this.getWidth()/2.2),25);
		create.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent actionEvent) {
			LiquidApplication.getGUI().enviroeditor.checkBoundaries(forceType, forceX, forceY, forceXComp, forceYComp);
			}
		});
		add(create);
	}
	
	/**
	 * Method splits up the String[] of the log file in order to correctly set the
	 * parameters of the Forces (or Initial Forces) section of the EnvironmentEditorPanel.
	 * 
	 * @param tokens - String[] of the log file to split
	 */
	public void updateForces(String[] tokens) {
		try {
			forceType.setSelectedItem(tokens[0]);
			forceX.setSelectedItem(tokens[1]);
			forceY.setSelectedItem(tokens[2]);
			forceXComp.setSelectedItem(tokens[3]);
			forceYComp.setSelectedItem(tokens[4]);
		} catch (Exception e) {
			e.printStackTrace();}
	}
	
	/**
	 * Method resets the parameters of the Forces section.
	 */
	public void resetForces() {
		forceType.setSelectedIndex(0);
		forceX.setSelectedIndex(0);
		forceY.setSelectedIndex(0);
		forceXComp.setSelectedIndex((int) (EnvironmentEditorPanel.enviroLenLimit/10));
		forceYComp.setSelectedIndex((int) (EnvironmentEditorPanel.enviroWidLimit/8));
	}
}