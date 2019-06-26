
public class MatchController {

	private Team team1;
	private Team team2;

	private Match match = Match.getMatchInstance();

	public MatchController(Team t1, Team t2) {
		this.team1 = t1;
		this.team2 = t2;
	}

	public void toss() {

		int[] res = new int[2];
		res = Match.tossAndopt();

		switch (res[0]) {
		case 0:
			if (res[1] == 0) {
				System.out.println(team1.getName() + " Won the toss and opts to Bat first!");
				match.battingOrder(team1,team2);
			} else {
				System.out.println(team1.getName() + " Won the toss and opts to Ball first!");
				match.battingOrder(team2,team1);
			}

			break;
		case 1:
			if (res[1] == 0) {
				System.out.println(team2.getName() + " Won the toss and opts to Bat first!");
				match.battingOrder(team2,team1);
			}

			else {
				System.out.println(team2.getName() + " Won the toss and opts to Ball first!");
				match.battingOrder(team1,team2);
			}

		}

	}
	
	public void startMatch() {
		System.out.println("Match Started!");
		
		match.firstInnings();
		
		System.out.println("");
		
		match.secondInnings();
		
		match.results();
		
	}

	public void endMatch() {
		System.out.println("Match ends!");
	};


	public void init() {
		toss();

		startMatch();

		endMatch();

	}

	
	
}
