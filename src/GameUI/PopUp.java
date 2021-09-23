package GameUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

class PopUp extends MouseAdapter {
	protected JPopupMenu menu;
	public int X=0,Y=0;
	public PopUp (JPopupMenu menu) {
		this.menu=menu;
	}
    public void mousePressed(MouseEvent evt) {
      if (evt.isPopupTrigger()) {
        menu.show(evt.getComponent(), evt.getX(), evt.getY());
        X=evt.getX();
        Y=evt.getY();
      }
    }

    public void mouseReleased(MouseEvent evt) {
      if (evt.isPopupTrigger()) {
        menu.show(evt.getComponent(), evt.getX(), evt.getY());
      }
    }

    public void mouseClicked(MouseEvent evt) {
    }
  }
