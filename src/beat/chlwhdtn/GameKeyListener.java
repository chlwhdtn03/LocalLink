package beat.chlwhdtn;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameKeyListener extends KeyAdapter {
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (JavaBeat.game == null) {
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			JavaBeat.game.pressS();
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			JavaBeat.game.pressD();
		} else if (e.getKeyCode() == KeyEvent.VK_F) {
			JavaBeat.game.pressF();
		} else if (e.getKeyCode() == KeyEvent.VK_J) {
			JavaBeat.game.pressJ();
		} else if (e.getKeyCode() == KeyEvent.VK_K) {
			JavaBeat.game.pressK();
		} else if (e.getKeyCode() == KeyEvent.VK_L) {
			JavaBeat.game.pressL();
		}
		System.out.println("new Beat(" + JavaBeat.game.gameMusic.getTime() + " - startTime - gap, \""
				+ (new String((new StringBuilder(String.valueOf(e.getKeyChar()))).toString())).toUpperCase() + "\"),");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (JavaBeat.game == null) {
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			JavaBeat.game.releaseS();
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			JavaBeat.game.releaseD();
		} else if (e.getKeyCode() == KeyEvent.VK_F) {
			JavaBeat.game.releaseF();
		} else if (e.getKeyCode() == KeyEvent.VK_J) {
			JavaBeat.game.releaseJ();
		} else if (e.getKeyCode() == KeyEvent.VK_K) {
			JavaBeat.game.releaseK();
		} else if (e.getKeyCode() == KeyEvent.VK_L) {
			JavaBeat.game.releaseL();
		}
	}
}
