package beat.chlwhdtn;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import beat.Main;

public class Game extends Thread {

	private Image gameInfoImage = new ImageIcon(Main.class.getResource("../images/gameInfo.png")).getImage();
	private Image judgementLineImage = new ImageIcon(Main.class.getResource("../images/judgementLine.png")).getImage();
	private Image noteRouteLineImage = new ImageIcon(Main.class.getResource("../images/noteRouteLine.png")).getImage();
	private Image noteRouteSImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	private Image noteRouteDImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	private Image noteRouteFImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	private Image noteRouteJImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	private Image noteRouteKImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	private Image noteRouteLImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();

	public int combo;
	private String title;
	private String musicTitle;
	public Music gameMusic;

	public String judge = "Ready";
	ArrayList<Note> notelist = new ArrayList<Note>();

	public Game(String title, String musicTitle) {
		this.title = title;
		combo = 0;
		this.musicTitle = musicTitle;
		gameMusic = new Music(this.musicTitle, false);

	}

	public void screenDraw(Graphics2D g) {
		g.drawImage(noteRouteSImage, 80, 30, null);
		g.drawImage(noteRouteDImage, 184, 30, null);
		g.drawImage(noteRouteFImage, 288, 30, null);
		g.drawImage(noteRouteJImage, 438, 30, null);
		g.drawImage(noteRouteKImage, 542, 30, null);
		g.drawImage(noteRouteLImage, 646, 30, null);
		g.drawImage(noteRouteLineImage, 76, 30, null);
		g.drawImage(noteRouteLineImage, 180, 30, null);
		g.drawImage(noteRouteLineImage, 284, 30, null);
		g.drawImage(noteRouteLineImage, 388, 30, null);
		g.drawImage(noteRouteLineImage, 434, 30, null);
		g.drawImage(noteRouteLineImage, 538, 30, null);
		g.drawImage(noteRouteLineImage, 642, 30, null);
		g.drawImage(noteRouteLineImage, 746, 30, null);
		g.drawImage(gameInfoImage, 0, 550, null);
		g.drawImage(judgementLineImage, 0, 450, null);
		for (int i = 0; i < notelist.size(); i++) {
			Note note = notelist.get(i);
			if (!note.proceeded) {
				notelist.remove(i);
			} else {
				note.screenDraw(g);
			}
		}
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		g.drawString("S", 120, 480);
		g.drawString("D", 224, 480);
		g.drawString("F", 328, 480);
		g.drawString("J", 478, 480);
		g.drawString("K", 582, 480);
		g.drawString("L", 686, 480);
		g.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
		g.setColor(Color.white);
		g.drawString(title, 20, 585);
		g.setFont(new Font("맑은 고딕", Font.BOLD, 50));
		switch (judge) {
		case "MISS!":
		case "EARLY!":
			g.setColor(Color.red);
			break;
		case "GOOD!":
			g.setColor(Color.gray);
			break;
		case "PERFECT!":
			g.setColor(Color.darkGray);

		}
		g.drawString(judge, 350, 200);
		g.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		g.drawString("x" + combo, 350, 250);
	}

	public void pressS() {
		judge("S");
		noteRouteSImage = new ImageIcon(Main.class.getResource("../images/noteRoutePressed.png")).getImage();
	}

	public void releaseS() {
		noteRouteSImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	}

	public void pressD() {
		judge("D");
		noteRouteDImage = new ImageIcon(Main.class.getResource("../images/noteRoutePressed.png")).getImage();
	}

	public void releaseD() {
		noteRouteDImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	}

	public void pressF() {
		judge("F");
		noteRouteFImage = new ImageIcon(Main.class.getResource("../images/noteRoutePressed.png")).getImage();
	}

	public void releaseF() {
		noteRouteFImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	}

	public void pressJ() {
		judge("J");
		noteRouteJImage = new ImageIcon(Main.class.getResource("../images/noteRoutePressed.png")).getImage();
	}

	public void releaseJ() {
		noteRouteJImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	}

	public void pressK() {
		judge("K");
		noteRouteKImage = new ImageIcon(Main.class.getResource("../images/noteRoutePressed.png")).getImage();
	}

