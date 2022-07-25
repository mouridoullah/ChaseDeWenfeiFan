/**
 * 
 */
package WenfeiChase;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.neo4j.driver.Record;

import entity.Contraint;
import entity.Graph;
import sgbd.GraphDataBaseHandler;;

/**
 * @author bdvm
 *
 */

public class Chase {
	/**
	 * 
	 */
	public static void compute(Graph graph, List<Contraint> sigma) {
		Pattern literalConstant = Pattern
				.compile("([a-zA-Z]\\.[a-zA-Z]=([1-9]?[0-9])|100|[a-zA-Z]\\.[a-zA-Z] = ([1-9]?[0-9])|100)");
		Pattern literalVariable = Pattern
				.compile("([a-zA-Z]\\.[a-zA-Z]=[a-zA-Z]\\.[a-zA-Z]|[a-zA-Z]\\.[a-zA-Z] = [a-zA-Z]\\.[a-zA-Z])");
		Pattern literalID = Pattern.compile("(.\\.id=.\\.id|.\\.id = .\\.id)");

		GraphDataBaseHandler sgbd = graph.getDatabaseHanlder();

		int resultatTest;
		boolean ok;
		String status;

		for (Contraint contraint : sigma) {
			Matcher matcherConstant = literalConstant.matcher(contraint.getHead());
			Matcher matcherVariable = literalVariable.matcher(contraint.getHead());
			Matcher matcherId = literalID.matcher(contraint.getHead());

			if (matcherConstant.find()) {
				func(graph, contraint);
			} else if (matcherVariable.find()) {
				func(graph, contraint);
			} else if (matcherId.find()) {
				do {
					ok = false;
					resultatTest = CheckConflit.conflitLabel(contraint, graph);
					if (resultatTest == 2) {
						System.err.println("Conflit de label avec la contrainte : " + contraint.afficherContraint());
						System.out.println(
								"\n------------------------------------------------------------------------------------------------------------------------------\n");
						System.exit(0);
					}
					if (resultatTest == 1) {

						Map<String, Object> mapX = CheckConflit.getNode(graph, contraint, "x");
						Map<String, Object> mapY = CheckConflit.getNode(graph, contraint, "y");

						if (mapX.isEmpty() || mapY.isEmpty() || mapX == null || mapY == null) {
							System.err.println("Noeud introuvable :\n" + contraint.afficherContraint());
							System.out.println(
									"\n------------------------------------------------------------------------------------------------------------------------------\n");
							System.exit(0);
						} else {

							boolean found = false;
							for (String key1 : mapX.keySet()) {
								for (String key2 : mapY.keySet()) {
									if (key1.equals(key2)) {
										if (!mapX.get(key1).equals(mapY.get(key2))) {
											found = true;
										}
									}
								}
							}
							if (found) {
								System.err.println(
										"Les noeuds partagent un ou plusieurs attributs communs avec des valeurs différentes pour la contrainte suivante : \n"
												+ contraint.afficherContraint());
								String query;
								List<Record> result;

								query = "MATCH " + contraint.getContext() + " WHERE " + contraint.getBody()
										+ " RETURN *";
								result = sgbd.execute(sgbd.getDriver(), query);
								GraphDataBaseHandler.afficherElementInfo(result);
								System.out.println(
										"\n------------------------------------------------------------------------------------------------------------------------------\n");
								System.exit(0);
							} else {
								status = appliquerContraint(sgbd, contraint);
								if (status == "ok")
									ok = true;
							}
						}
					} else if (resultatTest == 3) {
						noMatch(contraint);
						System.exit(0);
					}
				} while (ok);
			}

		}
	}

	private static void func(Graph graph, Contraint contraint) {
		GraphDataBaseHandler sgbd = graph.getDatabaseHanlder();
		int resultatTest;
		boolean ok;
		do {
			ok = false;
			resultatTest = CheckConflit.conflitAttribut(contraint, graph);
			if (resultatTest == 1 && !CheckConflit.testSatisfaction2(contraint, graph)) {
				satisfaction(sgbd, contraint);
				continue;
			} else if (resultatTest == 2) {
				conflitDeValDattribut(sgbd, contraint);
				System.exit(0);
			} else if (resultatTest == 4) {
				noMatch(contraint);
				System.exit(0);
			} else {
				appliquerContraint(sgbd, contraint);
				ok = true;
			}

			if (CheckConflit.testSatisfaction2(contraint, graph)) {
				appliquerContraint(sgbd, contraint);
				ok = true;
			}

		} while (ok);
	}

	private static void satisfaction(GraphDataBaseHandler sgbd, Contraint contraint) {
		System.out.println("Le graphe satisfait la contrainte : " + contraint.afficherContraint() + "\n");

		String query;
		List<Record> result;
//		System.out.println(contraint.afficherContraint()+"\n");
		query = "MATCH " + contraint.getContext() + " WHERE " + contraint.getHead() + " RETURN * LIMIT 2"; // Pour ne
																											// pas
																											// alourdir
																											// l'affichage
		result = sgbd.execute(sgbd.getDriver(), query);
		GraphDataBaseHandler.afficherElementInfo(result);

		System.out.println(
				"\n------------------------------------------------------------------------------------------------------------------------------\n");
	}

	private static void conflitDeValDattribut(GraphDataBaseHandler sgbd, Contraint contraint) {
		String query;
		List<Record> result;
		System.err.println("Conflit de valeur d'attribut car " + contraint.getHead().substring(0, 3)
				+ " est non nul avec la contrainte suivante : " + contraint.afficherContraint());
		query = "MATCH " + contraint.getContext() + " WHERE " + contraint.getBody() + " RETURN "
				+ contraint.getHead().substring(0, 1); // + "{.*}";
		result = sgbd.execute(sgbd.getDriver(), query);
		GraphDataBaseHandler.afficherElementInfo(result);
		System.out.println(
				"\n------------------------------------------------------------------------------------------------------------------------------\n");
	}

	private static void noMatch(Contraint contraint) {
		System.err.println("Le contexte ne match pas sur le graphe :\n" + contraint.afficherContraint());
		System.out.println(
				"\n------------------------------------------------------------------------------------------------------------------------------\n");
	}

	private static String appliquerContraint(GraphDataBaseHandler sgbd, Contraint contraint) {
		String query;
		List<Record> result;
		query = contraint.generateQuery();
		result = sgbd.execute(sgbd.getDriver(), query);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!result.isEmpty()) {
			System.out.println("L'application de la contrainte suivante : " + contraint.afficherContraint() + "\n");
			GraphDataBaseHandler.afficherElementInfo(result);
			System.out.println(
					"\n------------------------------------------------------------------------------------------------------------------------------\n");
			return "ok";

		} else {
			return "error";
		}

	}
}