
import java.util.ArrayList;
import java.util.List;

public class ScoreCard {

	List<Player> list = new ArrayList<>();
	
	
	public ScoreCard(List<Player> list) {
		this.list = list;
	}
	
	public List<Player> getList(){return list;}

}
