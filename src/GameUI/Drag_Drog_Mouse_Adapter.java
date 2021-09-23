package GameUI;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;


public class Drag_Drog_Mouse_Adapter implements MouseMotionListener, MouseListener {
	JLabelTransferHandler tHandler;
	boolean drag=false;
	Drag_Drop_Panel panel;
	JLabel drawLabel;
	
	public Drag_Drog_Mouse_Adapter (JLabelTransferHandler tHandler) {
		this.tHandler =tHandler;
		drawLabel=new JLabel ();
		drawLabel.setVisible(false);
		
	}
	 @Override 
	 public void mousePressed(MouseEvent evt) {
		
		if( SwingUtilities.isLeftMouseButton(evt) ) {
		  panel = (Drag_Drop_Panel)evt.getSource();
		    if(getLabel(evt)!=null) {
		      panel.drag_drop_label= getLabel(evt);
		      if(panel.ID.equals("L"))
		    	  	panel.getTransferHandler().exportAsDrag(panel, evt, TransferHandler.MOVE);
		    }
		    else 
		    	 drag=true;
		  } 
	 
	 }
	 
	 @Override
	 public void mouseDragged(MouseEvent evt) {
		 if(panel.drag_drop_label !=null && panel.ID.equals("R")) {
		   panel.drag_drop_label.setBounds(evt.getX(), evt.getY(), panel.drag_drop_label.getWidth(), panel.drag_drop_label.getHeight());
		 }
		 if(drag)
			 panel.repaint();
	 }
	
	@Override 
	public void mouseReleased(MouseEvent evt) {
		drag=false;
	 }
	
	private JLabel getLabel(MouseEvent evt) {
		JLabel ret =null;
		Component comp=null;
		if(panel !=null)
			 comp =SwingUtilities.getDeepestComponentAt(panel, evt.getX(), evt.getY());
		 if(comp!=null && comp instanceof JLabel)
			 ret = (JLabel)comp;
		 return ret;
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent evt) {
		panel = (Drag_Drop_Panel)evt.getSource();
		drawLabel.setVisible(true);
		Component comp=null;
		JLabel temp=null;
		if(panel !=null)
			 comp =SwingUtilities.getDeepestComponentAt(panel, evt.getX(), evt.getY());
		if(comp instanceof JLabel) {
			temp = (JLabel)comp;
			drawLabel.setText(temp.getText());
			drawLabel.setBounds(temp.getX(), temp.getY()+20, 200, 50);
			
			System.err.println("mouse entered eeeeee ee ojadlvjdlkakdn");
			panel.repaint();
		}
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	}
