import java.util.List;

public class CricketScoreResult {

	private List<Player> winnerScoreBoard;
	private List<Player> loserScoreBoard;
	
	private Team winner;
	private Team losing;
	
	public CricketScoreResult(Team win,Team lose, List<Player> winner, List<Player> loser) {
		this.winner = win;
		this.losing = lose;
		this.winnerScoreBoard = winner;
		this.loserScoreBoard = loser;
	}
	
	public Team getWinner() {return winner;}
	public Team getLoser() {return losing;}
	
	public List<Player> getWinningBoard(){return winnerScoreBoard;}
	public List<Player> getLosingScoreBoard(){return loserScoreBoard;}
}
