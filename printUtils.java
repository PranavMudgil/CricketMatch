

public class printUtils {

	public static void printTeamWinningToss(String team, String opt) {
		System.out.println(team + " won the toss and chooses to " + opt + " first!");
	}

	public static void printSeriesWinner(Team t1, Team t2) {
		int val = Integer.compare(t1.getSeriesScore(), t2.getSeriesScore());
		if (val == 0) {
			System.out.println("Series Drawed");
		} else if (val > 0) {
			System.out
					.println(t1.getName() + " " + t1.getSeriesScore() + "-" + t2.getSeriesScore() + " " + t2.getName());
		} else if (val < 0) {
			System.out
					.println(t2.getName() + " " + t2.getSeriesScore() + "-" + t1.getSeriesScore() + " " + t1.getName());
		}
		System.out.println("");
	}
	
	
	public static void printScoreBoardHeading() {
		System.out.println("_______________________________________________________________");
		System.out.println("\nSCORE BOARD:");
		System.out.println("---------------------------------------------------------------");
	}

	public static void printWicketTaken(Player batsman, String bowler, Team battingTeam, float over) {
		System.out.println("Over:" + over + " W Score:" + battingTeam.getTotalRuns() + "-"
				+ (11 - battingTeam.getWickets()) + "\n");
		System.out.println(batsman.getName() + " Out by " + bowler + " after " + (int)batsman.getRuns()
				+ " runs in (" + over + ") overs");
	}
	
	public static void printScoreCard(ScoreCard card) {
		System.out.println("Batting:");
		for (Player p : card.getList()) {
			if (p.getOut().equals("out")) {
				System.out.println(p.getName() + "    Out by:" + p.getOutBy() + "    	 	R:"
						+ (int)p.getRuns() + "  B:" + p.getBalls() + "  4's:" + p.getFours() + " 6's:"
						+ p.getSixes() + " S.R:" + p.getStrikeRate());
			} else {
				System.out.println(p.getName() + "*  	  		   	 	R:" + (int)p.getRuns() + "  B:"
						+ p.getBalls() + "  4's:" + p.getFours() + " 6's:" + p.getSixes() + " S.R:"
						+ p.getStrikeRate());
			}

		}
	}
	
	public static void printMatchWinner(Team win, Team lose) {
		System.out.println(
				"***** " + win.getName() + " Wins the match by " + (win.getTotalRuns() - lose.getTotalRuns())
						+ " runs " + " in " + (11 - win.getWickets()) + " wickets *****");
		System.out.println("_______________________________________________________________");

	}
	
	public static void printResultHeader(Team t1,Team t2) {
		System.out.println("\n_______________________________________________________________");
		System.out.println("Results:");
		System.out.println("\nTeam " + t1.getName() + ":" + t1.getTotalRuns() + "-"
				+ (11 - t1.getWickets()));

		System.out.println("Team " + t2.getName() + ":" + t2.getTotalRuns() + "-"
				+ (11 - t2.getWickets()));

	}
	
	public static void printOpeningPlayers(String onStrike, String onSemiStrike) {
		System.out.println("\n" + onStrike + "* & " + onSemiStrike + "* on the field!");
		System.out.println(onStrike + " is gonna open first!\n");
	}
	
	public static void printTarget(Team t1,Team t2) {
		System.out.println("Second Innings: " + t1.getName() + " needs " + (t2.getTotalRuns() + 1)
				+ " runs to win this match\n");

	}

}
