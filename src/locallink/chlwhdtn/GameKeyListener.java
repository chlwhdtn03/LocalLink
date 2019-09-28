package locallink.chlwhdtn;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameKeyListener extends KeyAdapter {
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (LocalLink.game == null) {
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			LocalLink.game.pressS();
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			LocalLink.game.pressD();
		} else if (e.getKeyCode() == KeyEvent.VK_F) {
			LocalLink.game.pressF();
		} else if (e.getKeyCode() == KeyEvent.VK_J) {
			LocalLink.game.pressJ();
		} else if (e.getKeyCode() == KeyEvent.VK_K) {
			LocalLink.game.pressK();
		} else if (e.getKeyCode() == KeyEvent.VK_L) {
			LocalLink.game.pressL();
		}
		System.out.println("new Beat(" + LocalLink.game.gameMusic.getTime() + " - startTime - gap, \""
				+ (new String((new StringBuilder(String.valueOf(e.getKeyChar()))).toString())).toUpperCase() + "\"),");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (LocalLink.game == null) {
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			LocalLink.game.releaseS();
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			LocalLink.game.releaseD();
		} else if (e.getKeyCode() == KeyEvent.VK_F) {
			LocalLink.game.releaseF();
		} else if (e.getKeyCode() == KeyEvent.VK_J) {
			LocalLink.game.releaseJ();
		} else if (e.getKeyCode() == KeyEvent.VK_K) {
			LocalLink.game.releaseK();
		} else if (e.getKeyCode() == KeyEvent.VK_L) {
			LocalLink.game.releaseL();
		}
	}
}
