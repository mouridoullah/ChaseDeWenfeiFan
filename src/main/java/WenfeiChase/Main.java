package WenfeiChase;

import java.util.ArrayList;
import java.util.List;


import entity.Contraint;
import entity.Graph;

public class Main {
	public static void main(String[] args) throws Exception {

		try (Graph graph = new Graph()) {

			graph.createGraphe();
			
			List<Contraint> sigma = new ArrayList<Contraint>();
			Contraint contraint1 = new Contraint("(x:User)-[:rel]->(y:User)", "x.d = y.d", "x.p = 1");
			Contraint contraint2 = new Contraint("(x:User)-[:rel]->(y:User)", "x.a = y.a", "x.b = 1");
			Contraint contraint3 = new Contraint("(x:User)-[:rel]->(y:User)", "x.d = y.d", "x.c = y.c");
			Contraint contraint4 = new Contraint("(x:User)-[:rel]->(y:User)", "x.a = y.a", "x.q = y.b");
			
			sigma.add(contraint1);
			sigma.add(contraint2);
			sigma.add(contraint3);
			sigma.add(contraint4);

			
			Chase.compute(graph, sigma);
			
			graph.deleteGraphe();
			
		}

	}
}
