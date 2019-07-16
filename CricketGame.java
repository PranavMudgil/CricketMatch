
public class CricketGame {

	public static void main(String[] args) {
		
		MatchController controller = MatchController.getInstance();
		
		// params: match type & number of matches in series
		controller.initMatch(MatchType.T20,5);
	}

}
