import java.util.ArrayList;
import java.util.List;

public class Team {
	private List<Player> Players;
	private String name;
	private int wickets = 10;
	private int runs = 0;

	
	public void out() {
		wickets--;
	}

	public void addRuns(int n) {
		runs+=n;
	}
	
	public int getWickets() {
		return this.wickets;
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
