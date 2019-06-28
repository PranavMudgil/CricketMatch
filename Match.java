
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.text.NumberFormatter;

public class Match {

	public Match() {
	}

	private static Match match = new Match();

	public static Match getMatchInstance() {
		return match;
	}

	private String findScoreInFirstInnings = null;
	private String findScoreInSecondInnings = null;

	private static DecimalFormat formatter = new DecimalFormat("#.#");
	// 10 overs match
	private static int OVERS = 10;
	private Team firstToBat;
	private Team secondToBat;
	private boolean allout = false;
	private Player currentBatting1;
	private Player currentBatting2;
	private boolean targetReached = false;
	private double totalOver = 0.0;
	private boolean secondInnings = false;

	int battingPos = 1;

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

	// takes 2 strings from controller class
	// and assigns them to findScoreInFirstInnings & findScoreInSecondInnings
	// variables here
	// and checks for null and other conditions like values should be within range
	public void findScoreInInnings(String innings1, String innings2) {
		if (innings1 != null && innings2 != null) {
			if (innings1.length() >= 3 && Character.getNumericValue(innings1.charAt(0)) < OVERS
					&& Character.getNumericValue(innings1.charAt(2)) < 7
					&& Character.getNumericValue(innings1.charAt(2)) >= 0)
				findScoreInFirstInnings = innings1.substring(0, 3);

			if (innings2.length() >= 3 && Character.getNumericValue(innings2.charAt(0)) < OVERS
					&& Character.getNumericValue(innings2.charAt(2)) < 7
					&& Character.getNumericValue(innings2.charAt(2)) >= 0)
				findScoreInSecondInnings = innings2.substring(0, 3);
		} else if (innings1 != null) {
			if (innings1.length() >= 3 && Character.getNumericValue(innings1.charAt(0)) < OVERS
					&& Character.getNumericValue(innings1.charAt(2)) < 7
					&& Character.getNumericValue(innings1.charAt(2)) >= 0)
				findScoreInFirstInnings = innings1.substring(0, 3);
		} else if (innings2 != null) {
			if (innings2.length() >= 3 && Character.getNumericValue(innings2.charAt(0)) < OVERS
					&& Character.getNumericValue(innings2.charAt(2)) < 7
					&& Character.getNumericValue(innings2.charAt(2)) >= 0)
				findScoreInSecondInnings = innings2.substring(0, 3);
		}

	}

	// prints the current bats-men with their runs, balls, Strike Rate, and team's
	// total runs in how many wicketg with current over
	public void getScore() {
		System.out.println("_______________________________________________________________");
		System.out.println("\nSCORE BOARD:");
		System.out.println("---------------------------------------------------------------");
		if (secondInnings == false) {
			System.out.println("Batting:");
			System.out.println(currentBatting1.getName() + "*	|	R:" + currentBatting1.getRuns() + "  B:"
					+ currentBatting1.getBalls() + "  4's:" + currentBatting1.getFours() + " 6's:"
					+ currentBatting1.getSixes() + " S.R:" + currentBatting1.getStrikeRate());
			System.out.println(currentBatting2.getName() + "*		|	R:" + currentBatting2.getRuns() + "  B:"
					+ currentBatting2.getBalls() + "  4's:" + currentBatting2.getFours() + " 6's:"
					+ currentBatting2.getSixes() + " S.R:" + currentBatting2.getStrikeRate());
			System.out.println("\nTotal:" + firstToBat.getTotalRuns() + "-" + (10 - secondToBat.getWickets()) + "("
					+ formatter.format(totalOver) + ")\n");
		} else {
			System.out.println("\nBatting");
			System.out.println(currentBatting1.getName() + "*       R:" + currentBatting1.getRuns() + "  B:"
					+ currentBatting1.getBalls() + "  4's:" + currentBatting1.getFours() + " 6's:"
					+ currentBatting1.getSixes() + " S.R:" + currentBatting1.getStrikeRate());
			System.out.println(currentBatting2.getName() + "*       R:" + currentBatting2.getRuns() + "  B:"
					+ currentBatting2.getBalls() + "  4's:" + currentBatting2.getFours() + " 6's:"
					+ currentBatting2.getSixes() + " S.R:" + currentBatting2.getStrikeRate());
			System.out.println("Total:" + secondToBat.getTotalRuns() + "-" + (10 - secondToBat.getWickets()) + "("
					+ formatter.format(totalOver) + ") overs " + "Target:" + firstToBat.getTotalRuns() + " runs");
		}

		System.out.println("---------------------------------------------------------------\n");
	}

