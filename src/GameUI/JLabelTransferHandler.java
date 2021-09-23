package GameUI;

import java.awt.Color;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceMotionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;


public class JLabelTransferHandler extends TransferHandler{
	
	 public  DataFlavor localObjectFlavor;
	 
	  public   JLabel label = new JLabel();

	  public   JWindow window;
	  public JLabelTransferHandler(JFrame frame) {
	    System.out.println("LabelTransferHandler");
	    localObjectFlavor = new ActivationDataFlavor(
	    		Drag_Drop_Panel.class, DataFlavor.javaJVMLocalObjectMimeType, "JLabel");
	    window = new JWindow(frame);
	    window.add(label);
	    window.setAlwaysOnTop(true);
	    window.setBackground(new Color(0,true));
	    DragSource.getDefaultDragSource().addDragSourceMotionListener(
	    new DragSourceMotionListener() {
	      @Override public void dragMouseMoved(DragSourceDragEvent dsde) {
	        Point pt = dsde.getLocation();
	        pt.translate(5, 5); 
	        window.setLocation(pt);
	      }
	    });
	  }
	  @Override protected Transferable createTransferable(JComponent c) {
	    System.out.println("createTransferable");
	    Drag_Drop_Panel p = (Drag_Drop_Panel)c;
	    JLabel l = p.drag_drop_label;
	    String text = l.getText();
	     
	    final DataHandler dh = new DataHandler(c, localObjectFlavor.getMimeType());
	    if(text==null) return dh;
	    final StringSelection ss = new StringSelection(text+"\n");
	    return new Transferable() {
	      @Override public DataFlavor[] getTransferDataFlavors() {
	        ArrayList<DataFlavor> list = new ArrayList<>();
	        for(DataFlavor f:ss.getTransferDataFlavors()) {
	          list.add(f);
	        }
	        for(DataFlavor f:dh.getTransferDataFlavors()) {
	          list.add(f);
	        }
	        return list.toArray(dh.getTransferDataFlavors());
	      }
	      public boolean isDataFlavorSupported(DataFlavor flavor) {
	        for (DataFlavor f: getTransferDataFlavors()) {
	          if (flavor.equals(f)) {
	            return true;
	          }
	        }
	        return false;
	      }
	      public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
	        if(flavor.equals(localObjectFlavor)) {
	          return dh.getTransferData(flavor);
	        } else {
	          return ss.getTransferData(flavor);
	        }
	      }
	    };
	  }
	  @Override public boolean canImport(TransferSupport support) {
	    if(!support.isDrop()) {
	      return false;
	    }
	    return true;
	  }
	  @Override public int getSourceActions(JComponent c) {
	    System.out.println("getSourceActions");
	    Drag_Drop_Panel p = (Drag_Drop_Panel)c;
	    label.setIcon(p.drag_drop_label.getIcon());
	    label.setText(p.drag_drop_label.getText());
	    window.pack();
	    Point pt = p.drag_drop_label.getLocation();
	    SwingUtilities.convertPointToScreen(pt, p);
	    window.setLocation(pt);
	    window.setVisible(true);
	    return MOVE;
	  }
	  @Override public boolean importData(TransferSupport support) {
	    System.out.println("importData");
	    if(!canImport(support)) return false;
	    Drag_Drop_Panel target = (Drag_Drop_Panel)support.getComponent();
	    try {
	      Drag_Drop_Panel src = (Drag_Drop_Panel)support.getTransferable().getTransferData(localObjectFlavor);
	      if(src.ID.equals(target.ID))
	    	  	return false;
	      JLabel l = new JLabel();
	   
	      l.setIcon(src.drag_drop_label.getIcon());
	      l.setText(src.drag_drop_label.getText());
	      l.setBounds(50,50, src.drag_drop_label.getWidth(), src.drag_drop_label.getHeight());
	      l.setVisible(true);
	      target.setVisible(true);
	      target.add(l);
	      
	      target.revalidate();
	      target.repaint();
	      return true;
	    } catch(UnsupportedFlavorException ufe) {
	      ufe.printStackTrace();
	    } catch(java.io.IOException ioe) {
	      ioe.printStackTrace();
	    }
	    return false;
	  }
	  @Override protected void exportDone(JComponent c, Transferable data, int action) {
//	    System.out.println("exportDone");
//	    Drag_Drop_Panel src = (Drag_Drop_Panel)c;
//	    if(action == TransferHandler.MOVE) {
//	      src.remove(src.drag_drop_label);
//	      src.revalidate();
//	      src.repaint();
//	    }
//	    src.drag_drop_label = null;
	    window.setVisible(false);
	  }
	  
	}