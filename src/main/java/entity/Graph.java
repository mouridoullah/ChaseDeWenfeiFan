package entity;

import sgbd.GraphDataBaseHandler;

public class Graph implements AutoCloseable  {
	private String create;
	private GraphDataBaseHandler databaseHanlder;
	
	public Graph() {
		databaseHanlder = new GraphDataBaseHandler();
		// TODO Auto-generated constructor stub
	}

	public String getCreate() {
		return create;
	}

	public void setCreate(String create) {
		this.create = create;
	}

	public GraphDataBaseHandler getDatabaseHanlder() {
		return databaseHanlder;
	}

	public void setDatabaseHanlder(GraphDataBaseHandler databaseHanlder) {
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
