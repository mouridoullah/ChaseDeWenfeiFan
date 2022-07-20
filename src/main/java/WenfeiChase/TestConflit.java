package WenfeiChase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Record;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.util.Pair;

import entity.Contraint;
import entity.Graph;
import sgbd.GraphDataBaseHandler;

public class TestConflit {
	private static GraphDataBaseHandler sgbd;
	private static String head;
	private static String subHead;
	private static String queryTestConflit;
	private static String queryTestSatisfaction;
	private static List<Record> resultTestConflit;
	private static List<Record> resultTestSatisfaction;

	public static int conflitAttribut(Contraint contraint, Graph graph) {
		sgbd = graph.getDatabaseHanlder();
		head = contraint.getHead();
		subHead = head.substring(0, 3);

		queryTestConflit = "MATCH " + contraint.getContext() + "\nWHERE " + subHead + " IS NOT NULL" + "\nRETURN *";
		resultTestConflit = sgbd.execute(sgbd.getDriver(), queryTestConflit);

		queryTestSatisfaction = "MATCH " + contraint.getContext() + "\nWHERE " + head + "\nRETURN *";
		resultTestSatisfaction = sgbd.execute(sgbd.getDriver(), queryTestSatisfaction);

		if (!resultTestSatisfaction.isEmpty() && !resultTestConflit.isEmpty()) {
			return 1;
		} else if (!resultTestConflit.isEmpty()) {
			return 2;
		} else {
			return 3;
		}

	}

	public static boolean conflitLabel(Contraint contraint, Graph graph) {
		sgbd = graph.getDatabaseHanlder();
		queryTestConflit = "\nMATCH " + contraint.getContext() + 
							"\nWHERE " + contraint.getBody() +
							"\nRETURN * LIMIT 2";
		
		resultTestConflit = sgbd.execute(sgbd.getDriver(), queryTestConflit);
		
		Value value;
		List<Node> listNode = new ArrayList<Node>();

		for (Record record : resultTestConflit) {
			List<Pair<String, Value>> list = record.fields();
			for (Pair<String, Value> pair : list) {
				value = pair.value();
				if ("NODE".equals(value.type().name())) {
					listNode.add(value.asNode());
				}
			}
		}
		 
		List<Object> listLabel = new ArrayList<Object>();
		
		for (Node node : listNode) {
			listLabel.add(node.labels());
		}
		
		if(listLabel.get(0).equals(listLabel.get(listLabel.size() - 1))) {
			return false;
		}else {
			return true;
		}
	}

	public static Map<String, Object> getNode(Contraint contraint, String sub) {
		String query;
		List<Record> result;
		if (sub == "x") {
			query = "MATCH " + contraint.getContext() + "\nWHERE " + contraint.getBody() + "\nRETURN "
					+ contraint.getHead().substring(0, 1);
			result = sgbd.execute(sgbd.getDriver(), query);
			if (!result.isEmpty()) {
				for (Record record : result) {
					List<Pair<String, Value>> list = record.fields();
					for (Pair<String, Value> pair : list) {
						Value value = pair.value();
						return value.asMap();
					}
				}
			}
		} else if (sub == "y") {
			query = "MATCH " + contraint.getContext() + "\nWHERE " + contraint.getBody() + "\nRETURN "
					+ contraint.getHead().substring(7, 8);
			result = sgbd.execute(sgbd.getDriver(), query);
			if (!result.isEmpty()) {
				for (Record record : result) {
					List<Pair<String, Value>> list = record.fields();
					for (Pair<String, Value> pair : list) {
						Value value = pair.value();
						return value.asMap();
					}
				}
			}
		}
		return null;
	}
}
