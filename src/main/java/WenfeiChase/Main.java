package WenfeiChase;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.Record;

import entity.Contraint;
import entity.Graph;
import sgbd.GraphDataBaseHanlder;

public class Main {
	public static void main(String[] args) throws Exception {

		try (Graph graph = new Graph()) {

			graph.createGraphe();
			//GraphDataBaseHanlder sgbd = graph.getDatabaseHanlder();
			List<Contraint> sigma = new ArrayList<Contraint>();
			Contraint contraint = new Contraint("(x:User)-[:rel]->(y:User)", "x.d = y.d", "x.c = 1");
			sigma.add(contraint);
			
			Chase.compute(graph, sigma);

			GraphDataBaseHanlder sgbd = graph.getDatabaseHanlder();
			String query = "MATCH (x:User)-[:rel]->(y:User) WHERE x.d = y.d AND x.c = 1 RETURN x";
			List<Record> result = sgbd.execute(sgbd.getDriver(), query);

			GraphDataBaseHanlder.afficherElementInfo(result);

			//graph.deleteGraphe();
		}

	}
}
