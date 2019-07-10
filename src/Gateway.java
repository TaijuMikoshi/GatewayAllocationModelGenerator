
public class Gateway {
	String name;
	protected int range = 1;

	public Gateway(String name) {
		this.name = name;
	}
	public Gateway(String name, int range) {
		this.name = name;
		this.range = range;
	}

	public String toString() {
		return name;
	}

	public int getRange() {
		return range;
	}
}
