
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Match {

	public Match() {
	}

	private static Match match = new Match();

	public static Match getMatchInstance() {
		return match;
	}

	private DecimalFormat runFormatter = new DecimalFormat("###");

	// 10 overs match
	private static int OVERS = 10;

	private Team firstToBat;
	private Team secondToBat;
	
	private int matchId = -1;

	private boolean allout = false;
	private boolean targetReached = false;

	private Player currentBatting1;
	private Player currentBatting2;
	private Player currentBowler;

	private float totalOver = 0.0f;
	private boolean secondInnings = false;
	private List<Player> firstToBatScoreBoard = new ArrayList<>();
	private List<Player> secondToBatScoreBoard = new ArrayList<>();

	int battingPos = 1;
	
	public void setMatchId(int id) {
		this.matchId = id;
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

	public void printScoreBoardHeading() {
		System.out.println("_______________________________________________________________");
		System.out.println("\nSCORE BOARD:");
		System.out.println("---------------------------------------------------------------");
	}

	public void printFirstInningsScoreBoard() {
		System.out.println("Batting:");
		for (Player p : firstToBatScoreBoard) {
			if (p.getOut().equals("out")) {
				System.out.println(p.getName() + "    Out by:" + p.getOutBy() + "    	 	R:"
						+ runFormatter.format(p.getRuns()) + "  B:" + p.getBalls() + "  4's:" + p.getFours() + " 6's:"
						+ p.getSixes() + " S.R:" + p.getStrikeRate());
			} else {
				System.out.println(p.getName() + "*  	  		   	 	R:" + runFormatter.format(p.getRuns()) + "  B:"
						+ p.getBalls() + "  4's:" + p.getFours() + " 6's:" + p.getSixes() + " S.R:"
						+ p.getStrikeRate());
			}

		}
	}

	public void printSecondInningsScoreBoard() {
		System.out.println("\nBatting");
		for (Player p : secondToBatScoreBoard) {
			if (p.getOut().equals("out")) {
				System.out.println(p.getName() + " 		   Out by:" + p.getOutBy() + "    	 	R:"
						+ runFormatter.format(p.getRuns()) + "  B:" + p.getBalls() + "  4's:" + p.getFours() + " 6's:"
						+ p.getSixes() + " S.R:" + p.getStrikeRate());
			} else {
				System.out.println(p.getName() + "*  		    	 	R:" + runFormatter.format(p.getRuns()) + "  B:"
						+ p.getBalls() + "  4's:" + p.getFours() + " 6's:" + p.getSixes() + " S.R:"
						+ p.getStrikeRate());
			}

		}
	}

	// prints the current bats-men with their runs, balls, Strike Rate, and team's
	// total runs in how many wicketg with current over
	public void getScore() {
		printScoreBoardHeading();
		if (secondInnings == false) {
			printFirstInningsScoreBoard();
		} else {
			printSecondInningsScoreBoard();
		}

		System.out.println("---------------------------------------------------------------\n");
	}

	public void printWicketTaken(Player batsman, String bowler, Team battingTeam, float over) {
		System.out.println("Over:" + over + " W Score:" + battingTeam.getTotalRuns() + "-"
				+ (11 - battingTeam.getWickets()) + "\n");
		System.out.println(batsman.getName() + " Out by " + bowler + " after " + runFormatter.format(batsman.getRuns())
				+ " runs in (" + over + ") overs");
	}

	// first team to get random runs
	public void firstInnings() {

		// assigns first batting persons from the player list
		currentBatting1 = firstToBat.getPlayer(battingPos - 1);
		currentBatting2 = firstToBat.getPlayer(battingPos);

		// this list adds the players who batted and got out
		List<Player> bowlers = secondToBat.getBowlers();

		System.out.println("\n" + currentBatting1.getName() + "* & " + currentBatting2.getName() + "* on the field!");
		System.out.println(currentBatting1.getName() + " is gonna open first!\n");
		// keeps the count of current over as an integer 1-10
		int currentOver = 0;

		// condition while all players are not out or over are finished
		while (!allout && currentOver < OVERS) {

			totalOver = currentOver + 0.0f;

			// select random bowler from bowlers or allrounders
			currentBowler = bowlers.get(Utility.getRandomInt(0, bowlers.size()));

			System.out.println(currentBowler.getName() + " on bowling\n");

			// increment over in current bowler
			currentBowler.addOver();

			for (int j = 1; j < 7; j++) {

				// adds total over count after each run generated
				int run = Utility.getRandomInt(0, 8);
				totalOver += 0.1;
				totalOver = Utility.round(totalOver, 1);

				Utility.timeout(1000);

				// if random generates 7 it means W(out)
				if (run == 7) {

					wicketTaken(firstToBat, currentBatting1, currentBowler, firstToBatScoreBoard);

					SqlQuery.InsertBallDataFirstInnings(firstToBat, currentBatting1, currentBowler.getName(), totalOver,
							"W",matchId);
					printWicketTaken(currentBatting1, currentBowler.getName(), firstToBat, totalOver);

					// increments the batting counter to get next player from player list
					battingPos++;
					currentBatting1 = null;
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
					runTaken(firstToBat, currentBatting1, currentBowler, run);

					SqlQuery.InsertBallDataFirstInnings(firstToBat, currentBatting1, currentBowler.getName(), totalOver,
							run + "",matchId);

				}
				// if run is odd switch batting postitions of players
				if (run % 2 == 1 && run != 7) {
					switchBattingStrike(run);
				}
			}

			// next over
			currentOver++;
			if (currentOver == OVERS || allout == true)
				break;

			System.out.println("\nOver: " + currentOver);

		}

		if (allout) {
			firstToBatScoreBoard.add(currentBatting2);
		} else {
			firstToBatScoreBoard.add(currentBatting1);
			firstToBatScoreBoard.add(currentBatting2);
		}

		SqlQuery.InsertBreakQuery(firstToBat, totalOver,matchId);
		
		firstToBat.setOvers(totalOver);

		getScore();
	}

	public void switchBattingStrike(int run) {

		Player temp = currentBatting1;
		currentBatting1 = currentBatting2;
		currentBatting2 = temp;
		System.out.println(" \n" + currentBatting1.getName() + " on Strike!\n");
	}

	public void wicketTaken(Team team, Player batting, Player bowler, List<Player> scoreBoard) {
		// decrement wicket of team
		team.out();
		// set player batting status to out
		batting.setOut(true);
		// increment wicket of bowler
		bowler.wicket();
		// add bowler to batsman outby
		batting.outBy(bowler.getName());
		// add player to scoreboard
		scoreBoard.add(batting);

	}

	public void printOpeningPlayers(String onStrike, String onSemiStrike) {
		System.out.println("\n" + onStrike + "* & " + onSemiStrike + "* on the field!");
		System.out.println(onStrike + " is gonna open first!\n");
	}

	// the reason i choose two methods with same function to compare the runs from
	// previous team in the second innings

	public void resetValuesForSecondInnings() {
		secondInnings = true;
		battingPos = 1;
		allout = false;
		totalOver = 0.0f;
		currentBowler = null;
	}

	public void secondInnings() {

		System.out.println("Second Innings: " + secondToBat.getName() + " needs " + (firstToBat.getTotalRuns() + 1) + " runs to win this match\n");
		// resetting values for second innings
		resetValuesForSecondInnings();

		List<Player> bowlers = firstToBat.getBowlers();

		currentBatting1 = secondToBat.getPlayer(battingPos - 1);
		currentBatting2 = secondToBat.getPlayer(battingPos);

		printOpeningPlayers(currentBatting1.getName(), currentBatting2.getName());

		float currentOver = 0;

		while (!allout && currentOver < OVERS && !targetReached) {

			totalOver = currentOver + 0.0f;

			// select random bowler from bowlers or allrounders
			currentBowler = bowlers.get(Utility.getRandomInt(0, bowlers.size()));
			
			System.out.println(currentBowler.getName() + " on bowling\n");
			
			// increment over in current bowler
			currentBowler.addOver();

			for (int j = 1; j < 7; j++) {

				int run = Utility.getRandomInt(0, 8);


				totalOver += 0.1;
				totalOver = Utility.round(totalOver, 1);

				// if second team reaches the target the match finishes
				if (firstToBat.getTotalRuns() < secondToBat.getTotalRuns()) {

					targetReached = true;
					break;

				}

				Utility.timeout(1000);

				// if random generates 7 it means W(out)
				if (run == 7) {

					wicketTaken(secondToBat, currentBatting1, currentBowler, secondToBatScoreBoard);

					SqlQuery.InsertBallDataSecondInnings(secondToBat, firstToBat, currentBatting1,
							currentBowler.getName(), totalOver, "W",matchId);

					printWicketTaken(currentBatting1, currentBowler.getName(), secondToBat, totalOver);

					battingPos++;
					currentBatting1 = null;

					// check if team has no wickets left
					if (secondToBat.getWickets() == 1) {
						allout = true;
						break;
					}
					currentBatting1 = secondToBat.getPlayer(battingPos);
					System.out.println(currentBatting1.getName() + " on batting");

					// else add runs to team and continue bowling
				} else {

					runTaken(secondToBat, currentBatting1, currentBowler, run);

					SqlQuery.InsertBallDataSecondInnings(secondToBat, firstToBat, currentBatting1,
							currentBowler.getName(), totalOver, run + "",matchId);

				}
				if (run % 2 == 1 && run != 7) {
					switchBattingStrike(run);
				}

			}

			// next over
			currentOver++;
			if (currentOver == OVERS || allout == true)
				break;

			System.out.println("\nOver: " + currentOver);
		}

		if (allout) {
			secondToBatScoreBoard.add(currentBatting2);
		} else {
			secondToBatScoreBoard.add(currentBatting1);
			secondToBatScoreBoard.add(currentBatting2);
		}

		if ((totalOver + "").equals((OVERS - 1) + ".6"))
			totalOver = OVERS;

		getScore();
	}

	public void runTaken(Team team, Player batting, Player bowler, int run) {
		if (run == 4)
			batting.addFour();
		if (run == 6)
			batting.addSix();
		System.out.print("\nOver:" + totalOver + " Run:" + run);
		team.addRuns(run);
		bowler.addRunsGiven(run);

		batting.addRuns(run);
		batting.addballs();
	}

	public void printResultHeader() {
		System.out.println("\n_______________________________________________________________");
		System.out.println("Results:");
		System.out.println("\nTeam " + firstToBat.getName() + ":" + firstToBat.getTotalRuns() + "-"
				+ (11 - firstToBat.getWickets()));

		System.out.println("Team " + secondToBat.getName() + ":" + secondToBat.getTotalRuns() + "-"
				+ (11 - secondToBat.getWickets()));

	}

	public void printWinner(Team winner, Team loser) {
		System.out.println(
				"***** " + winner.getName() + " Wins the match by " + (winner.getTotalRuns() - loser.getTotalRuns())
						+ " runs " + " in " + (11 - winner.getWickets()) + " wickets *****");
		System.out.println("_______________________________________________________________");

	}

	// prints final results where both runs of teams are compared(using
	// Integer.compare(int ,int)) and winning team is printed
	public CricketScoreResult results() {

		int res = Integer.compare(firstToBat.getTotalRuns(), secondToBat.getTotalRuns());

		if (res > 0) {
			
			printResultHeader();
			
			printWinner(firstToBat, secondToBat);

			SqlQuery.finalBallResultQuery(firstToBat,secondToBat, firstToBat.getName(), totalOver,matchId);

			return new CricketScoreResult(firstToBat, secondToBat, firstToBatScoreBoard, secondToBatScoreBoard);

		} else if (res < 0) {
			printResultHeader();

			printWinner(secondToBat, firstToBat);

			SqlQuery.finalBallResultQuery(firstToBat, secondToBat, secondToBat.getName(), totalOver,matchId);

			return new CricketScoreResult(secondToBat, firstToBat, secondToBatScoreBoard, firstToBatScoreBoard);

		} else {
			printResultHeader();
			System.out.println("Its a tie, with both the teams making " + firstToBat.getTotalRuns() + " runs!");
			System.out.println("_______________________________________________________________");

			SqlQuery.finalBallResultQuery(firstToBat, secondToBat, "Tied", totalOver,matchId);

			return new CricketScoreResult(firstToBat, secondToBat, firstToBatScoreBoard, secondToBatScoreBoard);

		}

	}

}
