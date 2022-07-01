package WenfeiChase;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Logging;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.neo4j.driver.util.Pair;

import phi.Contraint;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chase implements AutoCloseable {
	private final Driver driver;
	private final String uri = "bolt://localhost:7687";
	private final String user = "neo4j";
	private final String password = "test";
	private final Config config;

	public Chase() {
		config = Config.builder().withLogging(Logging.slf4j()).build();
		driver = GraphDatabase.driver(this.getUri(), AuthTokens.basic(this.getUser(), this.getPassword()), config);
	}

	public String getUri() {
		return uri;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public void close() throws Exception {
		driver.close();
	}

	public Driver getDriver() {
		return driver;
	}

	public List<Record> execute(Driver driver, String query) {
		List<Record> resultats;
		try (Session session = driver.session()) {
			List<Record> record = session.writeTransaction(tx -> {
				Result result = tx.run(query);
				return result.list();
			});
			resultats = record;
		}
		return resultats;
	}

	private void init() {
		String create = "CREATE  (u1:User{id:1,a:1,b:2}),\n" + "		(u2:User{id:2,c:3,d:4}),\n"
				+ "		(u3:User{id:3,c:3,d:4}),\n" + "		(u4:User{id:2,c:3,d:4}),\n"
				+ "		(u5:User{id:1,a:1,b:2})\n" + "	   \n" + "CREATE (u3)-[:rel]->(u1)\n"
				+ "CREATE (u3)-[:rel]->(u2)\n" + "CREATE (u1)-[:rel]->(u5)\n" + "CREATE (u4)-[:rel]->(u2)\n"
				+ "CREATE (u5)-[:rel]->(u4)\n" + "CREATE (u2)-[:rel]->(u5)";
		this.execute(this.getDriver(), create);
	}

	private static void afficherElementInfo(List<Record> result) {
		for (Record record : result) {

			Value value;
			String key;
			String typName;

			List<Pair<String, Value>> list = record.fields();
			for (Pair<String, Value> pair : list) {

				value = pair.value();
				key = pair.key();
				System.out.println("---- {RecordKey : " + key + "} ----");

				if (value != null) {
					typName = value.type().name(); // NODE, RELATIONSHIP, NULL
					if ("NODE".equals(typName)) {
						Node node = value.asNode();
						Long id = value.asNode().id();
						String idWenfei = value.get("id").toString();
						System.out.println("NodeKey : " + id + ", Nodelabel : " + node.labels() + ", idWenfei = "
								+ idWenfei + "\n");
					} else if ("RELATIONSHIP".equals(typName)) {

						Relationship relationship = value.asRelationship();
						Long id = value.asRelationship().id();
						Long startNodeId = relationship.startNodeId();
						Long endNodeId = relationship.endNodeId();
						System.out.println("RelationshipKey : " + id);
						System.out.println(
								"(" + startNodeId + ") -[" + relationship.type() + "]-> (" + endNodeId + ")\n");
					}
				}

			}
		}
	}

	private void delete() {
		String del = "MATCH (n)   \n" + "OPTIONAL MATCH (n)-[r]-()\n" + "DELETE n,r";
		this.execute(this.getDriver(), del);
	}

	public static void main(String[] args) throws Exception {

		try (Chase database = new Chase()) {

			// creation de la base
			database.init();

			String query = "MATCH (u:User)-[r:rel]-() " + "RETURN u, r";
			List<Record> result = database.execute(database.getDriver(), query);

			afficherElementInfo(result);

			Contraint phi_1 = new Contraint();
			phi_1.setContext("(x:User)-[:rel]->(y:User)");
			phi_1.setBody("x.a = y.a");
			phi_1.setHead("y.b = 1");

			Pattern lateralConstant = Pattern
					.compile("([a-zA-Z]\\.[a-zA-Z]=([1-9]?[0-9])|100|[a-zA-Z]\\.[a-zA-Z] = ([1-9]?[0-9])|100)");
			Pattern lateralVariable = Pattern
					.compile("([a-zA-Z]\\.[a-zA-Z]=[a-zA-Z]\\.[a-zA-Z]|[a-zA-Z]\\.[a-zA-Z] = [a-zA-Z]\\.[a-zA-Z])");
			Pattern lateralID = Pattern.compile("(.\\.id=.\\.id|.\\.id = .\\.id)");

			Matcher m = lateralConstant.matcher(phi_1.getHead());
			Matcher n = lateralVariable.matcher(phi_1.getBody());
			Matcher o = lateralID.matcher(phi_1.getHead());

			System.out.println(m.find());
			System.out.println(n.find());
			System.out.println(o.find());

//			String query_phi_1 = phi_1.generateQuery();
//
//			System.out.println(query_phi_1);

			database.delete();
		}

	}
}