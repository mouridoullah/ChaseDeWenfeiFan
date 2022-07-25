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

			/*
			 * -------------------------------------------- constant literal x.A = c
			 * ------------------------------------------------
			 */

			// (Company{"p":5,"a":1,"alias":"Company1","b":2})-[]-(City{"name":"London","c":3,"d":4,"g":3})
			Contraint contraint1 = new Contraint("(x:Company)-[]-(y:City)", "x.p = 5, x.b = 2, y.g = 3", "x.z = 900");

			// (Person{"a":1,"b":2,"firstname":"toto","s":1})-[]->(Company{"p":5,"a":1,"alias":"Company1","b":2})
			Contraint contraint2 = new Contraint("(x:Person)-[]->(y:Company)", "x.a = y.a, x.s = 1", "x.b = 5");

			// (Company{"name":"Company2","c":3,"d":4,"o":2})-[]->(City{"name":"Liverpool","c":3,"d":4,"o":2})
			Contraint contraint3 = new Contraint("(x:Company)-[]->(y:City)", "x.c = 3", "x.l = 800");

			// (Person{"a":1,"b":2,"firstname":"toto","s":1})-[]->(Company{"p":5,"a":1,"alias":"Company1","b":2})
			Contraint contraint4 = new Contraint("(x:Person)-[]->(y:Company)", "x.a = y.a", "x.s = 1");

			// (Person{"t":4,"m":1})-[]->(Person{"w":2})-[]->(Person{"w":2})
			Contraint contraint5 = new Contraint("(x:Person)-[]->(y:Person)", "x.m = 1, y.w = 2", "y.m = 1");

			// (Person)
			Contraint contraint6 = new Contraint("(x:Person)", null, "x.u = 25");

			// (Person{"a":1,"b":2,"firstname":"toto","s":1})
			Contraint contraint7 = new Contraint("(x:Person)", "x.b = 2, x.a = 1", "x.u = 25");

			/*
			 * -------------------------------------------- variable literal x.A = y.B
			 * ------------------------------------------------
			 */

			// (Person{"a":1,"b":2,"firstname":"toto","s":1})-[]->(Car{"brand":"Ferrari"})
			Contraint contraint8 = new Contraint("(x:Person)-[]->(y:Car)", "x.s = 1", "y.i = x.a");

			// (Person{"c":3,"d":4,"f":2,"lastname":"titi"})-[]->(Cat"name":"Derby","k":4})
			Contraint contraint9 = new Contraint("(x:Person)-[]->(y:Cat)", "x.c = 3", "y.k = x.c");

			// (Person{"c":3,"d":4,"f":2,"lastname":"titi"})-[]->(Cat{"name":"Derby","k":4})
			Contraint contraint10 = new Contraint("(x:Person)-[]->(y:Cat)", "x.c = 3", "y.k = x.d");

			/*
			 * -------------------------------------------- id literal x.id = y.id.
			 * ------------------------------------------------
			 */

			// (City{"name":"London","c":3,"d":4,"g":3}),
			// (City{"name":"Liverpool","c":3,"d":4,"o":2})
			Contraint contraint11 = new Contraint("(x:City), (y:City)", "x.d = 4, y.o = 2, x.g = 3", "x.id = y.id");

			// (Person{"t":4,"m":1})-[]->(Person{"w":2})
			Contraint contraint12 = new Contraint("(x:Person)-[]->(y:Person)", "x.m = 1, y.w = 2", "x.id = y.id");

			// (Company{"p":5,"a":1,"alias":"Company1","b":2}),
			// (City{"name":"Liverpool","c":3,"d":4,"o":2})
			Contraint contraint13 = new Contraint("(x:Company), (y:City)", "x.a = 1, y.o = 2", "x.id = y.id");

			// (Person{"a":1,"b":2,"firstname":"toto","s":1}),
			// (Person{"c":3,"d":4,"f":2,"lastname":"titi"})
			Contraint contraint14 = new Contraint("(x:Person), (y:Person)", "x.s = 1, y.f = 2", "x.id = y.id");

			// (Person{"a":1,"u":25})-[]->(Person{"a":1,"b":2,"j":3,"u":25})-[]->(Person{"u":25,"b":2})
			Contraint contraint15 = new Contraint("(x:Person)-[]->(y:Person)", "x.b = 2, y.u = 25", "x.id = y.id");

			// (Company{"name":"Company2","c":3,"d":4,"o":2}),
			// (Company{"p":5,"a":1,"alias":"Company1","b":2})
			Contraint contraint16 = new Contraint("(x:Company), (y:Company)", "x.o = 2, x.c = 3, y.p = 5",
					"x.id = y.id");

			// (Company{"name":"Company2","c":3,"d":4,"o":2}),
			// (Person{"c":3,"d":4,"f":2,"lastname":"titi"})
			Contraint contraint17 = new Contraint("(x:Company), (y:Person)", "x.c = 3, y.f = 2, y.c = x.c, x.d = 4",
					"x.id = y.id");

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
			sigma.add(contraint17);

			Chase.compute(graph, sigma);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}