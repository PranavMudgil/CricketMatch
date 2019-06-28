
public class MatchController {

	private Team team1;
	private Team team2;

	private Match match = Match.getMatchInstance();

	// contructor
	public MatchController(Team t1, Team t2) {
		this.team1 = t1;
		this.team2 = t2;
	}

	
	// this method takes random two element array either 0-0, 0-1, 1-0, 1-1 for toss
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
	
	public void findScoreInOver(String inn1,String inn2) {
		match.findScoreInInnings(inn1, inn2);
	}
	

	// calls method in Match.java class
	public void startMatch() {
		System.out.println("\nMatch Started!");
		
		match.firstInnings();
		
		System.out.println("");
		
		match.secondInnings();
		
		match.results();
		
	}

	
	public void endMatch() {
		System.out.println("Match ends!");
	};


	// called from main method
	public void init() {
		toss();

		startMatch();

		endMatch();

	}

	
	
}
