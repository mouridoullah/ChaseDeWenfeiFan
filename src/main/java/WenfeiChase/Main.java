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
			Contraint contraint1 = new Contraint("(x:Company), (y:City)", "x.d = y.d", "x.z = 900");
			Contraint contraint2 = new Contraint("(x:Person)-[]->(y:Company)", "x.a = y.a", "x.b = 5");
			Contraint contraint3 = new Contraint("(x:Person)-[]->(y:Car)", "x.s = 1", "y.i = x.a");
			Contraint contraint4 = new Contraint("(x:Person)-[]->(y:Cat)", "x.c = 3", "y.k = x.c");
			Contraint contraint5 = new Contraint("(x:Company{name:'Company1'})-[]-(y:City{name:'London'})", "x.a = y.a", "x.id = y.id");	
			Contraint contraint6 = new Contraint("(x:Person{s:1})-[]-(y:Person{f:2})", "x.s = 1 AND y.f = 2", "x.id = y.id");	
			Contraint contraint7 = new Contraint("(x:City{g:3}), (y:City{o:2})", "x.s = 1 AND y.f = 2", "x.id = y.id");	
			Contraint contraint8 = new Contraint("(x:Company{o:2, c:3}), (y:Company{p:5})", "x.s = 1 AND y.f = 2", "x.id = y.id");	
			Contraint contraint9 = new Contraint("(x:Company{o:2, c:3}), (y:Person {s:1})", "x.s = 1 AND y.f = 2", "x.id = y.id");	
			
			sigma.add(contraint1);
			sigma.add(contraint2);
			sigma.add(contraint3);
			sigma.add(contraint4);
			sigma.add(contraint5);
			sigma.add(contraint6);
			sigma.add(contraint7);
			sigma.add(contraint8);
			sigma.add(contraint9);
			
			Chase.compute(graph, sigma);
			
		}
	}
}