	// first team to get random runs
	public void firstInnings() {
		// assigns first batting persons from the player list
		currentBatting1 = firstToBat.getPlayer(battingPos - 1);
		currentBatting2 = firstToBat.getPlayer(battingPos);

		System.out.println("\n" + currentBatting1.getName() + "* & " + currentBatting2.getName() + "* on the field!");
		System.out.println(currentBatting1.getName() + " is gonna open first!\n");
		// keeps the count of current over as an integer 1-10
		int currentOver = 0;

		// condition while all players are not out or over are finished
		while (!allout && currentOver < OVERS) {
			totalOver = currentOver + 0.0;
			for (int j = 1; j < 7; j++) {

				if (findScoreInFirstInnings != null) {
					// if the total overs equals the provided score required over, it calls to
					// getScore()
					if (formatter.format(totalOver).equals(findScoreInFirstInnings))
						this.getScore();
					else if ((currentOver + ".0").equals(findScoreInFirstInnings))
						this.getScore();
				}
				// adds total over count after each run generated
				int run = scoreGenerator();
				totalOver += (double) (j / 10) + 0.1;

				// if random generates 7 it means W(out)
				if (run == 7) {
					firstToBat.out();
					System.out.println("Over:" + formatter.format(totalOver) + " W Score:" + firstToBat.getTotalRuns()
							+ "-" + (10 - firstToBat.getWickets()) + "\n");
					System.out.println(currentBatting1.getName() + " Out after " + currentBatting1.getRuns()
							+ " runs in (" + formatter.format(totalOver) + ") overs");
					// increments the batting counter to get next player from player list
					battingPos++;
					// check if team has no wickets left
					if (firstToBat.getWickets() == 1) {
						allout = true;
						break;
					}
					// gets new player from the list
					currentBatting1 = firstToBat.getPlayer(battingPos);
					System.out.println(currentBatting1.getName() + " on batting");

					// else add runs to team and continue bowling
				} else {
					if (run == 4)
						currentBatting1.addFour();
					if (run == 6)
						currentBatting1.addSix();
					System.out.print("Over:" + formatter.format(totalOver) + " Run:" + run);
					firstToBat.addRuns(run);

					currentBatting1.addRuns(run);
					currentBatting1.addballs();

				}
				// if run is odd switch batting postitions of players
				if (run % 2 == 1 && run != 7) {

					Player temp = currentBatting1;
					currentBatting1 = currentBatting2;
					currentBatting2 = temp;
					System.out.println(" \n" + currentBatting1.getName() + " on Strike!");

				}
				System.out.println("");
			}

			// next over
			currentOver++;
			if (currentOver == OVERS || allout == true)
				break;

			System.out.println("\nOver: " + currentOver);

		}
		// if innings finishes before the required over to see score, it prints at the
		// last of innings
		if (findScoreInFirstInnings != null)
			if (Float.parseFloat(findScoreInFirstInnings) > totalOver)
				getScore();
	}

	// the reason i choose two methods with same function to compare the runs from
	// previous team in the second innings

