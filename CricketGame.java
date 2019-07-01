import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		team2Players.add(team2Player11);

		// We can change the team names since, players are randomly generated and score
		// is random
		String firstTeamName = "INDIA";
		String secondTeamName = "Pakistan";

		Connection conn = null;
		

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
		// if you want to see the score-board after the whole over, simply give values
		// like (4.0,5.0)
		controller.findScoreInOver(null, null);

		// init method to toss, start and end match
		controller.init();

		CricketScoreResult result = controller.getResults();
		

		if (result == null) {
			System.out.println("Result null");
			System.exit(1);
		}

		List<Player> winnerTeam = result.getWinningBoard();

		List<Player> losingTeam = result.getLosingScoreBoard();

		Team win = result.getWinner();
	
		Team lose = result.getLoser();
		
		
		

		
		String insertPlayer = "INSERT into `PlayerDetail`(player_id,player_name,runs_scored,balls_played,player_type,strike_rate,fours,sixes,wickets_taken,overs_taken,runs_given,economy_rate,team_name,outby,out_status,match_id) "
				+ "VALUES(";
		List<PreparedStatement> listmen = new ArrayList<>();

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "root");
			
			
			PreparedStatement insertTeam1 = conn
					.prepareStatement("INSERT into `Team`(team_name) VALUES('" + win.getName() + "')");
			PreparedStatement insertTeam2 = conn
					.prepareStatement("INSERT into `Team`(team_name) VALUES('" + lose.getName() + "')");
			
			PreparedStatement fetch_teamid = conn.prepareStatement("SELECT team_id,team_name from `Team`");
			Map<String,Integer> map = new HashMap<>();
			try {
				insertTeam1.executeUpdate();
				insertTeam2.executeUpdate();
				
			}catch(SQLIntegrityConstraintViolationException e) {
				
				ResultSet idQuery = fetch_teamid.executeQuery();
				

				while (idQuery.next()) {
					map.put(idQuery.getString(2),idQuery.getInt(1)) ;
				}

			}
			
			if(map.size()==0) {
				
				ResultSet idQuery = fetch_teamid.executeQuery();

				while (idQuery.next()) {
					int id = idQuery.getInt(1);
					String name = idQuery.getString(2);
					map.put(name,id) ;
				}
			}
			

			
			String insertMatch;
			if(win.getTotalRuns()==lose.getTotalRuns()) {
				
				insertMatch = "INSERT into `CricketMatch`(team1_id, team1_name,team2_id,team2_name,team1_score,team2_score,team1_wickets,team2_wickets,status) VALUES("
						+map.get(win.getName())+ ",'" + win.getName() + "',"+map.get(lose.getName())+ "," + win.getTotalRuns() + ","+lose.getTotalRuns()+"," + (11 - win.getWickets())+","+(11-lose.getWickets())
						+ ",'Tied')";
			}else {
				insertMatch = "INSERT into `CricketMatch`(team1_id, team1_name,team2_id,team2_name,team1_score,team2_score,winner,winsby,team1_wickets,team2_wickets) VALUES("
					+map.get(win.getName())+ ",'" + win.getName() + "',"+map.get(lose.getName())+ ",'"+lose.getName()+"',"+ win.getTotalRuns() + ","+lose.getTotalRuns()+","+map.get(win.getName())+","+(win.getTotalRuns()-lose.getTotalRuns()) +","+ (11 - win.getWickets())+","+(11-lose.getWickets())+")";
			}
			
			PreparedStatement insertWinner = conn.prepareStatement(insertMatch);

			insertWinner.executeUpdate();
			
			PreparedStatement getMatch_id = conn.prepareStatement("SELECT match_id from CricketMatch ORDER BY match_id DESC limit 1");
			
			ResultSet res_match_id = getMatch_id.executeQuery();
			int match_id=0;
			while(res_match_id.next()) {
				match_id = res_match_id.getInt(1);
			}
		

			for (Player p : winnerTeam) {
			
				if(p.getOut().equals("out")) {
					listmen.add(conn.prepareStatement(insertPlayer + null + ",'" + p.getName() + "'," + p.getRuns() + ","
							+ p.getBalls() + ",'" + p.getType() + "'," + p.getStrikeRate() + "," + p.getFours() + ","
							+ p.getSixes() + "," + p.getWickets() + "," + p.getOversTaken() + "," + p.getRunsGiven() + ","
							+ p.getEconomyRate() + "," + map.get(lose.getName()) + ",'" + p.getOutBy() + "','" + p.getOut() + "',"+match_id+")"));
				}else {
					listmen.add(conn.prepareStatement(insertPlayer + null + ",'" + p.getName() + "'," + p.getRuns() + ","
							+ p.getBalls() + ",'" + p.getType() + "'," + p.getStrikeRate() + "," + p.getFours() + ","
							+ p.getSixes() + "," + p.getWickets() + "," + p.getOversTaken() + "," + p.getRunsGiven() + ","
							+ p.getEconomyRate() + "," + map.get(lose.getName()) + ",'','" + p.getOut() + "',"+match_id+")"));
				}
			}
			for (Player p : losingTeam) {
				if(p.getOut().equals("out")) {
					listmen.add(conn.prepareStatement(insertPlayer + null + ",'" + p.getName() + "'," + p.getRuns() + ","
							+ p.getBalls() + ",'" + p.getType() + "'," + p.getStrikeRate() + "," + p.getFours() + ","
							+ p.getSixes() + "," + p.getWickets() + "," + p.getOversTaken() + "," + p.getRunsGiven() + ","
							+ p.getEconomyRate() + "," + map.get(lose.getName()) + ",'" + p.getOutBy() + "','" + p.getOut() + "',"+match_id+")"));
				}else {
					listmen.add(conn.prepareStatement(insertPlayer + null + ",'" + p.getName() + "'," + p.getRuns() + ","
							+ p.getBalls() + ",'" + p.getType() + "'," + p.getStrikeRate() + "," + p.getFours() + ","
							+ p.getSixes() + "," + p.getWickets() + "," + p.getOversTaken() + "," + p.getRunsGiven() + ","
							+ p.getEconomyRate() + "," + map.get(lose.getName()) + ",'','" + p.getOut() + "',"+match_id+")"));
				}
				
			}

			for (PreparedStatement p : listmen) {
				p.executeUpdate();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection failed!");
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.getMessage();

		} finally {
			try {
				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

}
