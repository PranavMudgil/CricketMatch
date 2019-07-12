

public class CricketResult {

	private Team winner;
	private Team losing;
	
	public CricketResult(Team win,Team lose) {
		this.winner = win;
		this.losing = lose;
	}

	public Team getWinner() {return winner;}
	public Team getLoser() {return losing;}

}
