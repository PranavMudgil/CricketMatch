
public class CricketGame {

	public static void main(String[] args) {
		
		MatchController controller = new MatchController();
		
		// params: match type & number of matches in series
		controller.initMatch(MatchType.T20,5);
	}

}
