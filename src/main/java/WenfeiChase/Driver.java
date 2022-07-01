package WenfeiChase;

import static org.neo4j.driver.Values.parameters;

import org.neo4j.driver.*;

public class Driver {
    public static void main(String[] args) {
    	
        org.neo4j.driver.Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "test"));
        Session session = driver.session();

        runQuery(session);

        // query
        Value params = parameters("title", "Forrest Gump");
        Result result = session.run(
                "MATCH (m:Movie {title:$title})<-[:ACTS_IN]-(a:Actor) " +
                "RETURN a.firstName, a.lastName",
                params);
        System.out.println(result.peek().get("a.firstName"));

        // cleanup
        session.run("MATCH (m:Movie) DETACH DELETE m");
        session.run("MATCH (a:ACtor) DELETE a");

        session.close();
        driver.close();
    }

	private static void runQuery(Session session) {
		// create graph
        session.run(
            "CREATE (a:Actor {firstName:'Tom',lastName:'Hanks'})" +
                    "-[:ACTS_IN]->(m:Movie {title:'Forrest Gump'}) " +
            "RETURN a, m");
	}
}
