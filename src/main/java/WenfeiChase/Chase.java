package WenfeiChase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.neo4j.driver.Record;

import entity.Contraint;
import entity.Graph;
import sgbd.GraphDataBaseHanlder;

public class Chase {
	private List<Contraint> sigma;
	private Graph graph;

	public Chase() {
	}

	public Graph getGraph() {
		return graph;
	}

	public List<Contraint> getSigma() {
		return sigma;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public void setSigma(List<Contraint> sigma) {
		sigma = new ArrayList<Contraint>();
		this.sigma = sigma;
	}

	public void compute(Graph graph, List<Contraint> sigma) {
		Pattern lateralConstant = Pattern.compile("([a-zA-Z]\\.[a-zA-Z]=([1-9]?[0-9])|100|[a-zA-Z]\\.[a-zA-Z] = ([1-9]?[0-9])|100)");
		Pattern lateralVariable = Pattern.compile("([a-zA-Z]\\.[a-zA-Z]=[a-zA-Z]\\.[a-zA-Z]|[a-zA-Z]\\.[a-zA-Z] = [a-zA-Z]\\.[a-zA-Z])");
		Pattern lateralID       = Pattern.compile("(.\\.id=.\\.id|.\\.id = .\\.id)");

		while (!sigma.isEmpty()) {
			for (Contraint contraint : sigma) {
				String query;
				List<Record> result;
				
				Matcher matcherConstant = lateralConstant.matcher(contraint.getHead());
				Matcher matcherVariable = lateralVariable.matcher(contraint.getHead());
				Matcher matcherId = lateralID.matcher(contraint.getHead());

				if (matcherConstant.find()) {
					query = contraint.generateQuery();
					GraphDataBaseHanlder databaseHandler = (GraphDataBaseHanlder) graph.getDatabaseHanlder();
					result = databaseHandler.execute(databaseHandler.getDriver(), query);
					databaseHandler.afficherElementInfo(result);
					
				} else if (matcherVariable.find()) {
					query = contraint.generateQuery();
					GraphDataBaseHanlder databaseHandler = (GraphDataBaseHanlder) graph.getDatabaseHanlder();
					result = databaseHandler.execute(databaseHandler.getDriver(), query);
					databaseHandler.afficherElementInfo(result);
				}else if(matcherId.find()) {
					
				}
			}
		}
	}

}
