import java.util.ArrayList;

public class Map1D extends AbstractMap {
	protected Cell cells[];
	protected ArrayList<Gateway> gateways = new ArrayList<Gateway>();

	public Map1D(int size_x) {
		this.size.x = size_x;
		cells = new Cell[this.size.x];
		for(int i =0; i < this.size.x; i++) {
			cells[i] = new Cell();
		}
	}

	public boolean allocateGateway(Gateway g, int position) {
		if (0 <= position && position < cells.length) {
			gateways.add(g);
			cells[position].addGateway(g);

			int left = (position - g.range + cells.length) % cells.length;
			int num = g.range * 2 + 1;
			for(int i = 0; i < num; i++) {
				cells[left].setIntencity(1);
				left = (left + 1) % cells.length;
			}
			return true;
		} else {
			return false;
		}
	}

	public int getIntencity(Pair pos){
		return cells[pos.x].getIntencity();
	}

	public int getMaxIntencity() {
		int max = 0;
		for(int i = 0 ; i < size.x; i++) {
			int tmp = cells[i].getIntencity();
			if(tmp > max) max = tmp;
		}
		return max;
	}

	public int getNumOfGateway() {
		return gateways.size();
	}
}
