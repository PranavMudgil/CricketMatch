
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

public final class MatchUtils {

	public static void timeout(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public static float round(float value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.floatValue();
	}

	public static int getRandomInt(int a, int b) {
		int res = ThreadLocalRandom.current().nextInt(a, b);
		return res;
	}

	public static int[] tossAndOpt() {
		int[] tossandopt = new int[2];
		tossandopt[0] = getRandomInt(0, 2);
		tossandopt[1] = getRandomInt(0, 2);

		return tossandopt;
	}

	public static int getRun(Player batting) {
		if (batting.getType() == Player.Type.BOWLER) {
			return randomIntForBowler();
		} else {
			return randomIntForBatsman();
		}
	}

	private static int randomIntForBowler() {
		int[] PROBABLE = { 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 5, 5, 6, 7, 7, 7, 7, 7, 7, 7 };

		return PROBABLE[getRandomInt(0, 28)];
	}

	private static int randomIntForBatsman() {
		int[] PROBABLE = { 1, 2, 2, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 7, 7, 7 };

		return PROBABLE[getRandomInt(0, 24)];
	}

}