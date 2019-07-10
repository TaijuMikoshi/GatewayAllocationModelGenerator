public abstract class AbstractMap {
	Pair size = new Pair(0, 0);
	protected double coverage = 0.0;

	public double getCoverage() {
		return coverage;
	}

	public Pair getSize() {
		return size;
	}

	public abstract int getIntencity(Pair pos);
	public abstract int getMaxIntencity();
	public abstract int getNumOfGateway();
}
