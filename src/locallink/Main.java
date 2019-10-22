package locallink;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import locallink.Account.AccountManager;
import locallink.chlwhdtn.LocalLink;

public class Main {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int NOTE_SPEED = 3;
	public static final int SLEEP_TIME = 10;
	public static final int REACH_TIME = 2;
	public static long MEMORY_USAGE = 0;

	public static void main(String[] args) {
		AccountManager.loadUser();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				AccountManager.saveUser();
			}
		}));
		Thread utilThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {		
					while(true) {
						MEMORY_USAGE = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
						Delay.sleep(3000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		utilThread.setName("유틸 쓰레드");
		utilThread.start();
		new LocalLink();
	}
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
}
