
public class Player {

	private String name;
	private int age;
	private int sixes = 0;
	private int fours = 0;
	private int balls = 0;
	private float strikeRate = 0.00f;
	private Type type;
	private float runs = 0f;
	private int wickets = 0;
	private float runs_given = 0f;
	private float overs_taken = 0f;
	private String outby;
	private float economyRate = 0.00f;
	private String out = "not-out";

	public static enum Type {
		BOWLER, BATSMAN, WICKETKEEPER, ALLROUNDER
	}

	public Player(String name, int age, Type type) {
		this.name = name;
		this.age = age;
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	public int getWickets() {
		return wickets;
	}

	public void wicket() {
		wickets++;
	}

	public void addOver() {
		overs_taken++;
	}

	public float getEconomyRate() {
		if (overs_taken == 0)
			return 0.00f;
		economyRate = Utility.round(runs_given / overs_taken, 2);
		return economyRate;
	}

	public void outBy(String name) {
		outby = name;
	}

	public String getOutBy() {
		return outby;
	}

	public int getOversTaken() {
		return (int) overs_taken;
	}

	public String getName() {
		return this.name;
	}

	public int getRunsGiven() {
		return (int) runs_given;
	}

	@Override
	public String toString() {
		String str = ("Name:" + this.name + ", Age:" + this.age + " Category:" + this.type);
		return str;
	}

	public float getStrikeRate() {
		if (runs == 0.0)
			return 0.0f;

		strikeRate = Utility.round(((runs / balls) * 100), 2);

		return strikeRate;
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

	public void addRunsGiven(int n) {
		runs_given += n;
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

	public String getOut() {

		return out;
	}

	public int getRuns() {
		return (int) this.runs;
	}

	public void setOut(boolean status) {
		if (status)
			out = "out";
		else
			out = "not-out";
	}

}
