import java.util.ArrayList;
import java.util.List;


public class CricketGame {

	public static void main(String[] args) {

		// Team list of first team
		List<Player> team1Players = new ArrayList<>();
		// generate random 10 players
		for (int i = 0; i < 10; i++) {
			team1Players.add(new Player());
		}
		// Team list of second team
		List<Player> team2Players = new ArrayList<>();
		// random 10 players
		for (int i = 0; i < 10; i++) {
			team2Players.add(new Player());
		}
		
		// We can change the team names since, players are randomly generated and score is random
		String firstTeamName = "INDIA";
		
		String secondTeamName = "Pakistan";
		
		// Create two objects of class Team
		Team team1 = new Team(firstTeamName, team1Players);
		Team team2 = new Team(secondTeamName, team2Players);
	
		// give team name and player list to MatchController
		MatchController controller = new MatchController(team1, team2);

	
		System.out.println("Welcome to " + team1.getName() + " VS " + team2.getName() + " T20 Match");
		
		// init method to toss, start and end match
		controller.init();

	}

}
