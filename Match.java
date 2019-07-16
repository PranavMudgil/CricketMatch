
import java.util.ArrayList;

import java.util.List;

public class Match {

	private final int OVERS;

	private Team firstToBat;
	private Team secondToBat;

	private int matchId = -1;

	private boolean allout = false;
	private boolean targetReached = false;
	private boolean secondInnings = false;

	private Player currentBatting1;
	private Player currentBatting2;
	private Player currentBowler;

	private float totalOver = 0.0f;

	private int battingPos = 1;

	List<Player> scoreboard = new ArrayList<>();
	
	public Match(int id, int overs) {
		OVERS = overs;
		matchId = id;
	}

	// called by controller to set the batting team 0 means team1 ,1 means team2
	public void battingOrder(Team t1, Team t2) {
		firstToBat = t1;
		secondToBat = t2;
	}

	// first team to get random runs
	public void startFirstInnings() {
		// assigns first batting persons from the player list
		currentBatting1 = firstToBat.getPlayer(battingPos - 1);
		currentBatting2 = firstToBat.getPlayer(battingPos);

		// this list adds the players who batted and got out
		List<Player> bowlers = secondToBat.getBowlers();

		printUtils.printOpeningPlayers(currentBatting1.getName(), currentBatting2.getName());

		int currentOver = 0;

		while (!allout && currentOver < OVERS) {

			totalOver = currentOver + 0.0f;

			currentBowler = bowlers.get(MatchUtils.getRandomInt(0, bowlers.size()));

			System.out.println(currentBowler.getName() + " on bowling\n");

			currentBowler.addOver();

			for (int j = 1; j < 7; j++) {

				int run = MatchUtils.getRun(currentBatting1);

				totalOver += 0.1;
				totalOver = MatchUtils.round(totalOver, 1);

				MatchUtils.timeout(100);

				// if random generates 7 it means W(out)
				if (run == 7) {

					wicketTaken(firstToBat, currentBatting1, currentBowler, scoreboard);

					DBHelper.InsertBallDataFirstInnings(firstToBat, currentBatting1, currentBowler.getName(), totalOver,
							"W", matchId);

					printUtils.printWicketTaken(currentBatting1, currentBowler.getName(), firstToBat, totalOver);

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
					DBHelper.InsertBallDataFirstInnings(firstToBat, currentBatting1, currentBowler.getName(), totalOver,
							run + "", matchId);
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
			scoreboard.add(currentBatting2);
		} else {
			scoreboard.add(currentBatting1);
			scoreboard.add(currentBatting2);
		}
		DBHelper.InsertBreakQuery(firstToBat, totalOver, matchId);
		firstToBat.setOvers(totalOver);
		firstToBat.setScoreCard(scoreboard);
		getScore();
		scoreboard.clear();
	}

	public void startSecondInnings() {

		printUtils.printTarget(secondToBat, firstToBat);

		// resetting values for second innings
		resetValuesForSecondInnings();

		float currentOver = 0;

		List<Player> bowlers = firstToBat.getBowlers();

		currentBatting1 = secondToBat.getPlayer(battingPos - 1);
		currentBatting2 = secondToBat.getPlayer(battingPos);

		printUtils.printOpeningPlayers(currentBatting1.getName(), currentBatting2.getName());

		while (!allout && currentOver < OVERS && !targetReached) {

			totalOver = currentOver + 0.0f;

			// select random bowler from bowlers or allrounders
			currentBowler = bowlers.get(MatchUtils.getRandomInt(0, bowlers.size()));

			System.out.println(currentBowler.getName() + " on bowling\n");

			// increment over in current bowler
			currentBowler.addOver();

			for (int j = 1; j < 7; j++) {

				int run = MatchUtils.getRun(currentBatting1);

				totalOver += 0.1;
				totalOver = MatchUtils.round(totalOver, 1);

				// if second team reaches the target the match finishes
				if (firstToBat.getTotalRuns() < secondToBat.getTotalRuns()) {
					targetReached = true;
					break;
				}
				MatchUtils.timeout(100);

				// if random generates 7 it means W(out)
				if (run == 7) {

					wicketTaken(secondToBat, currentBatting1, currentBowler, scoreboard);
					DBHelper.InsertBallDataSecondInnings(secondToBat, firstToBat, currentBatting1,
							currentBowler.getName(), totalOver, "W", matchId);
					printUtils.printWicketTaken(currentBatting1, currentBowler.getName(), secondToBat, totalOver);
					battingPos++;
					currentBatting1 = null;
					// check if team has no wickets left
					if (secondToBat.getWickets() == 1) {
						allout = true;
						break;
					}
					currentBatting1 = secondToBat.getPlayer(battingPos);
					System.out.println(currentBatting1.getName() + " on batting");

				} else {

					runTaken(secondToBat, currentBatting1, currentBowler, run);
					DBHelper.InsertBallDataSecondInnings(secondToBat, firstToBat, currentBatting1,
							currentBowler.getName(), totalOver, run + "", matchId);
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
			scoreboard.add(currentBatting2);
		} else {
			scoreboard.add(currentBatting1);
			scoreboard.add(currentBatting2);
		}
		secondToBat.setScoreCard(scoreboard);
		getScore();
		scoreboard = null;
	}

	// returns final results where both runs of teams are compared
	public CricketResult results() {

		int res = Integer.compare(firstToBat.getTotalRuns(), secondToBat.getTotalRuns());

		if (res > 0) {
			printUtils.printResultHeader(firstToBat, secondToBat);
			printUtils.printMatchWinner(firstToBat, secondToBat);

			DBHelper.finalBallResultQuery(firstToBat, secondToBat, firstToBat.getName(), totalOver, matchId);

			return new CricketResult(firstToBat, secondToBat);

		} else if (res < 0) {
			printUtils.printResultHeader(firstToBat, secondToBat);
			printUtils.printMatchWinner(secondToBat, firstToBat);

			DBHelper.finalBallResultQuery(firstToBat, secondToBat, secondToBat.getName(), totalOver, matchId);

			return new CricketResult(secondToBat, firstToBat);

		} else {
			printUtils.printResultHeader(firstToBat, secondToBat);
			System.out.println("Its a tie, with both the teams making " + firstToBat.getTotalRuns() + " runs!");

			DBHelper.finalBallResultQuery(firstToBat, secondToBat, "Tied", totalOver, matchId);

			return new CricketResult(firstToBat, secondToBat);

		}

	}

	private void resetValuesForSecondInnings() {
		secondInnings = true;
		battingPos = 1;
		allout = false;
		totalOver = 0.0f;
		currentBowler = null;
	}

	private void runTaken(Team team, Player batting, Player bowler, int run) {
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

	private void wicketTaken(Team team, Player batting, Player bowler, List<Player> scoreBoard) {
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

	private void switchBattingStrike(int run) {

		Player temp = currentBatting1;
		currentBatting1 = currentBatting2;
		currentBatting2 = temp;
		System.out.println(" \n" + currentBatting1.getName() + " on Strike!\n");
	}

	// prints the current bats-men with their runs, balls, Strike Rate, and team's
	// score
	private void getScore() {

		printUtils.printScoreBoardHeading();

		if (secondInnings == false) {
			printUtils.printScoreCard(firstToBat.getScoreCard());
		} else {
			printUtils.printScoreCard(secondToBat.getScoreCard());
		}

		System.out.println("---------------------------------------------------------------\n");
	}

}
