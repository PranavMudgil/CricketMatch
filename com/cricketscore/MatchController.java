
public class MatchController {

	private Team team1;
	private Team team2;

	private CricketScoreResult result;

	private Match match = Match.getMatchInstance();
	


	// contructor
	public MatchController(Team t1, Team t2) {
		this.team1 = t1;
		this.team2 = t2;
	}

	// this method takes random two element array either 0-0, 0-1, 1-0, 1-1 for toss
	public void doToss() {

		int[] res = new int[2];
		res = Match.tossAndopt();

		switch (res[0]) {
		case 0:
			if (res[1] == 0) {
				System.out.println(team1.getName() + " Won the toss and opts to Bat first!");
				match.battingOrder(team1, team2);
			} else {
				System.out.println(team1.getName() + " Won the toss and opts to Ball first!");
				match.battingOrder(team2, team1);
			}

			break;
		case 1:
			if (res[1] == 0) {
				System.out.println(team2.getName() + " Won the toss and opts to Bat first!");
				match.battingOrder(team2, team1);
			}

			else {
				System.out.println(team2.getName() + " Won the toss and opts to Ball first!");
				match.battingOrder(team1, team2);
			}

		}

	}

	
	// calls method in Match.java class
	public void startMatch() {


		System.out.println("\nMatch Started!");

		match.firstInnings();

		System.out.println("");

		Utility.timeout(2000);

		match.secondInnings();
		


	}

	public void endMatch() {

		System.out.println("Match ends!");

		result = match.results();
	};

	// called from main method
	public void initMatch(int id) {
		
		match.setMatchId(id);

		doToss();

		startMatch();

		endMatch();

	}

	public CricketScoreResult getResults() {
		if (result != null)
			return result;
		return null;
	}

}
