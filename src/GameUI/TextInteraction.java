package GameUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class TextInteraction extends JFrame {

	protected boolean runWelco;
	JPanel scrollPanel;
	
	

	public TextInteraction(JPanel panel, JScrollPane scroll,JTextArea text, JTextField textField, JLabel score, JLabel value, String welcome, String input,String input2,  boolean runWelco){
		
		panel.setLayout(null);
		
		
		
		runWelco=runWelco;
//		if(runWelco)
//		{
//			text.setText(input);
//		}
//		else{
//			text.setText(welcome);
//			runWelco=true;
//		}
		
		
		scroll.setPreferredSize(new Dimension( 500, 340));
		scrollPanel= new JPanel();
		scrollPanel.setLayout(new FlowLayout());
		scrollPanel.add(scroll);
		scrollPanel.setVisible(true);
		scrollPanel.setBounds(0, 15, 600, 350);
		panel.add(scrollPanel);

		if(score!=null){
			score.setText("Correct Answer : ");
			score.setForeground(Color.RED);
			score.setBounds(20,370,120,30);
			panel.add(score);
		}

		if (value!=null){
			value.setText("0/3");
			value.setForeground(Color.RED);
			value.setBounds(120,370,50,30);
			panel.add(value);
		}

		textField.setBounds(50,360,495,30);
		textField.setText(input2);
		panel.add(textField);
		panel.setVisible(true);
		add(panel);
		setBounds(100, 100, 600, 470);
		setMinimumSize(new Dimension(600, 0));
		setVisible(true);
	//	pack();
		//frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);


	}



}
