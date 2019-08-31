package notedata;

import beat.chlwhdtn.Beat;

public class Sunrise implements BeatListener{
	@Override
	public Beat[] getNotes(int startTime, int gap) {
		return new Beat[] { new Beat(3030 - startTime - gap, "D"), new Beat(3560 - startTime - gap, "F"),
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
				new Beat(51110 - startTime - gap, "F"), new Beat(51560 - startTime - gap, "D")
		};
	}
}
