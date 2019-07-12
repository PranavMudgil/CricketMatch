

import java.util.ArrayList;
import java.util.List;

public class Team {
	private List<Player> Players;
	private String name;
	private int wickets = 11;
	private int runs = 0;
	private float overs;
	private int seriesScore = 0;

	private ScoreCard scoreCard;

	public void reset() {
		for (Player p : Players) {
			p.reset();
		}
		scoreCard = null;
		wickets = 11;
		runs = 0;
		overs = 0.0f;

	}

	public void out() {
		wickets--;
	}

	public void addRuns(int n) {
		runs += n;
	}

	public int getWickets() {
		return this.wickets;
	}

	public List<Player> getBowlers() {
		List<Player> bowlers = new ArrayList<>();
		for (int i = Players.size() - 1; i >= 0; i--) {
			Player p = Players.get(i);
			if (p.getType() == Player.Type.BOWLER || p.getType() == Player.Type.ALLROUNDER) {
				bowlers.add(p);
			}

		}

		return bowlers;
	}

	public void addSeriesScore() {
		seriesScore++;
	}

	public int getSeriesScore() {
		return seriesScore;
	}

	public float getOver() {
		return this.overs;
	}

	public void setOvers(float over) {
		this.overs = over;
	}

	public void addPlayers(List<Player> team) {
		Players = new ArrayList<>(team);
	}

	public Team(String name, List<Player> players) {
		this.name = name;
		addPlayers(players);
	}

	public String getName() {
		return this.name;
	}

	public int getTotalRuns() {
		return this.runs;
	}

	public Player getPlayer(int n) {
		return Players.get(n);
	}
	
	public ScoreCard getScoreCard(){
		return scoreCard;
	}

	public void setScoreCard(List<Player> player_scores) {
		scoreCard = new ScoreCard(player_scores);
	}
}
