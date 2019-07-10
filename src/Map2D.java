import java.util.ArrayList;

public class Map2D extends AbstractMap {
	protected Cell cells[]; // 左下を添え字０とする２次元配列を一次元で表現
	protected ArrayList<Gateway> gateways = new ArrayList<Gateway>();

	public Map2D(int size_x, int size_y) {
		this.size.x = size_x;
		this.size.y = size_y;
		cells = new Cell[this.size.x * this.size.y];
		for(int i =0; i < this.size.x * this.size.y; i++) {
			cells[i] = new Cell();
		}
	}

	public boolean allocateGateway(Gateway g, int x, int y) {
		if (0 <= x && x < this.size.x && 0 <= y && y < this.size.y) {
			gateways.add(g);

			int position = x + y * this.size.x;
			cells[position].addGateway(g);

			int left = (x - g.range + this.size.x) % this.size.x;
			int num = g.range * 2 + 1;
			for(int i = 0; i < num; i++) {
				int t = left + y * this.size.x;
				cells[t].setIntencity(1);
				left = (left + 1) % this.size.x;
			}

			int bottom = (y - g.range + this.size.y) % this.size.y;
			for(int i = 0; i < num; i++) {
				int t = x + bottom * this.size.x;
				cells[t].setIntencity(1);
				bottom = (bottom + 1) % this.size.y;
			}

			cells[position].setIntencity(-1);


			return true;
		} else {
			return false;
		}
	}

	public int getIntencity(Pair pos){
		int position = pos.x + pos.y * this.size.x;
		return cells[position].getIntencity();
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
