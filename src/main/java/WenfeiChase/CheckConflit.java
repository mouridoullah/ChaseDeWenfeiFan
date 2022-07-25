/**
 * 
 */
package WenfeiChase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.neo4j.driver.Record;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.util.Pair;

import entity.Contraint;
import entity.Graph;
import sgbd.GraphDataBaseHandler;

/**
 * @author bdvm
 *
 */

public class CheckConflit {
	private static GraphDataBaseHandler sgbd;
	private static String head;
	private static String subHead;
	private static String queryTestConflit;
	private static String queryTestSatisfaction;
	private static List<Record> resultTestConflit;
	private static List<Record> resultTestSatisfaction;
	
	/**
	 * 
	 */


	public static boolean testSatisfaction1(Contraint contraint, Graph graph) {
		sgbd = graph.getDatabaseHanlder();
		head = contraint.getHead();
		subHead = head.substring(0, 3);
		queryTestSatisfaction = "MATCH " + contraint.getContext() + 
								"\nWHERE " + head + 
								"\nRETURN *";
		resultTestSatisfaction = sgbd.execute(sgbd.getDriver(), queryTestSatisfaction);
		
		if(resultTestSatisfaction.isEmpty()) {					
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean testSatisfaction2(Contraint contraint, Graph graph) {
		sgbd = graph.getDatabaseHanlder();
		head = contraint.getHead();
		subHead = head.substring(0, 3);
		queryTestSatisfaction = contraint.getBody() != null ? "MATCH " + contraint.getContext() + 
															  "\nWHERE " + contraint.getBody() + " AND NOT exists("+subHead+")" +
															  "\nRETURN *"
															  : "MATCH " + contraint.getContext() + 
															  "\nWHERE NOT exists("+subHead+")" +
															  "\nRETURN *";
		
		resultTestSatisfaction = sgbd.execute(sgbd.getDriver(), queryTestSatisfaction);
		
		if(resultTestSatisfaction.isEmpty()) {					
			return false;
		} else {
			return true;
		}
	}
	
	public static int conflitAttribut(Contraint contraint, Graph graph) {
		sgbd = graph.getDatabaseHanlder();
		head = contraint.getHead();
		subHead = head.substring(0, 3);
		
		String query = contraint.getBody() != null ? "MATCH " + contraint.getContext() + 
													 "\nWHERE " + contraint.getBody() + 
													 "\nRETURN *"
												   : "MATCH " + contraint.getContext() + 
													 "\nRETURN *";
		
		List<Record>  res = sgbd.execute(sgbd.getDriver(), query);
		
		if(!res.isEmpty()) {
			queryTestConflit = "MATCH " + contraint.getContext() + 
							   "\nWHERE " + subHead + " IS NOT NULL" + 
							   "\nRETURN *";
			resultTestConflit = sgbd.execute(sgbd.getDriver(), queryTestConflit);
			if(!resultTestConflit.isEmpty()) {
				if(testSatisfaction1(contraint, graph)) {
					return 1;
				}else {
					return 2;
				}

			} else {
				return 3;
			}
		} else {
			return 4;
		}
	}

	public static int conflitLabel(Contraint contraint, Graph graph) {
		sgbd = graph.getDatabaseHanlder();
		
		queryTestConflit = contraint.getBody() != null 
												? "MATCH " + contraint.getContext() + 
												  "\nWHERE " + contraint.getBody() +
												  "\nRETURN * LIMIT 2" 
												  
												: "MATCH " + contraint.getContext() + 
												  "\nRETURN * LIMIT 2";
		
		resultTestConflit = sgbd.execute(sgbd.getDriver(), queryTestConflit);
		
		if(!resultTestConflit.isEmpty()) {
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
				return 1;
			}else {
				return 2;
			}
		} else {
			return 3;
		}
	}

	public static Map<String, Object> getNode(Graph graph, Contraint contraint, String sub) {
		head = contraint.getHead();
		sgbd = graph.getDatabaseHanlder();
		String query;
		List<Record> result;
		final String res;
		
		Pattern literalID = Pattern.compile("(.\\.id=.\\.id|.\\.id = .\\.id)");
		Matcher matcherId = literalID.matcher(head);
		
		Pattern literalVariable = Pattern
				.compile("([a-zA-Z]\\.[a-zA-Z]=[a-zA-Z]\\.[a-zA-Z]|[a-zA-Z]\\.[a-zA-Z] = [a-zA-Z]\\.[a-zA-Z])");
		Matcher matcherVariable = literalVariable.matcher(head);
		
		if (matcherId.find()) {
		
			res = sub == "x" 
					  ? contraint.getHead().substring(0, 1) 
					  : contraint.getHead().substring(7, 8);
		} else if(matcherVariable.find()){
			res = sub == "x" 
					  ? contraint.getHead().substring(0, 1) 
					  : contraint.getHead().substring(6, 7);
		} else {
			res =  contraint.getHead().substring(0, 1); 
		}
		
		
		query = "MATCH " + contraint.getContext() + "\nWHERE " + contraint.getBody() + "\nRETURN "
				+ res;
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
		return null;
	}
}
