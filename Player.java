
public class Player {

	private String name;
	private int age;
	private int sixes = 0;
	private int fours = 0;
	private int balls = 0;
	private double strikeRate = 0.00;
	private Type type;
	private int runs = 0;

	public static enum Type {
		BOWLER, BATSMAN, WICKETKEEPER, ALLROUNDER
	}

	public Player(String name, int age, Type type) {
		this.name = name;
		this.age = age;
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		String str = ("Name:" + this.name + ", Age:" + this.age + " Category:" + this.type);
		return str;
	}

	public double getStrikeRate() {
		if (runs == 0)
			return 0;

		return ((runs / balls) * 100);
	}

	public void addRuns(int n) {
		runs += n;
	}

	public void addballs() {
		balls++;
	}

	public void addSix() {
		sixes++;
	}

	public void addFour() {
		fours++;
	}

	public int getSixes() {
		return sixes;
	}

	public int getFours() {
		return fours;
	}

	public int getBalls() {
		return balls;
	}

	public int getRuns() {
		return this.runs;
	}

}
