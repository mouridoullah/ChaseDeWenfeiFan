package WenfeiChase;

import java.util.ArrayList;
import java.util.List;
import entity.Contraint;
import entity.Graph;

public class Main {
	public static void main(String[] args) throws Exception {
		try (Graph graph = new Graph()) {
			graph.deleteGraphe();
			graph.createGraphe();

			List<Contraint> sigma = new ArrayList<Contraint>();
			
			
			
			Contraint contraint1  = new Contraint("(x:Company)-[]-(y:City)", "x.p = 5, x.b = 2, x.a = 1", "x.z = 900");
			Contraint contraint2  = new Contraint("(x:Person)-[]->(y:Company)", "x.a = y.a, x.s = 1", "x.b = 5");
			Contraint contraint3  = new Contraint("(x:Company)-[]->(y:City)", "x.c = 3", "x.l = 800");
			Contraint contraint4  = new Contraint("(x:Person)-[]->(y:Company)", "x.a = y.a", "x.s = 1");
			Contraint contraint5  = new Contraint("(x:Person), (y:Person)", "x.a = 1, y.b = 2", "y.a = 1");
			Contraint contraint6  = new Contraint("(x:Person)", null, "x.u = 25");
			Contraint contraint7  = new Contraint("(x:Person)", "x.b = 2, x.a = 1", "x.u = 25");
			Contraint contraint8  = new Contraint("(x:Person)-[]->(y:Car)", "x.s = 1", "y.i = x.a");
			Contraint contraint9  = new Contraint("(x:Person)-[]->(y:Cat)", "x.c = 3", "y.k = x.c");
			Contraint contraint10 = new Contraint("(x:Person)-[]->(y:Cat)", "x.c = 3", "y.k = 4");
			Contraint contraint11 = new Contraint("(x:City), (y:City)", "x.d = 4, y.o = 2, x.g = 3", "x.id = y.id");
			Contraint contraint12 = new Contraint("(x:Person)-[]->(y:Person)", "x.a = 1, y.b = 2", "x.id = y.id");
			Contraint contraint13 = new Contraint("(x:Company), (y:City)", "x.a = 1, y.o = 2", "x.id = y.id");	
			Contraint contraint14 = new Contraint("(x:Person), (y:Person)", "x.s = 1, y.f = 2", "x.id = y.id") ;	
			Contraint contraint15 = new Contraint("(x:Company), (y:Company)", "x.o = 2, x.c = 3, y.p = 5", "x.id = y.id");	
			Contraint contraint16 = new Contraint("(x:Company), (y:Person)", "x.c = 3, y.f = 2, y.c = x.c, x.d = 4", "x.id = y.id");	
			

			
			
			sigma.add(contraint1);
			sigma.add(contraint2);
			sigma.add(contraint3);
			sigma.add(contraint4);
			sigma.add(contraint5);
			sigma.add(contraint6);
			sigma.add(contraint7);
			sigma.add(contraint8);
			sigma.add(contraint9);
			sigma.add(contraint10);
			sigma.add(contraint11);
			sigma.add(contraint12);
			sigma.add(contraint13);
			sigma.add(contraint14);
			sigma.add(contraint15);
			sigma.add(contraint16);


			Chase.compute(graph, sigma);
		}

	}
}
