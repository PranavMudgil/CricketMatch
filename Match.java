
import java.util.concurrent.ThreadLocalRandom;

public class Match {

	public Match() {
	}

	private static Match match = new Match();

	public static Match getMatchInstance() {
		return match;
	}

	// 10 overs match
	private static int OVERS = 20;
	private Team firstToBat;
	private Team secondToBat;
	private boolean allout = false;

	// random run generator 0-6 run 7 means wicket
	public static int scoreGenerator() {
		ThreadLocalRandom runs = ThreadLocalRandom.current();
		return runs.nextInt(0, 8);
	}

	// called by controller to set the batting team 0 means team1 ,1 means team2
	public void battingOrder(Team t1, Team t2) {
		firstToBat = t1;
		secondToBat = t2;
	}

	// generates 2 random integers for toss and opt(batting or bowling)
	public static int[] tossAndopt() {
		int[] tossandopt = new int[2];
		tossandopt[0] = ThreadLocalRandom.current().nextInt(0, 2);
		tossandopt[1] = ThreadLocalRandom.current().nextInt(0, 2);

		return tossandopt;
	}

	// first team to get random runs
	public void firstInnings() {
		
		int i = 0;

		while (!allout && i < OVERS) {

			int[] currentOver = over();

			for (int j : currentOver) {

				if (j == 7) {

					System.out.print("W ");
					firstToBat.out();

					if (firstToBat.getWickets() == 0) {
						allout = true;
						break;
					}

				} else {
					System.out.print(j + " ");
					firstToBat.addRuns(j);

				}

			}
			i++;
			System.out.println("");

		}

	}

	public void secondInnings() {
		allout = false;
		int i = 0;

		while (!allout && i < OVERS) {
		
			
			int[] currentOver = over();

			for (int j : currentOver) {
				if(firstToBat.getTotalRuns()<secondToBat.getTotalRuns()) {
					break;
				}

				if (j == 7) {

					System.out.print("W ");
					secondToBat.out();

					if (secondToBat.getWickets() == 0) {
						allout = true;
						break;
					}

				} else {
					System.out.print(j + " ");
					secondToBat.addRuns(j);
					

				}

			}
			i++;
			System.out.println("");

		}

	}

	// prints final results where both runs of teams are compared and winning team is printed
	public void results() {
		System.out.println("Team " + firstToBat.getName() + ":" + firstToBat.getTotalRuns() + "-"
				+ (10 - firstToBat.getWickets()));

		System.out.println("Team " + secondToBat.getName() + ":" + secondToBat.getTotalRuns() + "-"
				+ (10 - secondToBat.getWickets()));

		int res = Integer.compare(firstToBat.getTotalRuns(), secondToBat.getTotalRuns());

		if (res > 0) {
			System.out.println("Team " + firstToBat.getName() + " Won by "
					+ (firstToBat.getTotalRuns() - secondToBat.getTotalRuns()) + " runs " + " in "
					+ (10 - firstToBat.getWickets()) + " wickets");

		} else if (res < 0) {
			System.out.println("Team " + secondToBat.getName() + " Won by "
					+ (secondToBat.getTotalRuns() - firstToBat.getTotalRuns()) + " runs " + " in "
					+ (10 - secondToBat.getWickets()) + " wickets");


		} else {
			System.out.println("Its a tie, with both the teams making " + firstToBat.getTotalRuns() + " runs!");
		}
	}

	// random function which generates 6 random numbers between 0-7( 0-6 runs)(7 = W)
	public int[] over() {
		int[] over = new int[6];
		for (int i = 0; i < 6; i++) {
			over[i] = scoreGenerator();
		}
		return over;
	}

}
