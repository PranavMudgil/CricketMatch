
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper implements DBUtils{
	
	private static DBHelper helper = new DBHelper();
	
	public static DBHelper getInstance() {
		return helper;
	}
	
	private ResultSet result_set;

	public void InsertTeamName(Team t) {

		final String insertTeamQuery = "INSERT into `Team`(team_name) VALUES('" + t.getName() + "')";
		try {
			PreparedStatement insertTeam = MyConnection.getConnection().prepareStatement(insertTeamQuery);
			insertTeam.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void InsertTeam(Team t1,Team t2) {
		InsertTeamName(t1);
		InsertTeamName(t2);
	}
	
	public Map<String, Integer> FetchTeamId(String team1, String team2) {
		final String fetchTeamId = "SELECT team_id,team_name from `Team` WHERE team_name='" + team1
				+ "' OR team_name ='" + team2 + "'";
		result_set = null;
		Map<String, Integer> map = new HashMap<>();

		try {
			PreparedStatement fetchId = MyConnection.getConnection().prepareStatement(fetchTeamId);
			result_set = fetchId.executeQuery();
			while (result_set.next()) {
				map.put(result_set.getString(2), result_set.getInt(1));
			}
		} catch (SQLException e) {
			map = null;
			e.printStackTrace();
		}

		return map;
	}

	public void InsertMatchTiedResult(Team win, Team lose, Map<String, Integer> idMap,int id) {
		final String insertTiedResultQuery = "UPDATE CricketMatch SET team1_id=" + idMap.get(win.getName())
				+ ", team1_name='" + win.getName() + "',team2_id=" + idMap.get(lose.getName()) + ",team2_name='"
				+ lose.getName() + "',team1_score=" + win.getTotalRuns() + ",team2_score=" + lose.getTotalRuns()
				+ ",team1_wickets=" + (11 - win.getWickets()) + ",team2_wickets=" + (11 - lose.getWickets())
				+ ",status='tied' where match_id ="+id;


		try {
			PreparedStatement insertTieResult = MyConnection.getConnection().prepareStatement(insertTiedResultQuery);
			insertTieResult.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void InsertMatchResult(Team win, Team lose, Map<String, Integer> map, int id) {
		final String insertResultQuery = "UPDATE `CricketMatch` SET team1_id =" + map.get(win.getName())
				+ ",`team1_name` ='" + win.getName() + "',`team2_id` =" + map.get(lose.getName()) + ",`team2_name` = '"
				+ lose.getName() + "',team1_score= " + win.getTotalRuns() + ",team2_score = " + lose.getTotalRuns()
				+ ",winner = '" + win.getName() + "',winsby = " + (win.getTotalRuns() - lose.getTotalRuns())
				+ ",team1_wickets = " + (11 - win.getWickets()) + ",team2_wickets = " + (11 - lose.getWickets())
				+ " WHERE match_id =" + id;

		try {
			PreparedStatement insertMatchResult = MyConnection.getConnection().prepareStatement(insertResultQuery);
			insertMatchResult.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void InsertMatch(int t1, int t2, Team team1, Team team2, int id) {
		final String query = "INSERT into `CricketMatch`(team1_id,team1_name,team2_id,team2_name,series_id) VALUES("
				+ t1 + ",'" + team1.getName() + "'," + t2 + ",'" + team2.getName() + "'," + id + ")";
		try {
			PreparedStatement insertMatchResult = MyConnection.getConnection().prepareStatement(query);
			insertMatchResult.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int GetMatchId() {
		final String matchIdQuery = "SELECT match_id from CricketMatch ORDER BY match_id DESC limit 1";
		result_set = null;
		int result = -1;
		try {
			PreparedStatement matchIdResult = MyConnection.getConnection().prepareStatement(matchIdQuery);
			result_set = matchIdResult.executeQuery();
			while (result_set.next()) {
				result = result_set.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("Match not inserted");
		}

		return result;
	}

	public void InsertPlayer(Team win, Team lose,Map<String, Integer> map) {
		final String insertPlayerQuery = "INSERT into `PlayerDetail`(player_name,runs_scored,balls_played,player_type,strike_rate,fours,sixes,wickets_taken,overs_taken,runs_given,economy_rate,team_name,outby,out_status,match_id) "
				+ "VALUES(";

		List<PreparedStatement> PlayerListQuery = new ArrayList<>();

		int match_id = helper.GetMatchId();
		
		Connection conn =  MyConnection.getConnection();

		try {
			for (Player p : win.getScoreCard().getList()) {

				if (p.getOut().equals("out")) {
					PlayerListQuery.add(conn.prepareStatement(insertPlayerQuery + "'" + p.getName() + "'," + p.getRuns()
							+ "," + p.getBalls() + ",'" + p.getType() + "'," + p.getStrikeRate() + "," + p.getFours()
							+ "," + p.getSixes() + "," + p.getWickets() + "," + p.getOversTaken() + ","
							+ p.getRunsGiven() + "," + p.getEconomyRate() + "," + map.get(win.getName()) + ",'"
							+ p.getOutBy() + "','" + p.getOut() + "'," + match_id + ")"));
				} else {
					PlayerListQuery.add(conn.prepareStatement(insertPlayerQuery + "'" + p.getName() + "*',"
							+ p.getRuns() + "," + p.getBalls() + ",'" + p.getType() + "'," + p.getStrikeRate() + ","
							+ p.getFours() + "," + p.getSixes() + "," + p.getWickets() + "," + p.getOversTaken() + ","
							+ p.getRunsGiven() + "," + p.getEconomyRate() + "," + map.get(win.getName()) + ",'','"
							+ p.getOut() + "'," + match_id + ")"));
				}
			}
			for (Player p : lose.getScoreCard().getList()) {
				if (p.getOut().equals("out")) {
					PlayerListQuery.add(conn.prepareStatement(insertPlayerQuery + "'" + p.getName() + "'," + p.getRuns()
							+ "," + p.getBalls() + ",'" + p.getType() + "'," + p.getStrikeRate() + "," + p.getFours()
							+ "," + p.getSixes() + "," + p.getWickets() + "," + p.getOversTaken() + ","
							+ p.getRunsGiven() + "," + p.getEconomyRate() + "," + map.get(lose.getName()) + ",'"
							+ p.getOutBy() + "','" + p.getOut() + "'," + match_id + ")"));
				} else {
					PlayerListQuery.add(conn.prepareStatement(insertPlayerQuery + "'" + p.getName() + "*',"
							+ p.getRuns() + "," + p.getBalls() + ",'" + p.getType() + "'," + p.getStrikeRate() + ","
							+ p.getFours() + "," + p.getSixes() + "," + p.getWickets() + "," + p.getOversTaken() + ","
							+ p.getRunsGiven() + "," + p.getEconomyRate() + "," + map.get(lose.getName()) + ",'','"
							+ p.getOut() + "'," + match_id + ")"));
				}

			}

			for (PreparedStatement p : PlayerListQuery) {
				p.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	public void InsertBallDataFirstInnings(Team team, Player batting, String bowler, float over, String data, int id) {
		final String query = "INSERT into `ball_data`(match_status, batting ,over_by ,run ,team_name ,ball_data.total_over ,team_score, first_innings,personal_score,wickets_given,match_id,first_innings_by)"
				+ " VALUES('on-going','" + batting.getName() + "','" + bowler + "','" + data + "','" + team.getName()
				+ "'," + over + "," + team.getTotalRuns() + ",1," + batting.getRuns() + "," + (11 - team.getWickets())
				+ "," + id + ",'" + team.getName() + "')";


		try {
			PreparedStatement insertWicket = MyConnection.getConnection().prepareStatement(query);
			insertWicket.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void InsertBallDataSecondInnings(Team team, Team prev, Player batting, String bowler, float over,
			String data, int id) {

		final String query = "INSERT into `ball_data`(match_status,batting,over_by,run,team_name ,ball_data.total_over ,team_score,second_innings,personal_score,wickets_given,wickets_prev,prev_overs,target,first_innings_by,match_id)"
				+ "VALUES('on-going','" + batting.getName() + "','" + bowler + "','" + data + "','" + team.getName()
				+ "'," + over + "," + team.getTotalRuns() + ",1," + batting.getRuns() + "," + (11 - team.getWickets())
				+ "," + (11 - prev.getWickets()) + "," + prev.getOver() + "," + prev.getTotalRuns() + ",'"
				+ prev.getName() + "'," + id + ")";

		try {
			PreparedStatement insertBallData = MyConnection.getConnection().prepareStatement(query);
			insertBallData.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void InsertBreakQuery(Team team, float over, int id) {
		final String query = "INSERT into `ball_data`(match_status,team_name,ball_data.total_over,team_score,first_innings,wickets_given,prev_overs,wickets_prev,target,first_innings_by,match_id)VALUES('Break','"
				+ team.getName() + "'," + over + "," + +team.getTotalRuns() + ",1," + (11 - team.getWickets()) + ","
				+ over + "," + (11 - team.getWickets()) + "," + +team.getTotalRuns() + ",'" + team.getName() + "'," + id
				+ ")";

		try {
			PreparedStatement insertBallData = MyConnection.getConnection().prepareStatement(query);
			insertBallData.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void finalBallResultQuery(Team first, Team second, String winner, float over, int id) {
		final String query = "INSERT into `ball_data`(match_status,team_name,ball_data.total_over,team_score,wickets_given,wickets_prev,prev_overs,target,winner,first_innings_by,second_innings_by,match_id)"
				+ "VALUES('Finished','" + second.getName() + "'," + over + "," + second.getTotalRuns() + ","
				+ (11 - second.getWickets()) + "," + +(11 - first.getWickets()) + "," + first.getOver() + ","
				+ first.getTotalRuns() + ",'" + winner + "','" + first.getName() + "','" + second.getName() + "'," + id
				+ ")";

		try {
			PreparedStatement insertBallData = MyConnection.getConnection().prepareStatement(query);
			insertBallData.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getSeriesId() {
		final String query = "Select series_id from series_score order by series_id desc limit 1";
		int res = -1;

		try {

			PreparedStatement getId = MyConnection.getConnection().prepareStatement(query);
			ResultSet set = getId.executeQuery();

			while (set.next()) {
				res = set.getInt(1);
				return res;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	public void insertSeries(String t1, String t2) {
		final String query = "insert into series_score(team1_name,team2_name)values('" + t1 + "','" + t2 + "')";

		try {
			PreparedStatement insertSeries = MyConnection.getConnection().prepareStatement(query);
			insertSeries.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateSeries(Team team1, Team team2, int id) {
		final String query = "UPDATE series_score SET team1_name='" + team1.getName() + "',team1_score= "
				+ team1.getSeriesScore() + ",team2_name='" + team2.getName() + "',team2_score=" + team2.getSeriesScore()
				+ " where series_id =" + id;
		

	

		try {
			PreparedStatement insertSeries = MyConnection.getConnection().prepareStatement(query);
			insertSeries.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	public List<Player> getPlayers(String name) {
		final String query = "SELECT * from player where team_name='"+name+"'";
		List<Player> list = new ArrayList<Player>();
		try {
			PreparedStatement getPlayers = MyConnection.getConnection().prepareStatement(query);
			result_set = getPlayers.executeQuery();
			while(result_set.next()) {
				Player p = new Player(result_set.getInt(1),result_set.getString(2),result_set.getInt("player_age"),result_set.getString(4));
				list.add(p);
			
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	
	private DBHelper() {}
}
