
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

	private String findScoreInFirstInnings = null;
	private String findScoreInSecondInnings = null;

	private DecimalFormat runFormatter = new DecimalFormat("###");
	// 10 overs match
	private static int OVERS = 10;
	private Team firstToBat;
	private Team secondToBat;
	private boolean allout = false;
	private Player currentBatting1;
	private Player currentBatting2;
	private Player currentBowler;
	private boolean targetReached = false;
	private float totalOver = 0.0f;
	private boolean secondInnings = false;
	private List<Player> firstToBatScoreBoard = new ArrayList<>();
	private List<Player> secondToBatScoreBoard = new ArrayList<>();

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
			for (Player p : firstToBatScoreBoard) {
				if(p.getOut().equals("out")) {
					System.out.println(p.getName() + "    Out by:" + p.getOutBy() + "    	 	R:"
						+ runFormatter.format(p.getRuns()) + "  B:" + p.getBalls() + "  4's:" + p.getFours() + " 6's:"
						+ p.getSixes() + " S.R:" + p.getStrikeRate());
				}else{
					System.out.println(p.getName() + "*  	  		   	 	R:"+ runFormatter.format(p.getRuns())
					+ "  B:" + p.getBalls() + "  4's:" + p.getFours() + " 6's:"+ p.getSixes()
					+ " S.R:" + p.getStrikeRate());
				}
				
			}
			
		} else {
			System.out.println("\nBatting");
			for (Player p : secondToBatScoreBoard) {
				if(p.getOut().equals("out")) {
					System.out.println(p.getName() + " 		   Out by:" + p.getOutBy() + "    	 	R:"
						+ runFormatter.format(p.getRuns()) + "  B:" + p.getBalls() + "  4's:" + p.getFours() + " 6's:"
						+ p.getSixes() + " S.R:" + p.getStrikeRate());
				}else {
					System.out.println(p.getName() + "*  		    	 	R:"
							+ runFormatter.format(p.getRuns()) + "  B:" + p.getBalls() + "  4's:" + p.getFours() + " 6's:"
							+ p.getSixes() + " S.R:" + p.getStrikeRate());
				}
				
			}
			
		}

		System.out.println("---------------------------------------------------------------\n");
	}

	// first team to get random runs
	public void firstInnings(Connection conn) {

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
			currentBowler = bowlers.get(ThreadLocalRandom.current().nextInt(0, bowlers.size()));
			System.out.println(currentBowler.getName() + " on bowling\n");
			// increment over in current bowler
			currentBowler.addOver();

			for (int j = 1; j < 7; j++) {

				if (findScoreInFirstInnings != null) {
					// if the total overs equals the provided score required over, it calls to
					// getScore()
					if ((totalOver + "").equals(findScoreInFirstInnings))
						this.getScore();
					else if ((totalOver + "").equals(findScoreInFirstInnings))
						this.getScore();
				}
				// adds total over count after each run generated
				int run = scoreGenerator();
				totalOver += 0.1;
				totalOver = Player.round(totalOver, 1);

				try {

					PreparedStatement bowl;
					if (run == 7) {
						bowl = conn.prepareStatement(
								"INSERT into `BallData`(batsman_name,bowler_name,run,team_name) VALUES('"
										+ currentBatting1.getName() + "','" + currentBowler.getName() + "','W','"
										+ firstToBat.getName() + "')");

					} else {
						bowl = conn.prepareStatement(
								"INSERT into `BallData`(batsman_name,bowler_name,run,team_name) VALUES('"
										+ currentBatting1.getName() + "','" + currentBowler.getName() + "','" + run
										+ "','" + firstToBat.getName() + "')");
					}

					bowl.executeUpdate();
				} catch (SQLException e) {
					e.getCause();
				}

				// if random generates 7 it means W(out)
				if (run == 7) {
					// decrement wicket of team
					firstToBat.out();
					currentBatting1.setOut(true);
					// increment wicket of bowler
					currentBowler.wicket();
					// add bowler to batsman outby
					currentBatting1.outBy(currentBowler.getName());
					firstToBatScoreBoard.add(currentBatting1);

					System.out.println("Over:" + totalOver + " W Score:" + firstToBat.getTotalRuns() + "-"
							+ (10 - firstToBat.getWickets()) + "\n");
					System.out.println(currentBatting1.getName() + " Out by " + currentBowler.getName() + " after "
							+ runFormatter.format(currentBatting1.getRuns()) + " runs in (" + totalOver + ") overs");

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
					if (run == 4)
						currentBatting1.addFour();
					if (run == 6)
						currentBatting1.addSix();
					System.out.print("Over:" + totalOver + " Run:" + run);
					// add runs to team
					firstToBat.addRuns(run);
					// add runs to batsman
					currentBatting1.addRuns(run);
					// add balls played
					currentBatting1.addballs();
					//add runs given to bowler
					currentBowler.addRunsGiven(run);

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
		
		if(allout){
			firstToBatScoreBoard.add(currentBatting2);
		}else {
			firstToBatScoreBoard.add(currentBatting1);
			firstToBatScoreBoard.add(currentBatting2);
		}
		
		// if innings finishes before the required over to see score, it prints at the
		// last of innings
		if (findScoreInFirstInnings != null)
			if (Float.parseFloat(findScoreInFirstInnings) > totalOver)
				getScore();

		if ((totalOver + "").equals((OVERS - 1) + ".6"))
			totalOver = OVERS;

		getScore();
	}

	// the reason i choose two methods with same function to compare the runs from
	// previous team in the second innings

	public void secondInnings(Connection conn) {

		System.out.println("Second Innings: " + secondToBat.getName() + " needs " + (firstToBat.getTotalRuns() + 1)
				+ " runs to win this match\n");
		// resetting values for second innings
		secondInnings = true;
		battingPos = 1;
		allout = false;
		totalOver = 0.0f;
		currentBowler = null;
		List<Player> bowlers = firstToBat.getBowlers();

		currentBatting1 = secondToBat.getPlayer(battingPos - 1);
		currentBatting2 = secondToBat.getPlayer(battingPos);

		System.out.println("\n" + currentBatting1.getName() + "* & " + currentBatting2.getName() + "* on the field!");
		System.out.println(currentBatting1.getName() + " is gonna open first!\n");

		float currentOver = 0;

		while (!allout && currentOver < OVERS && !targetReached) {

			totalOver = currentOver + 0.0f;
			// select random bowler from bowlers or allrounders
			currentBowler = bowlers.get(ThreadLocalRandom.current().nextInt(0, bowlers.size()));
			System.out.println(currentBowler.getName() + " on bowling\n");
			// increment over in current bowler
			currentBowler.addOver();
			
			for (int j = 1; j < 7; j++) {

				if (findScoreInSecondInnings != null) {
					if ((totalOver + "").equals(findScoreInSecondInnings))
						this.getScore();
					else if ((totalOver + "").equals(findScoreInSecondInnings))
						this.getScore();
				}

				int run = scoreGenerator();

				currentBowler.addOver();
				try {
					if (conn != null) {
						PreparedStatement bowl;
						if (run == 7) {
							bowl = conn.prepareStatement(
									"INSERT into `BallData`(batsman_name,bowler_name,run,team_name) VALUES('"
											+ currentBatting1.getName() + "','" + currentBowler.getName() + "','W','"
											+ secondToBat.getName() + "')");

						} else {
							bowl = conn.prepareStatement(
									"INSERT into `BallData`(batsman_name,bowler_name,run,team_name) VALUES('"
											+ currentBatting1.getName() + "','" + currentBowler.getName() + "','" + run
											+ "','" + secondToBat.getName() + "')");
						}

						bowl.executeUpdate();
					}

				} catch (SQLException e) {
					e.getCause();
				}

				totalOver += 0.1;
				totalOver = Player.round(totalOver, 1);

				// if second team reaches the target the match finishes
				if (firstToBat.getTotalRuns() < secondToBat.getTotalRuns()) {
					
					
					targetReached = true;
					break;

				}

				// if random generates 7 it means W(out)
				if (run == 7) {
					secondToBat.out();
					currentBowler.wicket();
					currentBatting1.setOut(true);
					currentBatting1.outBy(currentBowler.getName());
					secondToBatScoreBoard.add(currentBatting1);

					System.out.println("Over:" + totalOver + " W\n");
					System.out.println(currentBatting1.getName() + " Out by " + currentBowler.getName() + " after "
							+ runFormatter.format(currentBatting1.getRuns()) + " runs in (" + totalOver + ") overs");

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
					if (run == 4)
						currentBatting1.addFour();
					if (run == 6)
						currentBatting1.addSix();
					System.out.print("Over:" + totalOver + " Run:" + run);
					secondToBat.addRuns(run);
					currentBowler.addRunsGiven(run);

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
			
			// next over
			currentOver++;
			if (currentOver == OVERS || allout == true)
				break;

			System.out.println("\nOver: " + currentOver);
		}
		
		if(allout) {
			secondToBatScoreBoard.add(currentBatting2);
		}else {
			secondToBatScoreBoard.add(currentBatting1);
			secondToBatScoreBoard.add(currentBatting2);
		}

		if (findScoreInSecondInnings != null) {
			if (totalOver < Float.parseFloat(findScoreInSecondInnings))
				this.getScore();
		}
		

		if ((totalOver + "").equals((OVERS - 1) + ".6"))
			totalOver = OVERS;

		getScore();
	}

	// prints final results where both runs of teams are compared(using
	// Integer.compare(int ,int)) and winning team is printed
	public CricketScoreResult results() {
		System.out.println("\n_______________________________________________________________");
		System.out.println("Results:");
		System.out.println("\nTeam " + firstToBat.getName() + ":" + firstToBat.getTotalRuns() + "-"
				+ (11 - firstToBat.getWickets()));

		System.out.println("Team " + secondToBat.getName() + ":" + secondToBat.getTotalRuns() + "-"
				+ (11 - secondToBat.getWickets()));

		int res = Integer.compare(firstToBat.getTotalRuns(), secondToBat.getTotalRuns());

		if (res > 0) {
			System.out.println("***** " + firstToBat.getName() + " Wins the match by "
					+ (firstToBat.getTotalRuns() - secondToBat.getTotalRuns()) + " runs " + " in "
					+ (11 - firstToBat.getWickets()) + " wickets *****");
			System.out.println("_______________________________________________________________");

			return new CricketScoreResult(firstToBat, secondToBat, firstToBatScoreBoard, secondToBatScoreBoard);

		} else if (res < 0) {

			System.out.println("***** " + secondToBat.getName() + " Wins the match by "
					+ (secondToBat.getTotalRuns() - firstToBat.getTotalRuns()) + " runs " + " in "
					+ (11 - secondToBat.getWickets()) + " wickets *****");
			System.out.println("_______________________________________________________________");
			return new CricketScoreResult(secondToBat, firstToBat, secondToBatScoreBoard, firstToBatScoreBoard);

		} else {
			System.out.println("Its a tie, with both the teams making " + firstToBat.getTotalRuns() + " runs!");
			System.out.println("_______________________________________________________________");
			return new CricketScoreResult(firstToBat, secondToBat, firstToBatScoreBoard, secondToBatScoreBoard);

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
