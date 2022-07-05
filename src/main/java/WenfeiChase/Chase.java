package WenfeiChase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.neo4j.driver.Record;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.util.Pair;

import entity.Contraint;
import entity.Graph;
import sgbd.GraphDataBaseHandler;

public class Chase {
	public static void compute(Graph graph, List<Contraint> sigma) {

		Pattern literalConstant = Pattern
				.compile("([a-zA-Z]\\.[a-zA-Z]=([1-9]?[0-9])|100|[a-zA-Z]\\.[a-zA-Z] = ([1-9]?[0-9])|100)");
		Pattern literalVariable = Pattern
				.compile("([a-zA-Z]\\.[a-zA-Z]=[a-zA-Z]\\.[a-zA-Z]|[a-zA-Z]\\.[a-zA-Z] = [a-zA-Z]\\.[a-zA-Z])");
		Pattern literalID = Pattern.compile("(.\\.id=.\\.id|.\\.id = .\\.id)");

		for (Contraint contraint : sigma) {
			String query;
			List<Record> resultTest;
			List<Record> result;
			GraphDataBaseHandler sgbd = graph.getDatabaseHanlder();
			String head;
			String subHead;
			String queryTest;

			Matcher matcherConstant = literalConstant.matcher(contraint.getHead());
			Matcher matcherVariable = literalVariable.matcher(contraint.getHead());
			Matcher matcherId = literalID.matcher(contraint.getHead());

			if (matcherConstant.find()) {
				head = contraint.getHead();
				subHead = head.substring(0, 3);

				queryTest = "MATCH " + contraint.getContext() + "\nWHERE " + head + "\nOR " + subHead + " IS NOT NULL"
						+ "\nRETURN *";

				resultTest = sgbd.execute(sgbd.getDriver(), queryTest);
				// GraphDataBaseHanlder.afficherElementInfo(resultTest);

				if (!resultTest.isEmpty()) {
					System.out.println("Conflit d'attribut pour la requete \n" + contraint.generateQuery());
					System.out.println("\n---------------------\n");
					continue;
				} else {
					query = contraint.generateQuery();
					result = sgbd.execute(sgbd.getDriver(), query);
					GraphDataBaseHandler.afficherElementInfo(result);
					System.out.println("\n---------------------\n");
				}

			} else if (matcherVariable.find()) {
				head = contraint.getHead();
				subHead = head.substring(0, 3);

				queryTest = "MATCH " + contraint.getContext() + "\nWHERE " + contraint.getHead() + "\nOR " + subHead
						+ " IS NOT NULL" + "\nRETURN *";

				resultTest = sgbd.execute(sgbd.getDriver(), queryTest);

				if (!resultTest.isEmpty()) {
					System.out.println("Conflit d'attribut pour la requete \n" + contraint.generateQuery());
					System.out.println("\n---------------------\n");
					continue;
				} else {
					query = contraint.generateQuery();
					result = sgbd.execute(sgbd.getDriver(), query);
					GraphDataBaseHandler.afficherElementInfo(result);
					System.out.println("\n---------------------\n");
				}

			} else if (matcherId.find()) {
				queryTest = "MATCH " + contraint.getContext() + " RETURN * LIMIT 2";
				resultTest = sgbd.execute(sgbd.getDriver(), queryTest);

				Value value;
				List<Node> listNode = new ArrayList<Node>();
				String typName;
				Node node1;
				Node node2;

				if (!resultTest.isEmpty()) {
					for (Record record : resultTest) {

						List<Pair<String, Value>> list = record.fields();

						for (Pair<String, Value> pair : list) {
							value = pair.value();
							typName = value.type().name();

							if ("NODE".equals(typName)) {
								listNode.add(value.asNode());
							}
						}
					}
					node1 = listNode.get(0);
					node2 = listNode.get(listNode.size() - 1);

					if (!node1.labels().equals(node2.labels())) {
						System.out.println("Conflit de label pour la requete\n" + contraint.generateQuery());
						System.out.println("\n---------------------\n");
						continue;
					} else {
						query = contraint.generateQuery();
						result = sgbd.execute(sgbd.getDriver(), query);
						GraphDataBaseHandler.afficherElementInfo(result);
						System.out.println("\n---------------------\n");
					}
				}

			}
		}

	}

}
