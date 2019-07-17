import java.util.List;
import java.util.Map;

public interface DBUtils {

	public void InsertTeamName(Team t);
	public void InsertTeam(Team t1,Team t2);
	public Map<String, Integer> FetchTeamId(String team1, String team2);
	public void InsertMatchTiedResult(Team win, Team lose, Map<String, Integer> idMap,int id);
	public void InsertMatchResult(Team win, Team lose, Map<String, Integer> map, int id);
	public void InsertMatch(int t1, int t2, Team team1, Team team2, int id);
	public int GetMatchId();
	public void InsertPlayer(Team win, Team lose,Map<String, Integer> map);
	public void InsertBallDataFirstInnings(Team team, Player batting, String bowler, float over, String data, int id);
	public void InsertBallDataSecondInnings(Team team, Team prev, Player batting, String bowler, float over,
			String data, int id);
	public void InsertBreakQuery(Team team, float over, int id);
	public void finalBallResultQuery(Team first, Team second, String winner, float over, int id);
	public int getSeriesId();
	public void insertSeries(String t1, String t2);
	public void updateSeries(Team team1, Team team2, int id);
	public List<Player> getPlayers(String name);
}