	public void releaseK() {
		noteRouteKImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	}

	public void pressL() {
		judge("L");
		noteRouteLImage = new ImageIcon(Main.class.getResource("../images/noteRoutePressed.png")).getImage();
	}

	public void releaseL() {
		noteRouteLImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	}

	@Override
	public void run() {
		dropNotes();
	}

	public void close() {
		gameMusic.close();
		this.interrupt();
	}

	public void dropNotes() {
		Beat[] beat = null;
		if (title.equals("해야")) {
			int startTime = 4000 - Main.REACH_TIME  * 1000;
			int gap = 100;
			beat = new Beat[] { new Beat(3030 - startTime - gap, "D"), new Beat(3560 - startTime - gap, "F"),
					new Beat(4480 - startTime - gap, "S"), new Beat(4630 - startTime - gap, "D"),
					new Beat(4750 - startTime - gap, "F"), new Beat(5130 - startTime - gap, "D"),
					new Beat(6190 - startTime - gap, "S"), new Beat(6770 - startTime - gap, "D"),
					new Beat(7580 - startTime - gap, "S"), new Beat(7760 - startTime - gap, "D"),
					new Beat(9430 - startTime - gap, "S"), new Beat(10950 - startTime - gap, "D"),
					new Beat(12500 - startTime - gap, "F"), new Beat(14250 - startTime - gap, "D"),
					new Beat(14610 - startTime - gap, "F"), new Beat(15230 - startTime - gap, "D"),
					new Beat(15930 - startTime - gap, "S"), new Beat(16300 - startTime - gap, "D"),
					new Beat(16650 - startTime - gap, "F"), new Beat(17030 - startTime - gap, "D"),
					new Beat(18020 - startTime - gap, "S"), new Beat(18380 - startTime - gap, "D"),
					new Beat(19400 - startTime - gap, "D"), new Beat(19750 - startTime - gap, "F"),
					new Beat(20760 - startTime - gap, "D"), new Beat(21100 - startTime - gap, "S"),
					new Beat(22090 - startTime - gap, "D"), new Beat(22460 - startTime - gap, "F"),
					new Beat(23840 - startTime - gap, "D"), new Beat(24520 - startTime - gap, "F"),
					new Beat(25470 - startTime - gap, "D"), new Beat(26310 - startTime - gap, "S"),
					new Beat(26650 - startTime - gap, "D"), new Beat(27640 - startTime - gap, "F"),
					new Beat(28010 - startTime - gap, "D"), new Beat(28020 - startTime - gap, "S"),
					new Beat(29070 - startTime - gap, "S"), new Beat(29420 - startTime - gap, "D"),
					new Beat(30410 - startTime - gap, "D"), new Beat(30750 - startTime - gap, "F"),
					new Beat(31760 - startTime - gap, "S"), new Beat(32130 - startTime - gap, "D"),
					new Beat(33150 - startTime - gap, "D"), new Beat(33530 - startTime - gap, "F"),
					new Beat(34590 - startTime - gap, "S"), new Beat(34920 - startTime - gap, "F"),
					new Beat(35620 - startTime - gap, "D"), new Beat(36320 - startTime - gap, "D"),
					new Beat(36980 - startTime - gap, "D"), new Beat(37670 - startTime - gap, "D"),
					new Beat(38370 - startTime - gap, "D"), new Beat(39060 - startTime - gap, "F"),
					new Beat(39750 - startTime - gap, "F"), new Beat(40490 - startTime - gap, "F"),
					new Beat(41180 - startTime - gap, "F"), new Beat(41780 - startTime - gap, "S"),
					new Beat(42120 - startTime - gap, "F"), new Beat(42490 - startTime - gap, "S"),
					new Beat(42850 - startTime - gap, "F"), new Beat(43210 - startTime - gap, "S"),
					new Beat(43570 - startTime - gap, "F"), new Beat(43940 - startTime - gap, "S"),
					new Beat(44260 - startTime - gap, "F"), new Beat(44610 - startTime - gap, "S"),
					new Beat(44970 - startTime - gap, "F"), new Beat(45830 - startTime - gap, "D"),
					new Beat(47320 - startTime - gap, "D"), new Beat(47900 - startTime - gap, "F"),
					new Beat(48340 - startTime - gap, "D"), new Beat(48920 - startTime - gap, "F"),
					new Beat(49380 - startTime - gap, "S"), new Beat(49750 - startTime - gap, "D"),
					new Beat(50060 - startTime - gap, "F"), new Beat(50590 - startTime - gap, "D"),
					new Beat(51110 - startTime - gap, "F"), new Beat(51560 - startTime - gap, "D"),

			};
		} else if (title.equals("Flower")) {
			int startTime = 500 - Main.REACH_TIME * 1000;
			beat = new Beat[] { new Beat(startTime, "S") };
		} else if (title.equals("지나고도 같은 오늘")) {
			int startTime = 4000 - Main.REACH_TIME * 1000;
			int gap = 100;
			beat = new Beat[] { new Beat(10910 - startTime - gap, "F"), new Beat(11200 - startTime - gap, "D"),
					new Beat(11600 - startTime - gap, "K"), new Beat(12070 - startTime - gap, "F"),
					new Beat(12500 - startTime - gap, "K"), new Beat(13400 - startTime - gap, "L"),
					new Beat(13790 - startTime - gap, "K"), new Beat(14270 - startTime - gap, "J"),
					new Beat(14680 - startTime - gap, "D"), new Beat(15170 - startTime - gap, "S"),
					new Beat(15630 - startTime - gap, "D"), new Beat(16060 - startTime - gap, "F"),
					new Beat(16960 - startTime - gap, "K"), new Beat(17870 - startTime - gap, "D"),
					new Beat(18300 - startTime - gap, "J"), new Beat(18720 - startTime - gap, "F"),
					new Beat(19160 - startTime - gap, "K"), new Beat(19620 - startTime - gap, "S"),
					new Beat(20050 - startTime - gap, "L"), new Beat(20550 - startTime - gap, "D"),
					new Beat(21470 - startTime - gap, "J"), new Beat(21870 - startTime - gap, "F"),
					new Beat(22310 - startTime - gap, "K"), new Beat(22740 - startTime - gap, "F"),
					new Beat(23150 - startTime - gap, "J"), new Beat(24130 - startTime - gap, "D"),
					new Beat(25000 - startTime - gap, "K"), new Beat(25440 - startTime - gap, "D"),
					new Beat(25890 - startTime - gap, "J"), new Beat(26340 - startTime - gap, "F"),
					new Beat(26760 - startTime - gap, "K"), new Beat(27650 - startTime - gap, "S"),
					new Beat(28540 - startTime - gap, "K"), new Beat(29010 - startTime - gap, "D"),
					new Beat(29580 - startTime - gap, "L"), new Beat(29940 - startTime - gap, "F"),
					new Beat(30360 - startTime - gap, "J"), new Beat(31250 - startTime - gap, "K"),
					new Beat(32180 - startTime - gap, "D"), new Beat(32580 - startTime - gap, "L"),
					new Beat(33060 - startTime - gap, "S"), new Beat(33500 - startTime - gap, "K"),
					new Beat(33950 - startTime - gap, "D"), new Beat(34450 - startTime - gap, "J"),
					new Beat(34900 - startTime - gap, "F"), new Beat(35270 - startTime - gap, "K"),
					new Beat(35790 - startTime - gap, "D"), new Beat(36230 - startTime - gap, "J"),
					new Beat(36680 - startTime - gap, "F"), new Beat(37570 - startTime - gap, "K"),
					new Beat(38490 - startTime - gap, "K"), new Beat(39320 - startTime - gap, "F"),
					new Beat(39800 - startTime - gap, "J"), new Beat(40230 - startTime - gap, "D"),
					new Beat(40720 - startTime - gap, "K"), new Beat(41180 - startTime - gap, "F"),
					new Beat(42100 - startTime - gap, "S"), new Beat(42930 - startTime - gap, "L"),
					new Beat(43350 - startTime - gap, "D"), new Beat(43820 - startTime - gap, "K"),
					new Beat(44240 - startTime - gap, "F"), new Beat(44730 - startTime - gap, "J"),
					new Beat(45610 - startTime - gap, "D"), new Beat(46510 - startTime - gap, "K"),
					new Beat(47320 - startTime - gap, "S"), new Beat(47910 - startTime - gap, "D"),
					new Beat(48350 - startTime - gap, "F"), new Beat(49270 - startTime - gap, "L"),
					new Beat(50120 - startTime - gap, "K"), new Beat(50990 - startTime - gap, "J"),
					new Beat(51900 - startTime - gap, "D"), new Beat(52780 - startTime - gap, "F"),
					new Beat(53650 - startTime - gap, "J"), new Beat(54490 - startTime - gap, "S"),
					new Beat(55070 - startTime - gap, "D"), new Beat(55490 - startTime - gap, "F"),
					new Beat(56390 - startTime - gap, "K"), new Beat(57360 - startTime - gap, "F"),
					new Beat(57740 - startTime - gap, "J"), new Beat(58170 - startTime - gap, "D"),
					new Beat(58670 - startTime - gap, "S"), new Beat(59060 - startTime - gap, "D"),
					new Beat(59500 - startTime - gap, "S"), new Beat(59970 - startTime - gap, "K"),
					new Beat(60470 - startTime - gap, "D"), new Beat(60890 - startTime - gap, "L"),
					new Beat(61390 - startTime - gap, "F"), new Beat(61800 - startTime - gap, "J"),
					new Beat(62260 - startTime - gap, "D"), new Beat(62660 - startTime - gap, "K"),
					new Beat(63090 - startTime - gap, "F"), new Beat(63570 - startTime - gap, "J"),
					new Beat(64000 - startTime - gap, "D"), new Beat(64530 - startTime - gap, "K"),
					new Beat(64900 - startTime - gap, "S"), new Beat(65310 - startTime - gap, "L"),
					new Beat(65750 - startTime - gap, "D"), new Beat(66150 - startTime - gap, "K"),
					new Beat(66650 - startTime - gap, "F"), new Beat(67090 - startTime - gap, "J"),
					new Beat(67540 - startTime - gap, "D"), new Beat(67990 - startTime - gap, "K"),
					new Beat(68960 - startTime - gap, "S"), new Beat(69390 - startTime - gap, "L"),
					new Beat(69600 - startTime - gap, "D"), new Beat(69830 - startTime - gap, "K"),
					new Beat(70640 - startTime - gap, "F"), new Beat(71560 - startTime - gap, "J"),
					new Beat(72490 - startTime - gap, "F"), new Beat(73350 - startTime - gap, "D"),
					new Beat(74240 - startTime - gap, "S"), new Beat(75110 - startTime - gap, "S"),
					new Beat(76050 - startTime - gap, "D"), new Beat(76860 - startTime - gap, "F"),
					new Beat(77740 - startTime - gap, "K"), new Beat(78670 - startTime - gap, "J"),
					new Beat(79570 - startTime - gap, "L"), new Beat(80470 - startTime - gap, "K"),
					new Beat(81010 - startTime - gap, "J"), new Beat(81430 - startTime - gap, "K"),
					new Beat(81910 - startTime - gap, "L"), new Beat(82380 - startTime - gap, "D"),
					new Beat(83310 - startTime - gap, "F"), new Beat(84110 - startTime - gap, "F"),
					new Beat(85020 - startTime - gap, "J"), new Beat(85970 - startTime - gap, "D"),
					new Beat(86810 - startTime - gap, "K"), new Beat(87710 - startTime - gap, "K"),
					new Beat(88600 - startTime - gap, "K"), new Beat(89470 - startTime - gap, "L"),
					new Beat(90390 - startTime - gap, "J"), new Beat(91260 - startTime - gap, "K"),
					new Beat(92210 - startTime - gap, "L"), new Beat(93100 - startTime - gap, "K"),
					new Beat(97550 - startTime - gap, "D"), new Beat(98370 - startTime - gap, "K"),
					new Beat(99290 - startTime - gap, "J"), new Beat(100270 - startTime - gap, "K"),
					new Beat(101230 - startTime - gap, "D"), new Beat(102090 - startTime - gap, "K"),
					new Beat(102480 - startTime - gap, "D"), new Beat(102690 - startTime - gap, "D"),
					new Beat(102970 - startTime - gap, "K"), new Beat(103860 - startTime - gap, "K"),
					new Beat(104320 - startTime - gap, "D"), new Beat(104520 - startTime - gap, "D"),
					new Beat(104770 - startTime - gap, "K"), new Beat(105660 - startTime - gap, "K"),
					new Beat(106090 - startTime - gap, "D"), new Beat(106290 - startTime - gap, "D"),
					new Beat(106550 - startTime - gap, "K"), new Beat(107420 - startTime - gap, "K"),
					new Beat(107900 - startTime - gap, "D"), new Beat(108120 - startTime - gap, "D"),
					new Beat(108360 - startTime - gap, "K"), new Beat(109210 - startTime - gap, "K"),
					new Beat(109650 - startTime - gap, "D"), new Beat(109870 - startTime - gap, "D"),
					new Beat(110110 - startTime - gap, "K"), new Beat(111020 - startTime - gap, "K"),
					new Beat(111450 - startTime - gap, "D"), new Beat(111650 - startTime - gap, "D"),
					new Beat(111910 - startTime - gap, "K"), new Beat(112800 - startTime - gap, "K"),
					new Beat(113210 - startTime - gap, "D"), new Beat(113430 - startTime - gap, "D"),
					new Beat(113690 - startTime - gap, "K"), new Beat(114580 - startTime - gap, "K"),
					new Beat(115030 - startTime - gap, "D"), new Beat(115240 - startTime - gap, "D"),
					new Beat(115480 - startTime - gap, "K"), new Beat(116370 - startTime - gap, "K"),
					new Beat(116810 - startTime - gap, "D"), new Beat(117040 - startTime - gap, "D"),
					new Beat(117310 - startTime - gap, "K"), new Beat(118200 - startTime - gap, "K"),
					new Beat(118660 - startTime - gap, "D"), new Beat(118850 - startTime - gap, "D"),
					new Beat(119090 - startTime - gap, "K"), new Beat(120000 - startTime - gap, "K"),
					new Beat(120400 - startTime - gap, "D"), new Beat(120630 - startTime - gap, "D"),
					new Beat(120880 - startTime - gap, "K"), new Beat(121750 - startTime - gap, "K"),
					new Beat(122220 - startTime - gap, "D"), new Beat(122420 - startTime - gap, "D"),
					new Beat(122680 - startTime - gap, "K"), new Beat(123560 - startTime - gap, "K"),
					new Beat(124000 - startTime - gap, "D"), new Beat(124220 - startTime - gap, "D"),
					new Beat(124450 - startTime - gap, "K"), new Beat(125330 - startTime - gap, "K"),
					new Beat(126220 - startTime - gap, "L"), new Beat(126230 - startTime - gap, "K"),
					new Beat(126230 - startTime - gap, "J"), new Beat(127150 - startTime - gap, "J"),
					new Beat(128030 - startTime - gap, "F"), new Beat(128930 - startTime - gap, "D"),
					new Beat(129860 - startTime - gap, "F"), new Beat(130720 - startTime - gap, "F"),
					new Beat(131620 - startTime - gap, "J"), new Beat(132500 - startTime - gap, "K"),
					new Beat(132980 - startTime - gap, "J"), new Beat(133420 - startTime - gap, "F"),
					new Beat(133830 - startTime - gap, "D"), new Beat(134280 - startTime - gap, "S"),
					new Beat(135190 - startTime - gap, "S"), new Beat(136070 - startTime - gap, "S"),
					new Beat(136980 - startTime - gap, "D"), new Beat(137000 - startTime - gap, "K"),
					new Beat(137000 - startTime - gap, "F"), new Beat(137000 - startTime - gap, "J"),
					new Beat(137850 - startTime - gap, "K"), new Beat(138790 - startTime - gap, "L"),
					new Beat(139690 - startTime - gap, "K"), new Beat(140150 - startTime - gap, "D"),
					new Beat(140540 - startTime - gap, "J"), new Beat(140770 - startTime - gap, "F"),
					new Beat(141490 - startTime - gap, "F"), new Beat(141510 - startTime - gap, "K"),
					new Beat(141510 - startTime - gap, "D"), new Beat(141510 - startTime - gap, "J"),
					new Beat(142350 - startTime - gap, "F"), new Beat(142350 - startTime - gap, "D"),
					new Beat(142360 - startTime - gap, "K"), new Beat(142380 - startTime - gap, "J"),
					new Beat(143240 - startTime - gap, "F"), new Beat(143240 - startTime - gap, "D"),
					new Beat(143260 - startTime - gap, "K"), new Beat(143260 - startTime - gap, "J"),
					new Beat(144120 - startTime - gap, "F"), new Beat(144120 - startTime - gap, "D"),
					new Beat(144140 - startTime - gap, "K"), new Beat(144140 - startTime - gap, "J"),
					new Beat(145030 - startTime - gap, "F"), new Beat(145030 - startTime - gap, "D"),
					new Beat(145030 - startTime - gap, "S"), new Beat(145060 - startTime - gap, "L"),
					new Beat(145060 - startTime - gap, "K"), new Beat(145060 - startTime - gap, "J"),
					new Beat(145900 - startTime - gap, "F"), new Beat(145900 - startTime - gap, "D"),
					new Beat(145900 - startTime - gap, "S"), new Beat(145930 - startTime - gap, "L"),
					new Beat(145930 - startTime - gap, "K"), new Beat(145930 - startTime - gap, "J"),
					new Beat(146780 - startTime - gap, "F"), new Beat(146780 - startTime - gap, "D"),
					new Beat(146780 - startTime - gap, "S"), new Beat(146810 - startTime - gap, "L"),
					new Beat(146810 - startTime - gap, "K"), new Beat(146810 - startTime - gap, "J"),
					new Beat(147700 - startTime - gap, "D"), new Beat(147700 - startTime - gap, "S"),
					new Beat(147720 - startTime - gap, "L"), new Beat(147720 - startTime - gap, "K"),
					new Beat(147720 - startTime - gap, "F"), new Beat(147720 - startTime - gap, "J"),
					new Beat(148620 - startTime - gap, "F"), new Beat(149570 - startTime - gap, "F"),
					new Beat(150460 - startTime - gap, "J"), new Beat(151290 - startTime - gap, "J"),
					new Beat(152200 - startTime - gap, "D"), new Beat(153100 - startTime - gap, "K"),
					new Beat(154010 - startTime - gap, "F"), new Beat(154930 - startTime - gap, "J"),
					new Beat(155800 - startTime - gap, "F"), new Beat(156650 - startTime - gap, "K"),
					new Beat(157600 - startTime - gap, "D"), new Beat(158460 - startTime - gap, "J"),
					new Beat(159340 - startTime - gap, "F"), new Beat(160220 - startTime - gap, "K"),
					new Beat(161200 - startTime - gap, "D"), new Beat(161640 - startTime - gap, "J"),
					new Beat(161850 - startTime - gap, "F"), new Beat(162060 - startTime - gap, "K"),
					new Beat(162970 - startTime - gap, "D"), new Beat(162990 - startTime - gap, "S"),
					new Beat(163430 - startTime - gap, "K"), new Beat(163660 - startTime - gap, "F"),
					new Beat(163840 - startTime - gap, "J"), new Beat(164340 - startTime - gap, "D"),
					new Beat(164530 - startTime - gap, "K"), new Beat(164720 - startTime - gap, "F"),
					new Beat(165190 - startTime - gap, "J"), new Beat(165420 - startTime - gap, "D"),
					new Beat(165660 - startTime - gap, "K"), new Beat(166100 - startTime - gap, "F"),
					new Beat(166320 - startTime - gap, "J"), new Beat(166520 - startTime - gap, "D"),
					new Beat(166980 - startTime - gap, "K"), new Beat(167400 - startTime - gap, "F"),
					new Beat(167890 - startTime - gap, "J"),
					new Beat(168390 - startTime - gap, "D"), new Beat(168390 - startTime - gap, "S"),
					new Beat(168520 - startTime - gap, "F"), new Beat(168960 - startTime - gap, "J"),
					new Beat(169410 - startTime - gap, "K"), new Beat(170160 - startTime - gap, "K"),
					new Beat(171050 - startTime - gap, "J"), new Beat(171460 - startTime - gap, "F"),
					new Beat(171680 - startTime - gap, "J"), new Beat(171890 - startTime - gap, "D"),
					new Beat(172830 - startTime - gap, "K"), new Beat(173260 - startTime - gap, "D"),
					new Beat(173490 - startTime - gap, "J"), new Beat(173690 - startTime - gap, "F"),
					new Beat(174560 - startTime - gap, "K"), new Beat(175000 - startTime - gap, "D"),
					new Beat(175270 - startTime - gap, "L"), new Beat(175500 - startTime - gap, "S"),
					new Beat(175940 - startTime - gap, "K"), new Beat(176170 - startTime - gap, "D"),
					new Beat(176410 - startTime - gap, "J"), new Beat(176620 - startTime - gap, "F"),
					new Beat(176880 - startTime - gap, "K"), new Beat(177260 - startTime - gap, "D"),
					new Beat(178160 - startTime - gap, "L"), new Beat(179040 - startTime - gap, "F"),
					new Beat(179980 - startTime - gap, "J"), new Beat(180870 - startTime - gap, "D"),
					new Beat(181120 - startTime - gap, "K"), new Beat(181330 - startTime - gap, "F"),
					new Beat(181560 - startTime - gap, "J"), new Beat(181750 - startTime - gap, "D"),
					new Beat(182000 - startTime - gap, "K"), new Beat(182220 - startTime - gap, "F"),
					new Beat(182450 - startTime - gap, "J"), new Beat(182640 - startTime - gap, "D"),
					new Beat(182880 - startTime - gap, "K"), new Beat(183110 - startTime - gap, "F"),
					new Beat(183330 - startTime - gap, "J"), new Beat(183540 - startTime - gap, "D"),
					new Beat(183740 - startTime - gap, "K"), new Beat(184000 - startTime - gap, "F"),
					new Beat(184260 - startTime - gap, "J"), new Beat(184460 - startTime - gap, "D"),
					new Beat(185080 - startTime - gap, "K"), new Beat(186210 - startTime - gap, "J"),
					new Beat(186530 - startTime - gap, "K"), new Beat(186850 - startTime - gap, "L"),
					new Beat(187120 - startTime - gap, "F"), new Beat(187470 - startTime - gap, "D"),
					 new Beat(188000 - startTime - gap, "J"),
					new Beat(188880 - startTime - gap, "D"), new Beat(189380 - startTime - gap, "K"),
					new Beat(189590 - startTime - gap, "D"), new Beat(189840 - startTime - gap, "F"),
					new Beat(190300 - startTime - gap, "K"), new Beat(190530 - startTime - gap, "D"),
					new Beat(190780 - startTime - gap, "K"), new Beat(191160 - startTime - gap, "F"),
					new Beat(191400 - startTime - gap, "J"), new Beat(191630 - startTime - gap, "D"),
					new Beat(192120 - startTime - gap, "K"), new Beat(192350 - startTime - gap, "D"),
					new Beat(192800 - startTime - gap, "J"), new Beat(192970 - startTime - gap, "F"),
					new Beat(193170 - startTime - gap, "K"), new Beat(193580 - startTime - gap, "D"),
					new Beat(193790 - startTime - gap, "L"), new Beat(194060 - startTime - gap, "F"),
					new Beat(195200 - startTime - gap, "J"), new Beat(196050 - startTime - gap, "J"),
					new Beat(196810 - startTime - gap, "J"), new Beat(197190 - startTime - gap, "J"),
					new Beat(197490 - startTime - gap, "J"), new Beat(198790 - startTime - gap, "F"),
					new Beat(199720 - startTime - gap, "J"), new Beat(200130 - startTime - gap, "F"),
					new Beat(200350 - startTime - gap, "J"), new Beat(200590 - startTime - gap, "D"),
					new Beat(201020 - startTime - gap, "K"), new Beat(201240 - startTime - gap, "D"),
					new Beat(201470 - startTime - gap, "J"), new Beat(201930 - startTime - gap, "D"),
					new Beat(202370 - startTime - gap, "K"), new Beat(202800 - startTime - gap, "D"),
					new Beat(203040 - startTime - gap, "J"), new Beat(203260 - startTime - gap, "F"),
					new Beat(203710 - startTime - gap, "K"), new Beat(204130 - startTime - gap, "D"),
					new Beat(204590 - startTime - gap, "J"), new Beat(204820 - startTime - gap, "F"),
					new Beat(205080 - startTime - gap, "K"), new Beat(205530 - startTime - gap, "D"),
					new Beat(205950 - startTime - gap, "J"), new Beat(206400 - startTime - gap, "F"),
					new Beat(206620 - startTime - gap, "K"), new Beat(206840 - startTime - gap, "D"),
					new Beat(207290 - startTime - gap, "J"), new Beat(207470 - startTime - gap, "F"),
					new Beat(207740 - startTime - gap, "K"), new Beat(208190 - startTime - gap, "D"),
					new Beat(208420 - startTime - gap, "J"), new Beat(208620 - startTime - gap, "F"),
					new Beat(209080 - startTime - gap, "K"), new Beat(209520 - startTime - gap, "S"),
					new Beat(210360 - startTime - gap, "L"),
					new Beat(210690 - startTime - gap, "D"), new Beat(211010 - startTime - gap, "K"),
					new Beat(211270 - startTime - gap, "F"), new Beat(212170 - startTime - gap, "J"),
					new Beat(213110 - startTime - gap, "D"), new Beat(214020 - startTime - gap, "D"),
					new Beat(214870 - startTime - gap, "K"), new Beat(215720 - startTime - gap, "K"),
					new Beat(216670 - startTime - gap, "L"), new Beat(217570 - startTime - gap, "K"),
					new Beat(218490 - startTime - gap, "J"), new Beat(218940 - startTime - gap, "D"),
					new Beat(219160 - startTime - gap, "K"), new Beat(219370 - startTime - gap, "F"),
					new Beat(219830 - startTime - gap, "L"), new Beat(220080 - startTime - gap, "D"),
					new Beat(220310 - startTime - gap, "K"), new Beat(221200 - startTime - gap, "F"),
					new Beat(222100 - startTime - gap, "J"), new Beat(222340 - startTime - gap, "D"),
					new Beat(222520 - startTime - gap, "K"), new Beat(222760 - startTime - gap, "F"),
					new Beat(223000 - startTime - gap, "L"), new Beat(223210 - startTime - gap, "D"),
					new Beat(223400 - startTime - gap, "K"), new Beat(223630 - startTime - gap, "S"),
					new Beat(223860 - startTime - gap, "J"), new Beat(224810 - startTime - gap, "D"),
					new Beat(225650 - startTime - gap, "D"), new Beat(225760 - startTime - gap, "D"),
					new Beat(226590 - startTime - gap, "K"), new Beat(227450 - startTime - gap, "J"),
					new Beat(228350 - startTime - gap, "F"), new Beat(229730 - startTime - gap, "J"),
					new Beat(230210 - startTime - gap, "D"), new Beat(230620 - startTime - gap, "K"),
					new Beat(231270 - startTime - gap, "S"), new Beat(233380 - startTime - gap, "D"),
					new Beat(233850 - startTime - gap, "J"), new Beat(234230 - startTime - gap, "F"),
					new Beat(234660 - startTime - gap, "K"), new Beat(236860 - startTime - gap, "J"),
					new Beat(239590 - startTime - gap, "J"), new Beat(239630 - startTime - gap, "F"),
					new Beat(240150 - startTime - gap, "F") };
		}
		int i = 0;
		gameMusic.start();
		while (i < beat.length && !isInterrupted()) {
			boolean dropped = false;
			if (beat[i].time <= gameMusic.getTime()) {
				Note note = new Note(beat[i].name);
				note.start();
				notelist.add(note);
				i++;
			}
			if (!dropped) {
				try {
					Thread.sleep(5);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void judge(String input) {
		for (int i = 0; i < notelist.size(); i++) {
			Note note = notelist.get(i);
			if (input.equals(note.noteType)) {
				judge = note.judge();
				switch (judge) {
				case "GOOD!":
				case "PERFECT!":
					combo++;
					break;
				default:
					combo = 0;
					break;
				}
				break;
			}
		}
	}

}
