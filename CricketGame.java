import java.util.ArrayList;
import java.util.List;


public class CricketGame {

	public static void main(String[] args) {


		List<Player> team1Players = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			team1Players.add(new Player());
		}

		List<Player> team2Players = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			team2Players.add(new Player());
		}
		

		String firstTeamName = "INDIA";
		
		String secondTeamName = "Pakistan";
		
		Team team1 = new Team(firstTeamName, team1Players);
		Team team2 = new Team(secondTeamName, team2Players);
	
		MatchController controller = new MatchController(team1, team2);

		System.out.println("Welcome to " + team1.getName() + " VS " + team2.getName() + " T20 Match");

		controller.init();

	}

}
