import java.util.*;

public class Cell {
	ArrayList<Gateway> gateways = new ArrayList<Gateway>();
	int intencity = 0;
	
	public List<Gateway> getGateways() {
		return Collections.unmodifiableList(gateways);
	}
	
	int getIntencity() {
		return intencity;
	}
	void setIntencity(int difference) {
		this.intencity += difference;
	}
	
	public boolean addGateway(Gateway g) {
		return this.gateways.add(g);
	}
}
