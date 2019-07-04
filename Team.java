import java.util.ArrayList;
import java.util.List;


public class Team {
	private List<Player> Players;
	private String name;
	private int wickets = 11;
	private int runs = 0;
	private float overs;

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

		for (int i = Players.size()-1; i >= 0; i--) {
			Player p = Players.get(i);
			if (p.getType() == Player.Type.BOWLER || p.getType() == Player.Type.ALLROUNDER) {
				bowlers.add(p);
			}

		}

		return bowlers;
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

}
