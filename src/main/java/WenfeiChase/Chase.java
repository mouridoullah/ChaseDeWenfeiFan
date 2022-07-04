package WenfeiChase;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.neo4j.driver.Record;

import entity.Contraint;
import entity.Graph;
import sgbd.GraphDataBaseHanlder;

public class Chase {
	public static void compute(Graph graph, List<Contraint> sigma) {
		Pattern lateralConstant = Pattern
				.compile("([a-zA-Z]\\.[a-zA-Z]=([1-9]?[0-9])|100|[a-zA-Z]\\.[a-zA-Z] = ([1-9]?[0-9])|100)");
		Pattern lateralVariable = Pattern
				.compile("([a-zA-Z]\\.[a-zA-Z]=[a-zA-Z]\\.[a-zA-Z]|[a-zA-Z]\\.[a-zA-Z] = [a-zA-Z]\\.[a-zA-Z])");
		Pattern lateralID = Pattern.compile("(.\\.id=.\\.id|.\\.id = .\\.id)");

		for (Contraint contraint : sigma) {
			String query;
			List<Record> result;

			Matcher matcherConstant = lateralConstant.matcher(contraint.getHead());
			Matcher matcherVariable = lateralVariable.matcher(contraint.getHead());
			Matcher matcherId = lateralID.matcher(contraint.getHead());

			if (matcherConstant.find()) {
				// TODO Faire un test pour conflit de d'attribut
				query = contraint.generateQuery();
				GraphDataBaseHanlder sgbd = graph.getDatabaseHanlder();
				result = sgbd.execute(sgbd.getDriver(), query);
				GraphDataBaseHanlder.afficherElementInfo(result);
				
			} else if (matcherVariable.find()) {
				// TODO Faire un test pour conflit de d'attribut
				query = contraint.generateQuery();
				GraphDataBaseHanlder sgbd = graph.getDatabaseHanlder();
				result = sgbd.execute(sgbd.getDriver(), query);
				GraphDataBaseHanlder.afficherElementInfo(result);
				
			} else if (matcherId.find()) {
				// TODO Faire un test pour conflit de label de d'attribut
				// TODO la partie fusionner des nodes
				/*
				 * faire un test sur le degree du node()
				 * creer un nouveau node 
				 * faire l'union des attribut
				 * rapporter les arretes sur le nouveau node
				 * supprimer les nodes avec DETACH DELETE
				 * */
			}
		}

	}

}
