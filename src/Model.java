import java.io.FileWriter;
import java.io.IOException;

public class Model {

	public void makeModel(AbstractMap map, String fname) {
		Pair size = map.getSize();
		if(size.y == 0) size.y = 1;

		try {
			FileWriter fw = new FileWriter(fname);

			makeGlobalParameters(map, size, fw);
			make_bel_right(map, size ,fw);
			make_move_right(map, size ,fw);
			if(size.y > 1)
				make_move_up(map, size ,fw);
			make_normalize(map, size ,fw);
			make_updateBelief(map, size ,fw);
			make_estimation(map, size ,fw);
			make_notConverge(map, size ,fw);

			fw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void makeGlobalParameters(AbstractMap map, Pair size, FileWriter fw) throws IOException{
		fw.write("#define X " + size.x + "\n");
		fw.write("#define Y " + size.y + "\n");
		fw.write("#define P " + size.x * size.y + "\n\n");

		fw.write("// map and structures\n");
		for(int y = size.y - 1; y >=0; y--) {
			fw.write("int m" + y + "[X] = {" + map.getIntencity(new Pair(0, y)));
			for(int x = 1; x < size.x; x++) {
				fw.write("," + map.getIntencity(new Pair(x, y)));
			}
			fw.write("};\n");
		}
		fw.write("\n");

		fw.write("// belief\n");
		for(int y = size.y - 1; y >=0; y--) {
			fw.write("int bel" + y + "[X] = {P");
			for(int x = 1; x < size.x; x++) {
				fw.write(",P");
			}
			fw.write("};\n");
		}
		fw.write("\n");


		fw.write("// \\overline{belief}\n");
		for(int y = size.y - 1; y >=0; y--) {
			fw.write("int _bel" + y + "[X];\n");
		}
		fw.write("\n\n");
	}

	private void make_bel_right(AbstractMap map, Pair size, FileWriter fw) throws IOException{
		fw.write("inline _bel_right(u) {\n");
		fw.write("\t" + "int i;\n");
		fw.write("\n");
		fw.write("\t" + "for (i : 0 .. X-1) {\n");
		for(int y = size.y - 1; y >=0; y--) {
			fw.write("\t\t" + "_bel" + y + "[i] = bel" + y + "[(i - u + X) % X];\n");
		}
		fw.write("\t" + "}\n");
		fw.write("}\n");
		fw.write("\n");
	}

	private void make_move_right(AbstractMap map, Pair size, FileWriter fw) throws IOException{
		fw.write("inline move_right() {\n");
		fw.write("\t" + "_bel_right(1);\n");
		fw.write("}\n\n");
	}

	private void make_move_up(AbstractMap map, Pair size, FileWriter fw) throws IOException{
		fw.write("inline move_up() {\n");
		fw.write("\t" + "int i;\n");
		fw.write("\n");
		fw.write("\t" + "for (i : 0 .. X-1) {\n");
		for(int y = size.y - 1; y >=0; y--) {
			fw.write("\t\t" + "_bel" + y + "[i] = bel" + (y + (size.y -1)) % size.y + "[i];\n");
		}
		fw.write("\t" + "}\n");
		fw.write("}\n\n");
	}

	private void make_normalize(AbstractMap map, Pair size, FileWriter fw) throws IOException{
		fw.write("inline normalize() {\n");
		fw.write("\t" + "int cnt = 0;\n");
		fw.write("\t" + "for (i : 0 .. X-1) {\n");
		for(int y = 0; y < size.y; y++) {
			fw.write("\t\t" + "if\n");
			fw.write("\t\t" + ":: _bel" + y + "[i] == 1 -> cnt++\n");
			fw.write("\t\t" + ":: else -> skip\n");
			fw.write("\t\t" + "fi;\n");
		}
		fw.write("\t" + "}\n");

		fw.write("\n");
		fw.write("\t" + "for (i : 0 .. X-1) {\n");
		for(int y = 0; y < size.y; y++) {
			fw.write("\t\t" + "if\n");
			fw.write("\t\t" + ":: _bel" + y + "[i] == 1 -> bel" + y + "[i] = cnt\n");
			fw.write("\t\t" + ":: else -> bel" + y + "[i] = 0\n");
			fw.write("\t\t" + "fi;\n");
		}
		fw.write("\t" + "}\n");

		fw.write("}\n\n");
	}

	private void make_updateBelief(AbstractMap map, Pair size, FileWriter fw) throws IOException{
		fw.write("inline updateBelief(z) {\n");
		fw.write("\t" + "int i;\n");
		fw.write("\t" + "for (i : 0 .. X-1) {\n");
		for(int y = 0; y < size.y; y++) {
			fw.write("\t\t" + "if\n");
			fw.write("\t\t" + ":: (m" + y + "[i] == z && _bel" + y + "[i] != 0) -> _bel" + y + "[i] = 1;\n");
			fw.write("\t\t" + ":: else -> _bel" + y + "[i] = 0;\n");
			fw.write("\t\t" + "fi;\n");
		}
		fw.write("\t" + "}\n");

		fw.write("\n");

		fw.write("\t" + "normalize();\n");
		fw.write("}\n\n");
	}

	private void make_estimation(AbstractMap map, Pair size, FileWriter fw) throws IOException{
		//int numGw = map.getNumOfGateway();
		int maxIntent = map.getMaxIntencity();
		fw.write("active proctype estimation() {\n");
		fw.write("\t" + "int j;\n");
		fw.write("\t" + "int z;\n");
		fw.write("\t" + "bool occur[" + (maxIntent + 1) + "];\n");

		fw.write("\n");

		fw.write("\t" + "do\n");
		fw.write("\t" + "::true ->\n");
		fw.write("\t\t" + "// obtain non-deterministic observation value\n");
		fw.write("\t\t" + "for (j : 0 .. " + maxIntent + ") {\n");
		fw.write("\t\t\t" + "occur[j] = false;\n");
		fw.write("\t\t" + "}\n");
		fw.write("\t\t" + "for (j : 0 .. X-1) {\n");
		for(int y = 0; y < size.y; y++) {
			fw.write("\t\t\t" + "if\n");
			fw.write("\t\t\t" + ":: bel" + y + "[j] != 0 -> occur[m" + y + "[j]] = true;\n");
			fw.write("\t\t\t" + ":: else -> skip\n");
			fw.write("\t\t\t" + "fi;\n");
		}
		fw.write("\t\t" + "}\n");

		fw.write("\n");
		fw.write("\t\t" + "// movement\n");
		fw.write("\t\t" + "if\n");
		for(int i = 0; i < maxIntent + 1; i++) {
			fw.write("\t\t" + ":: occur[" + i + "] -> z = "+ i +";\n");
		}
		fw.write("\t\t" + "fi\n");

		fw.write("\n");
		if(size.y == 1) {
			fw.write("\t\t" + "move_right();\n");

		}else {
			fw.write("\t\t" + "if\n");
			fw.write("\t\t" + ":: (z == 0) -> move_right();\n");
			for(int i = 1; i < maxIntent + 1; i++) {
				fw.write("\t\t" + ":: (z == " + i + ") -> move_up();\n");
			}
			fw.write("\t\t" + "fi\n");
		}

		fw.write("\n");
		fw.write("\t\t" + "// observation\n");
		fw.write("\t\t" + "updateBelief(z);\n");
		fw.write("\t" + "od;\n");
		fw.write("}\n");
	}

	private void make_notConverge(AbstractMap map, Pair size, FileWriter fw) throws IOException{
		fw.write("ltl notConverge {\n");
		fw.write("\t" + "! <>(\n");

		fw.write("\t\t" + "bel0[0] == 1");
		for(int x = 1; x < size.x; x++) {
			fw.write(" || bel0[" + x + "] == 1 ");
		}
		fw.write("\n");

		for(int y = 1; y < size.y; y++) {
			fw.write("\t\t");
			for(int x = 0; x < size.x; x++) {
				fw.write("|| bel" + y + "[" + x + "] == 1 ");
			}
			fw.write("\n");
		}
		fw.write("\t" + ")\n");
		fw.write("}\n");
	}
}
