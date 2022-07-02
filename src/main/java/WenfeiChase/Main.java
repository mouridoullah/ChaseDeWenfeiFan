package WenfeiChase;

import java.util.List;

import org.neo4j.driver.Record;

import entity.Graph;
import sgbd.GraphDataBaseHanlder;

public class Main {
	public static void main(String[] args) throws Exception {

		try (Graph graph = new Graph()) {

			graph.createGraphe();
			GraphDataBaseHanlder sgbd = graph.getDatabaseHanlder();

			String query = "MATCH (u:User)-[r:rel]-() " + "RETURN u, r";
			List<Record> result = sgbd.execute(sgbd.getDriver(), query);

			sgbd.afficherElementInfo(result);

			graph.deleteGraphe();
		}

	}
}
