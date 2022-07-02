package entity;

import sgbd.GraphDataBaseHanlder;

public class Graph implements AutoCloseable  {
	private String create;
	private GraphDataBaseHanlder databaseHanlder;
	
	public Graph() {
		databaseHanlder = new GraphDataBaseHanlder();
		// TODO Auto-generated constructor stub
	}

	public String getCreate() {
		return create;
	}

	public void setCreate(String create) {
		this.create = create;
	}

	public GraphDataBaseHanlder getDatabaseHanlder() {
		return databaseHanlder;
	}

	public void setDatabaseHanlder(GraphDataBaseHanlder databaseHanlder) {
		this.databaseHanlder = databaseHanlder;
	}
	
	public void createGraphe() {
		this.getDatabaseHanlder().init();
	}
	
	public void deleteGraphe() {
		this.getDatabaseHanlder().delete();
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
