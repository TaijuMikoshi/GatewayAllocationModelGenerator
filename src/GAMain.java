
public class GAMain {

	public static void main(String[] args) {
		GAMain main = new GAMain();
		main.exec();
		main.exec2D();
	}

	protected void exec() {
		int size_x = 8;
		Map1D map = new Map1D(size_x);

		System.out.println("map: " + map);
		System.out.println("coverage = " + map.getCoverage());

		map.allocateGateway(new Gateway("g0" ,1), 1);
		map.allocateGateway(new Gateway("g2" ,1), 4);

		Model m = new Model();
		m.makeModel(map,  "1dMap.pml");
	}

	protected void exec2D() {
		int size_x = 4;
		int size_y = 4;
		Map2D map = new Map2D(size_x, size_y);

		System.out.println("map: " + map);
		System.out.println("coverage = " + map.getCoverage());

		map.allocateGateway(new Gateway("g0" ,1), 0,1);
		map.allocateGateway(new Gateway("g1" ,1), 0,3);
		map.allocateGateway(new Gateway("g2" ,1), 2,1);
		map.allocateGateway(new Gateway("g3" ,1), 2,3);

		Model m = new Model();
		m.makeModel(map,  "2dMap.pml");
	}
}
