package GameUI;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Drag_Drop_Panel extends JPanel{
	public JLabel drag_drop_label;
	public String ID;
	public Drag_Drop_Panel(String ID ) {
		super();
		setLayout(null);
		this.ID=ID;
	}

}
