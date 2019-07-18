
public class CricketGame {

	public static void main(String[] args) {
		
		MatchController controller = MatchController.getInstance();
		
		/*
		* @param matchtype 
		* @param number of matches in series
		*/
		controller.initMatch(Match.MatchType.T20,5);
	}

}
