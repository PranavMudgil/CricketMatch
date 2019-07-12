import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

public final class Utility {

	
	public static void timeout(long time) {
		Thread t = new Thread(new Runnable() {
			public void run() {
			}
		});
		t.start();

		try {
			Thread.sleep(time);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public static float round(float value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.floatValue();
	}
	
	public static int getRandomInt(int a,int b) {
		return ThreadLocalRandom.current().nextInt(a, b);
	}
}