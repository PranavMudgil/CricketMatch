import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CricketGame {

	public static void main(String[] args) {

		// Team list of first team
		List<Player> team1Players = new ArrayList<>();

		// Enter players in the series you want them to Bat first
		Player team1Player1 = new Player("Virat Kohli", 30, Player.Type.BATSMAN);
		Player team1Player2 = new Player("Rohit Sharma", 32, Player.Type.BATSMAN);
		Player team1Player3 = new Player("Kedar Jadhav", 34, Player.Type.BATSMAN);
		Player team1Player4 = new Player("K.L Rahul", 29, Player.Type.ALLROUNDER);
		Player team1Player5 = new Player("Ravindra Jadeja", 30, Player.Type.ALLROUNDER);
		Player team1Player6 = new Player("Hardik Pandya", 25, Player.Type.ALLROUNDER);
		Player team1Player7 = new Player("M.S Dhoni", 37, Player.Type.WICKETKEEPER);
		Player team1Player8 = new Player("Jasprit Bhumra", 25, Player.Type.BOWLER);
		Player team1Player9 = new Player("Mohammed Shami", 29, Player.Type.BOWLER);
		Player team1Player10 = new Player("Kuldeep Yadav", 24, Player.Type.BOWLER);
		Player team1Player11 = new Player("Yuzvendra Chahal", 28, Player.Type.BOWLER);

		team1Players.add(team1Player1);
		team1Players.add(team1Player2);
		team1Players.add(team1Player3);
		team1Players.add(team1Player4);
		team1Players.add(team1Player5);
		team1Players.add(team1Player6);
		team1Players.add(team1Player7);
		team1Players.add(team1Player8);
		team1Players.add(team1Player9);
		team1Players.add(team1Player10);
		team1Players.add(team1Player11);

		List<Player> team2Players = new ArrayList<>();
		Player team2Player1 = new Player("Babar Azam", 24, Player.Type.BATSMAN);
		Player team2Player2 = new Player("Fakhar Zaman", 29, Player.Type.BATSMAN);
		Player team2Player3 = new Player("Haris Sohail", 30, Player.Type.BATSMAN);
		Player team2Player4 = new Player("Asif Ali", 27, Player.Type.BATSMAN);
		Player team2Player5 = new Player("Shoaib Malik", 37, Player.Type.ALLROUNDER);
		Player team2Player6 = new Player("Mohammed Hafeez", 32, Player.Type.ALLROUNDER);
		Player team2Player7 = new Player("Sarfaraz Ahmed", 32, Player.Type.WICKETKEEPER);
		Player team2Player8 = new Player("Shaheen Afreedi", 19, Player.Type.BOWLER);
		Player team2Player9 = new Player("Wahab Riaz", 33, Player.Type.BOWLER);
		Player team2Player10 = new Player("Mohammad Hasnain", 19, Player.Type.BOWLER);
		Player team2Player11 = new Player("Wasim Akhram", 38, Player.Type.BOWLER);

		team2Players.add(team2Player1);
		team2Players.add(team2Player2);
		team2Players.add(team2Player3);
		team2Players.add(team2Player4);
		team2Players.add(team2Player5);
		team2Players.add(team2Player6);
		team2Players.add(team2Player7);
		team2Players.add(team2Player8);
		team2Players.add(team2Player9);
		team2Players.add(team2Player10);
		team1Players.add(team2Player11);

		// We can change the team names since, players are randomly generated and score
		// is random
		String firstTeamName = "INDIA";

		String secondTeamName = "Pakistan";

		// Create two objects of class Team
		Team team1 = new Team(firstTeamName, team1Players);
		Team team2 = new Team(secondTeamName, team2Players);

		// give team name and player list to MatchController
		MatchController controller = new MatchController(team1, team2);

		System.out.println("Welcome to " + team1.getName() + " VS " + team2.getName() + " T20 Match\n");

		// pass the over for first & second innings in which you want to see the
		// score-board between match(in string)
		// if the innings finishes before the given over the score board will be visible
		// at the end of the innings
		// if you want to see only one innings' score in between, pass other value as
		// null
		// preconditions: values should be between .1 & .6(both inclusive) and overs are
		// set as 10 so between 0.1-0.6,1.1...9.6
		// failed to do so, would do nothing to program, but wont print score board in
		// desired over
		// if you want to see the score-board for the whole over, simply give values
		// like (4.0,5.0)
		controller.findScoreInOver(null, null);

		// init method to toss, start and end match
		controller.init();

	}

}
