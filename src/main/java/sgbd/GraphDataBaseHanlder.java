package sgbd;

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

import java.util.List;


public class GraphDataBaseHanlder implements AutoCloseable {
	private final Driver driver;
	private final String uri = "bolt://localhost:7687";
	private final String user = "neo4j";
	private final String password = "test";
	private final Config config;

	public GraphDataBaseHanlder() {
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

	public void init() {
		String create = "CREATE (u1:User{id:1,a:1,b:2}),\n" + 
						"		(u2:User{id:1,c:3,d:4}),\n" + 
						"		(u3:User{id:3,c:5,d:4}),\n" + 
						"		(u4:User{id:2,c:3,d:4}),\n" + 
						"		(u5:User{id:2,a:2,b:3})\n" + 
						"	   \n" + 
						"CREATE (u3)-[:rel]->(u1)\n" + 
						"CREATE (u3)-[:rel]->(u2)\n" + 
						"CREATE (u1)-[:rel]->(u5)\n" + 
						"CREATE (u4)-[:rel]->(u2)\n" + 
						"CREATE (u5)-[:rel]->(u4)\n" + 
						"CREATE (u2)-[:rel]->(u5)";
		this.execute(this.getDriver(), create);
	}

	public static void afficherElementInfo(List<Record> result) {
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
						System.out.println("NodeKey : " + id + "\nNodelabel : " + node.labels() + "\nWenfeiID = "
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

	public void delete() {
		String del = "MATCH (n)   \n" + "OPTIONAL MATCH (n)-[r]-()\n" + "DELETE n,r";
		this.execute(this.getDriver(), del);
	}
	
//	public static void main(String[] args) throws Exception {
//
//		try (GraphDataBaseHanlder database = new GraphDataBaseHanlder()) {
//
//
//			// creation de la base
//			database.init();
//
//			String query = "MATCH (u:User) WHERE u.a = 1 AND u.b = 2 RETURN u";
//			List<Record> result = database.execute(database.getDriver(), query);
//
//			afficherElementInfo(result);
//
//			//database.delete();
//		}
//
//	}

}