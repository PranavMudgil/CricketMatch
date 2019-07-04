import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlQuery {

	public static void InsertTeamName(Team t) {

		final String insertTeamQuery = "INSERT into `Team`(team_name) VALUES('" + t.getName() + "')";

		Connection conn = MyConnection.getConnection();

		try {
			PreparedStatement insertTeam = conn.prepareStatement(insertTeamQuery);
			insertTeam.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("Team already exists!");
		} catch (SQLException e) {
			System.out.println("Something went wrong while inserting team " + t.getName());
		} finally {
			MyConnection.closeConnection();
		}

	}

	public static Map<String,Integer> FetchTeamId(String team1,String team2) {
		final String fetchTeamId = "SELECT team_id,team_name from `Team` WHERE team_name='"+team1+"' OR team_name ='"+team2+"'";
		ResultSet result = null;
		Map<String,Integer> map = new HashMap<>();
		Connection conn = MyConnection.getConnection();
		
		try {
			PreparedStatement fetchId = conn.prepareStatement(fetchTeamId);
			result = fetchId.executeQuery();
			while(result.next()) {
				map.put(result.getString(2),result.getInt(1));
			}
		} catch (SQLException e) {
			map = null;
			e.printStackTrace();
		} finally {
			MyConnection.closeConnection();
		}

		return map;
	}

	public static void InsertMatchTiedResult(Team win, Team lose, Map<String, Integer> idMap) {
		final String insertTiedResultQuery = "INSERT into `CricketMatch`(team1_id, team1_name,team2_id,team2_name,team1_score,team2_score,team1_wickets,team2_wickets,status) VALUES("
				+ idMap.get(win.getName()) + ",'" + win.getName() + "'," + idMap.get(lose.getName()) + ","
				+ win.getTotalRuns() + "," + lose.getTotalRuns() + "," + (11 - win.getWickets()) + ","
				+ (11 - lose.getWickets()) + ",'Tied')";

		Connection conn = MyConnection.getConnection();

		try {
			PreparedStatement insertTieResult = conn.prepareStatement(insertTiedResultQuery);
			insertTieResult.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.closeConnection();
		}

	}

	public static void InsertMatchResult(Team win, Team lose, Map<String, Integer> map) {
		final String insertResultQuery = "UPDATE `CricketMatch` SET team1_id =" + map.get(win.getName())
				+ ", team1_name ='" + win.getName() + "',team2_id =" + map.get(lose.getName()) + " ,team2_name = "
				+ lose.getName() + ",team1_score= " + win.getTotalRuns() + ",team2_score = " + lose.getTotalRuns()
				+ ",winner = " + win.getName() + ",winsby = " + (win.getTotalRuns() - lose.getTotalRuns())
				+ ",team1_wickets = " + (11 - win.getWickets()) + ",team2_wickets = " + (11 - lose.getWickets());

		Connection conn = MyConnection.getConnection();
		try {
			PreparedStatement insertMatchResult = conn.prepareStatement(insertResultQuery);
			insertMatchResult.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void InsertMatch(int t1, int t2, Team team1, Team team2) {
		final String query = "INSERT into `CricketMatch`(team1_id,team1_name,team2_id,team2_name) VALUES(" + t1 + ",'"
				+ team1.getName() + "'," + t2 + ",'" + team2.getName() + "')";
		Connection conn = MyConnection.getConnection();
		try {
			PreparedStatement insertMatchResult = conn.prepareStatement(query);
			insertMatchResult.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int GetMatchId() {
		final String matchIdQuery = "SELECT match_id from CricketMatch ORDER BY match_id DESC limit 1";
		Connection conn = MyConnection.getConnection();
		ResultSet resultSet = null;
		int result = -1;
		try {
			PreparedStatement matchIdResult = conn.prepareStatement(matchIdQuery);
			resultSet = matchIdResult.executeQuery();
			while (resultSet.next()) {
				result = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("Match not inserted");
		} finally {
			MyConnection.closeConnection();
		}

		return result;
	}

	public static void InsertPlayer(Team win, List<Player> winnerTeam, Team lose, List<Player> losingTeam,
			Map<String, Integer> map) {
		final String insertPlayerQuery = "INSERT into `PlayerDetail`(player_name,runs_scored,balls_played,player_type,strike_rate,fours,sixes,wickets_taken,overs_taken,runs_given,economy_rate,team_name,outby,out_status,match_id) "
				+ "VALUES(";

		Connection conn = MyConnection.getConnection();

		List<PreparedStatement> PlayerListQuery = new ArrayList<>();

		int match_id = SqlQuery.GetMatchId();

		try {
			for (Player p : winnerTeam) {

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
			for (Player p : losingTeam) {
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
		} finally {
			MyConnection.closeConnection();
		}
	}

	public static void InsertBallDataFirstInnings(Team team, Player batting, String bowler, float over, String data,
			int id) {
		final String query = "INSERT into `ball_data`(match_status, batting ,over_by ,run ,team_name ,ball_data.total_over ,team_score, first_innings,personal_score,wickets_given,match_id,first_innings_by)"
				+ " VALUES('on-going','" + batting.getName() + "','" + bowler + "','" + data + "','" + team.getName()
				+ "'," + over + "," + team.getTotalRuns() + ",1," + batting.getRuns() + "," + (11 - team.getWickets())
				+ "," + id + ",'"+team.getName()+"')";

		Connection conn = MyConnection.getConnection();

		try {
			PreparedStatement insertWicket = conn.prepareStatement(query);
			insertWicket.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.closeConnection();
		}

	}

	public static void InsertBallDataSecondInnings(Team team, Team prev, Player batting, String bowler, float over,
			String data, int id) {

		final String query = "INSERT into `ball_data`(match_status,batting,over_by,run,team_name ,ball_data.total_over ,team_score,second_innings,personal_score,wickets_given,wickets_prev,prev_overs,target,first_innings_by,match_id)"
				+ "VALUES('on-going','" + batting.getName() + "','" + bowler + "','" + data + "','" + team.getName()
				+ "'," + over + "," + team.getTotalRuns() + ",1," + batting.getRuns() + "," + (11 - team.getWickets())
				+ "," + (11 - prev.getWickets()) + ","+prev.getOver()+"," + prev.getTotalRuns() + ",'"+prev.getName()+"'," + id + ")";

		Connection conn = MyConnection.getConnection();

		try {
			PreparedStatement insertBallData = conn.prepareStatement(query);
			insertBallData.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.closeConnection();
		}

	}

	public static void InsertBreakQuery(Team team, float over, int id) {
		final String query = "INSERT into `ball_data`(match_status,team_name,ball_data.total_over,team_score,first_innings,wickets_given,prev_overs,target,first_innings_by,match_id)VALUES('Break','"
				+ team.getName() + "'," + over + "," + +team.getTotalRuns() + ",1," + (11 - team.getWickets()) + ","
				+ over + "," + +team.getTotalRuns() + ",'" + team.getName() + "'," + id + ")";

		Connection conn = MyConnection.getConnection();

		try {
			PreparedStatement insertBallData = conn.prepareStatement(query);
			insertBallData.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.closeConnection();
		}

	}

	public static void finalBallResultQuery(Team first, Team second, String winner, float over, int id) {
		final String query = "INSERT into `ball_data`(match_status,team_name,ball_data.total_over,team_score,wickets_given,wickets_prev,target,winner,first_innings_by,second_innings_by,match_id)"
				+ "VALUES('Finished','" + second.getName() + "'," + over + "," + second.getTotalRuns() + ","
				+ (11 - second.getWickets()) + "," + +(11 - first.getWickets()) + "," + first.getTotalRuns() + ",'"
				+ winner + "','" + first.getName() + "','" + second.getName() + "'," + id + ")";

		Connection conn = MyConnection.getConnection();

		try {
			PreparedStatement insertBallData = conn.prepareStatement(query);
			insertBallData.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.closeConnection();
		}
	}
}
