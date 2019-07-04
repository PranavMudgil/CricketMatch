import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CricketGame {

	public static void main(String[] args) {

		// Team list of first team
		List<Player> team1Players = new ArrayList<>();
		List<Player> team2Players = new ArrayList<>();
		List<Player> winnerTeamScoreBoard = null;
		List<Player> losingTeamScoreBoard = null;

		Team team1 = null, team2 = null;
		Team win = null, lose = null;

		MatchController controller = null;

		CricketScoreResult result = null;
		
		
		Map<String, Integer> idMap  = null;

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
		team2Players.add(team2Player11);

		// Create two objects of class Team
		team1 = new Team("INDIA", team1Players);
		team2 = new Team("Pakistan", team2Players);
		
		SqlQuery.InsertTeamName(team1);
		SqlQuery.InsertTeamName(team2);

		
	
		
		idMap = SqlQuery.FetchTeamId(team1.getName(),team2.getName());
		
		 if(idMap.isEmpty()) {
			 System.exit(1);
		 }else {
			for(Map.Entry<String, Integer> entry:idMap.entrySet()) {
				System.out.println("key:"+entry.getKey()+" value:"+entry.getValue());
			}
		 }
		 
		 System.out.println(idMap.get(team1.getName()));
		 System.out.println(idMap.get(team2.getName()));
		 
//		if (teamIdResultSet != null) {
//			
//			try {
//				
//				while (teamIdResultSet.next()) {
//					System.out.println("inside");
//					String name = teamIdResultSet.getString("team_name");
//					int id = teamIdResultSet.getInt("team_id");
//					System.out.println("name:"+name);
//					System.out.println("id:"+id);
//					//idMap.put(teamIdResultSet.getString(2), teamIdResultSet.getInt(1));
//					System.out.println("/nId:"+teamIdResultSet.getInt(1)+" Name:"+teamIdResultSet.getString(2));
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//				System.out.println("Something wrong while getting id");
//			}finally{
//				teamIdResultSet = null;
//			}

	//	}
		 
		
		SqlQuery.InsertMatch(idMap.get(team1.getName()), idMap.get(team2.getName()), team1, team2);
		
		int id = SqlQuery.GetMatchId();

		
		
		// give team name and player list to MatchController
		controller = new MatchController(team1, team2);
		
		
		

		// null out instances that are not in use
		team1Players = null;
		team2Players = null;
		team1 = null;
		team2 = null;

		// initialise method to toss, start and end match
		controller.initMatch(id);

		
		// get CricketScoreResult object which has winner team, losing team, winner
		// Scoreboard, loser Scoreboard
		result = controller.getResults();

		// null check
		if (result == null) {
			System.out.println("Result null");
			System.exit(1);
		}

		// List of players in batting order
		winnerTeamScoreBoard = result.getWinningBoard();
		losingTeamScoreBoard = result.getLosingScoreBoard();

		// winner team object
		win = result.getWinner();
		lose = result.getLoser();

		

		

		

		if (win.getTotalRuns() == lose.getTotalRuns()) {

			SqlQuery.InsertMatchTiedResult(win, lose, idMap);

		} else {
			SqlQuery.InsertMatchResult(win, lose, idMap);
		}


		SqlQuery.InsertPlayer(win, winnerTeamScoreBoard, lose, losingTeamScoreBoard, idMap);
	}

}
