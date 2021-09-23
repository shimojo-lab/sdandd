package GameUI;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class MMKeyBinding {

	private InputMap inputMap;
	private ActionMap actionMap;

	public MMKeyBinding(JComponent comp, int keyCode, String id, ActionListener actionListener){
		
		
			inputMap = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
			actionMap = comp.getActionMap();
			inputMap.put(KeyStroke.getKeyStroke(keyCode, 0), id);
			actionMap.put(id, new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					actionListener.actionPerformed(e);
				}
			});

			

		}



}
