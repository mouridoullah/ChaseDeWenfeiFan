package WenfeiChase;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.neo4j.driver.Record;
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

		String query;
		List<Record> result;
		GraphDataBaseHandler sgbd = graph.getDatabaseHanlder();
		int resultatTest;

		for (Contraint contraint : sigma) {
			Matcher matcherConstant = literalConstant.matcher(contraint.getHead());
			Matcher matcherVariable = literalVariable.matcher(contraint.getHead());
			Matcher matcherId = literalID.matcher(contraint.getHead());

			if (matcherConstant.find()) {
				resultatTest = TestConflit.conflitAttribut(contraint, graph);

				if (resultatTest == 1) {
					System.out.println(contraint.afficherRequete()+"\n");
					System.out.println("Le graphe satisfait déjà la contrainte :\n" + contraint.afficherContraint());
					System.out.println("\n---------------------------------------------------------------\n");
					continue;
				} else if (resultatTest == 2) {
					System.out.println(contraint.afficherRequete()+"\n");
					query = "MATCH " + contraint.getContext() + " WHERE " + contraint.getBody() + " RETURN "
							+ contraint.getHead().substring(0, 1); // + "{.*}";
					result = sgbd.execute(sgbd.getDriver(), query);
					GraphDataBaseHandler.afficherElementInfo(result);

					System.out.println(
							"\nConflit de valeur d'attribut car " + contraint.getHead().substring(0, 3) + " est non nul");
					System.out.println("\n---------------------------------------------------------------\n");
					continue;
				} else {
					appliquerContraint(sgbd, contraint);
				}

			} else if (matcherVariable.find()) {
				resultatTest = TestConflit.conflitAttribut(contraint, graph);
				if (resultatTest == 1) {
					System.out.println(contraint.afficherRequete()+"\n");
					System.out.println("Le graphe satisfait déjà la contrainte \n" + contraint.afficherContraint());
					System.out.println("\n---------------------------------------------------------------\n");
					continue;
				} else if (resultatTest == 2) {
					System.out.println(contraint.afficherRequete()+"\n");
					query = "MATCH " + contraint.getContext() + " WHERE " + contraint.getBody() + " RETURN "
							+ contraint.getHead().substring(0, 1); // + "{.*}";
					result = sgbd.execute(sgbd.getDriver(), query);
					GraphDataBaseHandler.afficherElementInfo(result);
					System.out.println(
							"Conflit de valeur d'attribut car " + contraint.getHead().substring(0, 3) + " est non nul");
					System.out.println("\n---------------------------------------------------------------\n");
					continue;
				} else {
					appliquerContraint(sgbd, contraint);
				}

			} else if (matcherId.find()) {
				if (TestConflit.conflitLabel(contraint, graph)) {
					System.out.println(contraint.afficherRequete()+"\n");
					System.out.println("Conflit de label pour la contrainte :\n" + contraint.afficherContraint());
					System.out.println("\n---------------------------------------------------------------\n");
					continue;
				} else {
					Map<String, Object> mapX = TestConflit.getNode(contraint, "x");
					Map<String, Object> mapY = TestConflit.getNode(contraint, "y");

					boolean found = false;
					for (String key1 : mapX.keySet()) {
						for (String key2 : mapY.keySet()) { 
							if (key1.equals(key2)) {
								if(!mapX.get(key1).equals(mapY.get(key2))) {
									found = true;
								}
							}
						}
					}

					if (found) {
						System.out.println("Les noeuds partagent un ou plusieurs attributs communs");
						System.out.println("\n---------------------------------------------------------------\n");
						continue;
					} else {
						appliquerContraint(sgbd, contraint);
					}
				}

			}
		}

	}

	private static void appliquerContraint(GraphDataBaseHandler sgbd, Contraint contraint) {
		String query;
		List<Record> result;
		System.out.println(contraint.afficherRequete()+"\n");
		query = contraint.generateQuery();
		result = sgbd.execute(sgbd.getDriver(), query);
		GraphDataBaseHandler.afficherElementInfo(result);
		System.out.println("\n--------------------------------------------------------------\n");
	}
}
