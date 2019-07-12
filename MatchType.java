
public enum MatchType {
		T20(20), ODI(50);

		private int overs;

		private MatchType(int overs) {
			this.overs = overs;
		}

		public int getValue() {
			return overs;
		}
	}