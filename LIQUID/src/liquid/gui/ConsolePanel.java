package liquid.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.TextArea;

public class ConsolePanel extends Panel {
	
	private TextArea console;
	
	public ConsolePanel(){
		super();
		initComponents();
		setLayout(null);
		setBackground(Color.lightGray);
		setBounds(0,410,500,200);
		setVisible(true);
	}
	
	private void initComponents(){
		Font font = new Font("Verdana", Font.BOLD, 12);
		setFont(font);
		
		Label l = new Label("Console");
		l.setBounds(205,0,90,20);
		add(l);
		
		console = new TextArea(); 
		console.setText ("Welcome to LIQUID!\n");
		console.setEditable(false);
		ScrollPane sc = new ScrollPane();
		sc.setBounds(0, 20, 500, 175);
		sc.add(console);
		add(sc);
	}
	
	public void print_to_Console(String str){
		console.append(str);
	}
}