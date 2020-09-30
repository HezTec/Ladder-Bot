package coolGroup.LadderBot;

import sqlClasses.Points;

public class GlickoTest {
	public static void main(String[] args) {
		Points p = new Points();
		String p1 = "2000, 350, 0.06D";
		String p2 = "1000, 350, 0.06D";
		
		p.GlickoUpdateTesting(p2, p1, 1);

		
	}
}
