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


public class GraphDataBaseHandler implements AutoCloseable {
	private final Driver driver;
	private final String uri = "bolt://localhost:7687";
	private final String user = "neo4j";
	private final String password = "test";
	private final Config config;

	public GraphDataBaseHandler() {
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
		String create = "CREATE (person1:Person {name:'toto',s:1,a:1,b:2}),\n" + 
				"(person2:Person {name:'titi',f:2,c:3,d:4}),\n" + 
				"(company1:Company {name:'Company1',p:5,a:1,b:2}),\n" + 
				"(car:Car {brand:'Ferrari'}),\n" + 
				"(animal:Cat {name:'Derby',k:4}),\n" + 
				"(city1:City {name:'London',g:3,c:3,d:4}),\n" + 
				"(person1)-[:WORKS_FOR {since:2015}]->(company1),\n" + 
				"(person2)-[:WORKS_FOR {since:2018}]->(company1),\n" + 
				"(company1)-[:HAS_HQ {since:2004}]->(city1),\n" + 
				"(person1)-[:DRIVE {since:2017}]->(car),\n" + 
				"(person2)-[:HAS {since:2013}]->(animal),\n" + 
				"(company2:Company {name:'Company2', o:2,c:3,d:4}),\n" +  
				"(city2:City {name:'Liverpool',o:2,c:3,d:4}),\n" + 
				"(person2)-[:WORKS_FOR{since:2018}]->(company2),\n" +
				"(person1)-[:WORKS_FOR {since:2020}]->(person2),\n" +
				"(company2)-[:HAS_HQ{since:2007}]->(city2)\n";
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
						//String idWenfei = value.get("id").toString();
						System.out.println("NodeKey : " + id + "\nNodelabel : " + node.labels());
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
	
	public static void main(String[] args) throws Exception {

		try (GraphDataBaseHandler database = new GraphDataBaseHandler()) {
			// creation de la base
			database.init();

			String query = "MATCH (u:User) WHERE u.a = 1 AND u.b = 2 RETURN u";
			List<Record> result = database.execute(database.getDriver(), query);

			afficherElementInfo(result);

			//database.delete();
		}

	}

}