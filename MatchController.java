
import java.util.Map;

public class MatchController {

	private static MatchController controller = new MatchController();

	private Team team1, team2;

	private Match match;

	private int matchId;

	private CricketResult result;

	private Map<String, Integer> idMap;

	private int seriesId;

	private MatchController() {
	}

	public static MatchController getInstance() {
		return controller;
	}

	// called from main method
	public void initMatch(Match.MatchType type, int matches) {

		generateTeams();

		while (matches >= 1) {

			generateMatch(type);

			doToss();

			startMatch();

			endMatch();

			uploadMatchResult();

			reset();

			matches--;
		}
		MyConnection.closeConnection();
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

			CricketUtilsImpl.getInstance().InsertMatchTiedResult(winnerTeam, loserTeam, idMap, matchId);

		} else {
			CricketUtilsImpl.getInstance().InsertMatchResult(winnerTeam, loserTeam, idMap, matchId);
			winnerTeam.addSeriesScore();
		}

		CricketUtilsImpl.getInstance().InsertPlayer(winnerTeam, loserTeam, idMap);

		CricketUtilsImpl.getInstance().updateSeries(team1, team2, seriesId);

		printUtils.printSeriesWinner(team1, team2);
	}

	public void reset() {
		team1.reset();
		team2.reset();
	}

	private void generateMatch(Match.MatchType type) {

		CricketUtilsImpl.getInstance().InsertMatch(idMap.get(team1.getName()), idMap.get(team2.getName()), team1, team2,
				seriesId);

		matchId = CricketUtilsImpl.getInstance().GetMatchId();
		match = new Match(matchId, type);
	}

	private void generateTeams() {

		team1 = new Team("India", CricketUtilsImpl.getInstance().getPlayers("India"));
		team2 = new Team("Pakistan", CricketUtilsImpl.getInstance().getPlayers("Pakistan"));

		CricketUtilsImpl.getInstance().InsertTeam(team1, team2);

		idMap = CricketUtilsImpl.getInstance().FetchTeamId(team1.getName(), team2.getName());

		CricketUtilsImpl.getInstance().insertSeries(team1.getName(), team2.getName());

		seriesId = CricketUtilsImpl.getInstance().getSeriesId();
	}

}