	public void secondInnings() {

		System.out.println("Second Innings: " + secondToBat.getName() + " needs " + (firstToBat.getTotalRuns() + 1)
				+ " runs to win this match\n");
		// resetting values for second innings
		secondInnings = true;
		battingPos = 1;
		allout = false;
		totalOver = 0;

		currentBatting1 = secondToBat.getPlayer(battingPos - 1);
		currentBatting2 = secondToBat.getPlayer(battingPos);

		System.out.println("\n" + currentBatting1.getName() + "* & " + currentBatting2.getName() + "* on the field!");
		System.out.println(currentBatting1.getName() + " is gonna open first!\n");

		int currentOver = 0;

		while (!allout && currentOver < OVERS) {

			totalOver = currentOver;

			for (int j = 1; j < 7; j++) {

				if (findScoreInSecondInnings != null) {
					if (formatter.format(totalOver).equals(findScoreInSecondInnings))
						this.getScore();
				}

				int run = scoreGenerator();

				totalOver += (double) (j / 10) + 0.1;
				// if second team reaches the target the match finishes
				if (firstToBat.getTotalRuns() < secondToBat.getTotalRuns()) {
					targetReached = true;
					break;
				}

				// if random generates 7 it means W(out)
				if (run == 7) {

					System.out.println("Over:" + formatter.format(totalOver) + " W\n");
					System.out.println(currentBatting1.getName() + " Out after " + currentBatting1.getRuns()
							+ " runs in (" + formatter.format(totalOver) + ") overs");
					secondToBat.out();
					battingPos++;
					// check if team has no wickets left
					if (secondToBat.getWickets() == 1) {
						allout = true;
						break;
					}
					currentBatting1 = secondToBat.getPlayer(battingPos);
					System.out.println(currentBatting1.getName() + " on batting");

					// else add runs to team and continue bowling
				} else {
					if (run == 4)
						currentBatting1.addFour();
					if (run == 6)
						currentBatting1.addSix();
					System.out.print("Over:" + formatter.format(totalOver) + " Run:" + run);
					secondToBat.addRuns(run);

					currentBatting1.addRuns(run);
					currentBatting1.addballs();

				}
				if (run % 2 == 1 && run != 7) {

					Player temp = currentBatting1;
					currentBatting1 = currentBatting2;
					currentBatting2 = temp;

					System.out.println(" " + currentBatting1.getName() + " on Strike!");

				}
				System.out.println("");
			}
			if (targetReached)
				break;
			// next over
			currentOver++;
			if (currentOver == OVERS || allout == true)
				break;

			System.out.println("\nOver: " + currentOver);
		}

		if (findScoreInSecondInnings != null) {
			if (totalOver < Float.parseFloat(findScoreInSecondInnings))
				this.getScore();
		}

	}

	// prints final results where both runs of teams are compared(using
	// Integer.compare(int ,int)) and winning team is printed
	public void results() {
		System.out.println("\n_______________________________________________________________");
		System.out.println("Results:");
		System.out.println("\nTeam " + firstToBat.getName() + ":" + firstToBat.getTotalRuns() + "-"
				+ (10 - firstToBat.getWickets()));

		System.out.println("Team " + secondToBat.getName() + ":" + secondToBat.getTotalRuns() + "-"
				+ (10 - secondToBat.getWickets()));

		int res = Integer.compare(firstToBat.getTotalRuns(), secondToBat.getTotalRuns());

		if (res > 0) {
			System.out.println("***** " + firstToBat.getName() + " Wins the match by "
					+ (firstToBat.getTotalRuns() - secondToBat.getTotalRuns()) + " runs " + " in "
					+ (10 - firstToBat.getWickets()) + " wickets *****");
			System.out.println("_______________________________________________________________");

		} else if (res < 0) {

			System.out.println("***** " + secondToBat.getName() + " Wins the match by "
					+ (secondToBat.getTotalRuns() - firstToBat.getTotalRuns()) + " runs " + " in "
					+ (10 - secondToBat.getWickets()) + " wickets *****");
			System.out.println("_______________________________________________________________");

		} else {
			System.out.println("Its a tie, with both the teams making " + firstToBat.getTotalRuns() + " runs!");
			System.out.println("_______________________________________________________________");
		}
	}

	// un-used
	// random function which generates 6 random numbers between 0-7( 0-6 runs)(7 =
	// W)
	public int[] over() {
		int[] over = new int[6];
		for (int i = 0; i < 6; i++) {
			over[i] = scoreGenerator();
		}
		return over;
	}

}
