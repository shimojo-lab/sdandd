package GameUI;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Entity.ItemCard;

public class Drag_Drop extends JFrame  {
	
	// drop list 
	JComboBox selectedOption;
    private JSplitPane splitPanel;
	private Drag_Drop_Panel leftPanel, rightPanel;
	
	private List<DDLabel> cards, network_card;
	private List<DecisionOperation> decision_tree;
	
	//Swing component 
	private List<BufferedImage> images;
	private DDLabel label;
	private DDLabel textArea;
	private DDLabel textField;
	private DDLabel button;
	private JPopupMenu menu;
	private JButton save, compile;
	
	// line connect computer 
	
	MouseListener d_d_mouse_adapter;
	PopUp popup;
	JLabelTransferHandler transfer_handler;
	Component labelComp;
	
	
	
	
	private DomParsersD_D ref_Parser;
	public Drag_Drop(DomParsersD_D ref_Parser) {
		this.ref_Parser=ref_Parser;
		JPanel panel = new JPanel(new GridLayout(1,2));
		menu = new JPopupMenu();
		generate_menu_item();
		popup=new PopUp(menu);
		save =new JButton("Save");
		save.setBounds(270, 500, 100, 40);
		
		compile =new JButton("Compile");
		compile.setBounds(150, 500, 100, 40);
		
		splitPanel= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		transfer_handler = new JLabelTransferHandler(this);
		d_d_mouse_adapter = new Drag_Drog_Mouse_Adapter(transfer_handler);
			
		cards= new ArrayList<DDLabel>();
		network_card = new ArrayList<DDLabel>();
		
        leftPanel = new Drag_Drop_Panel( "L");
        leftPanel.setMinimumSize(new Dimension(300, 500));
        leftPanel.addMouseListener(d_d_mouse_adapter);
        leftPanel.setTransferHandler(transfer_handler);
     
        
        rightPanel = new Drag_Drop_Panel("R");
        rightPanel.setPreferredSize(new Dimension(400, 500));
        rightPanel.addMouseListener(d_d_mouse_adapter);
        rightPanel.setTransferHandler(transfer_handler);
        rightPanel.addMouseMotionListener((MouseMotionListener) d_d_mouse_adapter);
        rightPanel.addMouseListener(popup);
        rightPanel.add(save);
        rightPanel.add(compile);
        
        splitPanel.setRightComponent(rightPanel);
        splitPanel.setLeftComponent(leftPanel);
       
        String [] options = {"", "Network Component", "Resources Card", "Decision Tree", "Swing Component"};
        selectedOption = new  JComboBox(options); 
        selectedOption.setBounds(10, 10, 200, 50);
        selectedOption.setVisible(true);
        leftPanel.add(selectedOption);
        
        selectedOption.addActionListener( new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String option = selectedOption.getSelectedItem().toString();
				switch(option) {
				case "" : 
					break;
				case "Network Component" :create_net_component();
				break;
				case "Resources Card" :create_resources_card();
				break;
				case "Decision Tree" :create_decision_tree();
				break;
				case "Swing Component" :create_swing_component();
				break;
				default:
					break;
				}

			}

        });
        
        		
        panel.add(splitPanel);
      //  panel.add(rightPanel);
        panel.setPreferredSize(new Dimension(700,600));
       
        setTitle("Task SetUp");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().add(panel);
        setVisible(true);
        pack();
        
     	
	}
	
	public Component comp() {
		Component comp=null;
  	    comp =SwingUtilities.getDeepestComponentAt(rightPanel, popup.X, popup.Y);
  	    return comp;
	}
	public void generate_menu_item() {
		
		JMenuItem configure = new JMenuItem("Configure");
		configure.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent evt) {
	    	  labelComp=comp();
		    	  if(labelComp != null) {
		    		  if (labelComp instanceof DDLabel) {
		    			  
		    			 switch(((DDLabel) labelComp).ID) {
		    			 /*
		    			  * network decisioon 
		    			  */
		    			 	case "decision0": make_decision("decision0"); 
		    				break;
		    				case "decision1": make_decision("decision0"); 
		    				break;
		    				case "decision2": make_decision("decision0");
		    				break;
		    			
		    				default :
		    					break ;

		    			 }
		    			  
		    		  }
		    		  
		    	  }
	      }

		
	    });
	    menu.add(configure);
	    

	    JMenuItem delete = new JMenuItem("Delete");
	    delete.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent evt) {
	  		if(comp() != null) {
	  			rightPanel.remove(comp());
	  			rightPanel.repaint();
	  		}
	      }
	    });
	    menu.add(delete);
	}
	private void make_decision(String string) {
		
	}
	
	private void create_swing_component() {
		 clearLabelComponent();
		 label = new DDLabel("label","swing");
		 label.setText("JLabel");
		 label.setBounds(5, 70, 100, 50);
		 
		 textArea = new   DDLabel("textarea","swing");;
		 textArea.setText("JText Area");
		 textArea.setBounds(120,70, 100, 75);
		 
		 textField = new DDLabel("textfield","swing");
		 textField.setText("JText Field");
		 textField.setBounds(5, 180, 100, 50);
		 
		 button =new DDLabel("button","swing");
		 button.setText("JButton");
		 button.setBounds(120, 180, 100, 50);
		 setVisibility(true);
		 
		 DDLabel img =new DDLabel("img","swing");
		 img.setText("Image Bg");
		 img.setBounds(5, 290, 100, 50);
		 setVisibility(true);
		 
		 leftPanel.add(label);
		 leftPanel.add(textArea);
		 leftPanel.add(textField);
		 leftPanel.add(button);
		 leftPanel.add(img); 
		 this.getContentPane().repaint();
		
		
	}

	private void setVisibility(boolean aFlag) {
		label.setVisible(aFlag);
		textArea.setVisible(aFlag);
		textField.setVisible(aFlag);
		button.setVisible(aFlag);
	}
	private void create_decision_tree() {
		clearLabelComponent();
		int number_item_per_line =0;
		int x_off_set=140;
		int	y_off_set=70;
		int x=10;
		int y=50;
		
		for (int i=0;i<3;i++) {
			DDLabel decision = new DDLabel("decision"+i,"i");
			ImageIcon icon=new ImageIcon(this.createBuffredImage("/decision"+i+".png"));
			decision.setIcon(icon);
			leftPanel.add(decision);
			
			if(number_item_per_line ==0) 
				decision.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
			else if(number_item_per_line == 2) {
				y=y+y_off_set;
				x=10;
				decision.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
				number_item_per_line=0;
				
			}
			else {
				x=x+x_off_set;
				decision.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
				
			}
			
			leftPanel.repaint();
			
			number_item_per_line++;
		}
	}
	
	private void clearLabelComponent() {
		for (Component comp : leftPanel.getComponents()) {
			if( comp instanceof DDLabel)
				leftPanel.remove(comp);
		}
	}

	private void create_resources_card() {
		// only remove label different from it own label 
		
		clearLabelComponent();
		aux_create_resources_comp(false);
		
		
	}

	private void create_net_component() {
		clearLabelComponent();
		aux_create_resources_comp(true);
		
	
	}
	public  BufferedImage createBuffredImage(String path) {
		java.net.URL imgURL = ItemCard.class.getResource(path);
		
		if (imgURL != null) {
			 try {
					return  ImageIO.read(new File(imgURL.getPath()));
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		} else {
			System.err.println("path not found );" + path);
	
		}
		return null;
	}
	
	
	private void aux_create_resources_comp(boolean net_card) {
		int number_item_per_line =0;
		int x_off_set=140;
		int	y_off_set=70;
		int x=10;
		int y=50;
		String op;
		
		List<ItemCard> items ;
		if (net_card) {
			items=ref_Parser.net_card;
			op="net";
		}
		else {
			items=ref_Parser.resources_card;
			op="asset";
		}
		
		for (int i=0; i< items.size();i++) {
			System.err.println(items.get(i).cardId);
		
				DDLabel temp = new DDLabel (items.get(i).cardId, op);
				ImageIcon icon=new ImageIcon(items.get(i).getBackgroundImg());
				temp.setIcon(icon);
				temp.setText(items.get(i).cardId);
				//temp.setText(items.get(i).cardId);
				if(number_item_per_line ==0) 
					temp.setBounds(x, y, items.get(i).WBG+50, items.get(i).HBG);
				else if(number_item_per_line == 2) {
					y=y+y_off_set;
					x=10;
					temp.setBounds(x, y, items.get(i).WBG+50, items.get(i).HBG);
					number_item_per_line=0;
					
				}
				else {
					x=x+x_off_set;
					temp.setBounds(x, y, items.get(i).WBG+50, items.get(i).HBG);
					
				}
				leftPanel.add(temp);
				leftPanel.repaint();
				
				number_item_per_line++;
				
		}
		
	}
	
	
	public static void main (String [] args) {
		
		DomParsersD_D parser = new DomParsersD_D(null);
		Drag_Drop run_local = new Drag_Drop(parser);
		
	}
	
	private class DDLabel extends JLabel{
		String ID, description;
		
		public DDLabel(String ID, String description) {
			super();
			this.ID=ID;
			this.description=description;
		}
		
	}

}
