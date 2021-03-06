package liquid.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import liquid.core.GlobalVariables.ObjectType;
import liquid.core.LiquidApplication;

/**
 * Class is a branch of the EnvironmentEditorPanel. Here, all elements linked
 * to creating an obstacle or drain are present, such as the X-/Y-Coordinates.
 */
public class EnviroObstaclesAndDrainsPanel extends JPanel {
	
	// list of components needed to create an obstacle or drain
	private static final long serialVersionUID = 1L;
	float origLength;
	float origWidth;
	boolean actualChange = true;
	
	JComboBox<String> obstacleType;
	JComboBox<Float> obstacleRotation;
	JComboBox<Float> obstacleX;
	JComboBox<Float> obstacleY;
	JComboBox<Float> obstacleL;
	JComboBox<Float> obstacleW;
	ArrayList<Float> params;
	
	/**
	 * Constructor creates the Obstacles and Drains section of the EnvironmentEditorPanel.
	 */
	public EnviroObstaclesAndDrainsPanel() {
		initComponents();
	}
	
	/**
	 * Method creates the labels and drop-downs associated with creating obstacles and drains.
	 */
	public void initComponents() {
		setBounds(5,30,240,205);
		setBackground(Color.LIGHT_GRAY);
		setLayout(null);
		
		// makes labels specific to creating obstacles and drains
		JLabel l = new JLabel("Object Type:");
		l.setBounds(5,5,(this.getWidth()/2),25);
		add(l);
		
		l = new JLabel("Rotation (Degrees):");
		l.setBounds(125,5,(this.getWidth()/2),25);
		add(l);
		
		l = new JLabel("X-Coordinate:");
		l.setBounds(5,60,(this.getWidth()/2),25);
		add(l);
		
		l = new JLabel("Y-Coordinate:");
		l.setBounds(125,60,(this.getWidth()/2),25);
		add(l);
		
		l = new JLabel("Length:");
		l.setBounds(5,110,(this.getWidth()/2),25);
		add(l);
		
		l = new JLabel("Width:");
		l.setBounds(125,110,(this.getWidth()/2),25);
		add(l);
		
		// adds the obstacle/drain types to the drop-down list
		obstacleType = new JComboBox<String>();
		obstacleType.addItem(ObjectType.Rectangle.toString());
		obstacleType.addItem(ObjectType.Circle.toString());
		obstacleType.addItem(ObjectType.Rectangle_Drain.toString());
		obstacleType.addItem(ObjectType.Circle_Drain.toString());
		obstacleType.setBounds(5,30,(int)(this.getWidth()/2.2),25);
		obstacleType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				// sets the rotation drop-down option to be disabled for drains 
				if (arg0.getItem().toString().equals(ObjectType.Rectangle.toString()) ||
						arg0.getItem().toString().equals(ObjectType.Circle.toString())) {
					obstacleRotation.setEnabled(true);
				} else if (arg0.getItem().toString().equals(ObjectType.Rectangle_Drain.toString()) ||
						arg0.getItem().equals(ObjectType.Circle_Drain.toString())) {
					obstacleRotation.setEnabled(false);}
			}
        });
		add(obstacleType);
		
		obstacleRotation = new JComboBox<Float>();
		obstacleRotation.removeAllItems();
		for (int i = 0; i <= 360; i++) {
			obstacleRotation.addItem(Float.valueOf(i));}
		obstacleRotation.setBounds(125,30,(int)(this.getWidth()/2.2),25);
		add(obstacleRotation);
		
		// populates the drop-down information for the X/Y/Length/Width
		obstacleX = new JComboBox<Float>();
		obstacleY = new JComboBox<Float>();
		obstacleL = new JComboBox<Float>();
		obstacleW = new JComboBox<Float>();
		
		actualChange = false;
		xYParam(true);
		lenWidParam(true, true, 0);
		actualChange = true;
		createButton(); // makes a Create button
		resetObstacles();
	}
	
	/**
	 * Method adjusts the obstacle/drain's X-/Y-Coordinate to be within the environment's size. It also provides
	 * a real-time update of the obstacle parameters to prevent them from exceeding the environment's boundaries.
	 * @param enviroChange - determines when the environment size changes
	 */
	public void xYParam(boolean enviroChange) {
		// drop-down is newly populated due to an environment size change
		obstacleX.setModel(new DefaultComboBoxModel<>());
		for (int i = 0; i <= EditorPanel.enviroLenLimit; i++) {
			obstacleX.addItem(Float.valueOf(i));}
		obstacleX.setBounds(5,85,(int)(this.getWidth()/2.2),25);
		
		// used to adjust the Length drop-down to be within the environment size
		obstacleX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionevent) {
				if (!enviroChange || actualChange) {
					origLength = (float)obstacleL.getSelectedItem();
					float num = (float)obstacleX.getSelectedItem();
					lenWidParam(false, true, num);}
			}
		});
		add(obstacleX);
		
		// drop-down is newly populated due to an environment size change
		obstacleY.setModel(new DefaultComboBoxModel<>());
		for (int i = 0; i <= EditorPanel.enviroWidLimit; i++) {
			obstacleY.addItem(Float.valueOf(i));}
		obstacleY.setBounds(125,85,(int)(this.getWidth()/2.2),25);
		
		// used to adjust the Width drop-down to be within the environment size
		obstacleY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionevent) {
				if (!enviroChange || actualChange) {
					origWidth = (float)obstacleW.getSelectedItem();
					float num = (float)obstacleY.getSelectedItem();
					lenWidParam(false, false, num);}}
		});
		add(obstacleY);
	}
	
	/**
	 * Method adjusts the obstacle/drain's Length/Width to be within the environment's size. It also provides
	 * a real-time update of the obstacle parameters to prevent them from exceeding the environment's boundaries.
	 * @param enviroChange - determines when the environment size changes
	 * @param length       - determines whether to change the Length or Width
	 * @param limit        - the limit for the Length or Width
	 */
	public void lenWidParam(boolean enviroChange, boolean length, float limit) {
		if (enviroChange || length) {
			// drop-down is newly populated due to either an environment size change or a new X-Coordinate chosen
			obstacleL.removeAllItems();
			for (int i = 0; i <= (EditorPanel.enviroLenLimit-limit); i++) {
				obstacleL.addItem(Float.valueOf(i));}
			if (origLength <= (EditorPanel.enviroLenLimit-limit))
				obstacleL.setSelectedIndex((int)origLength);
			else
				obstacleL.setSelectedIndex((int)(EditorPanel.enviroLenLimit-limit));
			obstacleL.setBounds(5,135,(int)(this.getWidth()/2.2),25);
			add(obstacleL);}
		
		if (enviroChange || !length) {
			// drop-down is newly populated due to either an environment size change or a new Y-Coordinate chosen
			obstacleW.removeAllItems();
			for (int i = 0; i <= (EditorPanel.enviroWidLimit-limit); i++) {
				obstacleW.addItem(Float.valueOf(i));}
			if (origWidth <= (EditorPanel.enviroWidLimit-limit))
				obstacleW.setSelectedIndex((int)origWidth);
			else
				obstacleW.setSelectedIndex((int)(EditorPanel.enviroWidLimit-limit));
			obstacleW.setBounds(125,135,(int)(this.getWidth()/2.2),25);
			add(obstacleW);}
	}
	
	/**
	 * Method used to call the editor panel to make a Create button for obstacles.
	 */
	public void createButton() {
		// button creates the obstacle/drain according to the parameters set
		JButton create = new JButton("Create");
		create.setBounds(65,170,(int)(this.getWidth()/2.2),25);
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				createObstacle(false);
			}
		});
		add(create);
	}
	
	/**
	 * Method packages data and sends it to the editor to be added into the object list.
	 * @param update - either adds a new object or updates an object's parameters
	 */
	public void createObstacle(boolean update) {
		params = new ArrayList<Float>(5);
		params.add((Float)obstacleX.getSelectedItem());
		params.add((Float)obstacleY.getSelectedItem());
		params.add((Float)obstacleL.getSelectedItem());
		params.add((Float)obstacleW.getSelectedItem());
		params.add((Float)obstacleRotation.getSelectedItem());
		LiquidApplication.getGUI().getEditorPanel().addObject(obstacleType, params, update);
	}
	
	/**
	 * Method splits up the String[] of the log file in order to correctly set the
	 * parameters of the Obstacles and Drains section of the EnvironmentEditorPanel.
	 * @param tokens - String[] of the log file to split
	 */
	public void updateObstacles(String[] tokens) {
		try {
			obstacleType.setSelectedItem(tokens[0]);
			obstacleX.setSelectedItem(Float.parseFloat(tokens[1]));
			obstacleY.setSelectedItem(Float.parseFloat(tokens[2]));
			obstacleL.setSelectedItem(Float.parseFloat(tokens[3]));
			obstacleW.setSelectedItem(Float.parseFloat(tokens[4]));
			obstacleRotation.setSelectedItem(Float.parseFloat(tokens[5]));
		} catch (Exception e) {
			e.printStackTrace();}
	}
	
	/**
	 * Method resets the parameters of the Obstacles and Drains section.
	 */
	public void resetObstacles() {
		origLength = 50;
		origWidth = 50;
		obstacleType.setSelectedIndex(0);
		obstacleRotation.setSelectedIndex(0);
		obstacleX.setSelectedIndex(0);
		obstacleY.setSelectedIndex(0);
		obstacleL.setSelectedIndex((int)(EditorPanel.enviroLenLimit/10));
		obstacleW.setSelectedIndex((int)(EditorPanel.enviroWidLimit/8));
	}
}