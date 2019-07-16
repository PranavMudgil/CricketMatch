
import java.util.Map;

public class MatchController {
	
	private static MatchController controller = new MatchController();

	private Team team1, team2;

	private Match match;

	private int matchId;

	private CricketResult result;

	private Map<String, Integer> idMap;

	private int seriesId;
	
	private MatchController() {}
	
	public static MatchController getInstance() {
		return controller;
	}

	// called from main method
	public void initMatch(MatchType type, int matches) {

		generateTeams();

		while (matches >= 1) {

			generateMatch(type.getValue());

			doToss();

			startMatch();

			endMatch();

			uploadMatchResult();

			reset();

			matches--;
		}
		MyConnection.closeConnection();
	}

	private void generateTeams() {

		team1 = new Team("India", DBHelper.getPlayers("India"));
		team2 = new Team("Pakistan", DBHelper.getPlayers("Pakistan"));

		DBHelper.InsertTeam(team1, team2);

		idMap = DBHelper.FetchTeamId(team1.getName(), team2.getName());

		DBHelper.insertSeries(team1.getName(), team2.getName());

		seriesId = DBHelper.getSeriesId();
	}

	private void generateMatch(int overs) {

		DBHelper.InsertMatch(idMap.get(team1.getName()), idMap.get(team2.getName()), team1, team2, seriesId);

		matchId = DBHelper.GetMatchId();
		match = new Match(matchId, overs);
	}

	// this method takes random two element array either 0-0, 0-1, 1-0, 1-1 for toss
	public void doToss() {

		int[] res = MatchUtils.tossAndOpt();

		switch (res[0]) {
		case 0:
			if (res[1] == 0) {
				printUtils.printTeamWinningToss(team1.getName(), "Bat");
				match.battingOrder(team1, team2);
			} else {
				printUtils.printTeamWinningToss(team1.getName(), "Bowl");
				match.battingOrder(team2, team1);
			}
			break;
		case 1:
			if (res[1] == 0) {
				printUtils.printTeamWinningToss(team2.getName(), "Bat");
				match.battingOrder(team2, team1);
			} else {
				printUtils.printTeamWinningToss(team2.getName(), "Bowl");
				match.battingOrder(team1, team2);
			}
		}
	}

	// calls method in Match.java class
	public void startMatch() {

		match.startFirstInnings();

		MatchUtils.timeout(500);

		match.startSecondInnings();

	}

	// gets result from match.class
	public void endMatch() {

		result = match.results();
	};

	// performs all db operation on result
	private void uploadMatchResult() {

		Team winnerTeam = result.getWinner();
		Team loserTeam = result.getLoser();

		if (winnerTeam.getTotalRuns() == loserTeam.getTotalRuns()) {

			DBHelper.InsertMatchTiedResult(winnerTeam, loserTeam, idMap, matchId);

		} else {
			DBHelper.InsertMatchResult(winnerTeam, loserTeam, idMap, matchId);
			winnerTeam.addSeriesScore();
		}

		DBHelper.InsertPlayer(winnerTeam, loserTeam, idMap);

		DBHelper.updateSeries(team1, team2, seriesId);

		printUtils.printSeriesWinner(team1, team2);
	}

	public void reset() {
		team1.reset();
		team2.reset();
	}
}
