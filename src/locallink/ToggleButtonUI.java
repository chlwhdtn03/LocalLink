package locallink;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.plaf.basic.BasicButtonUI;

public class ToggleButtonUI extends BasicButtonUI {
	
	@Override
	public void paint(Graphics g, JComponent c) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, 50, 20);
		if(((JToggleButton) c).isSelected()) {
			g.setColor(Color.green);
			g.fillRect(0, 0, 20, 20);
		} else {
			g.setColor(Color.red);
			g.fillRect(30, 0, 20, 20);
		}
		super.paint(g, c);
	}

}
