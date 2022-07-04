package entity;



public class Contraint {
	private String context;
	private String body;
	private String head;
	
	public Contraint() {
		// TODO Auto-generated constructor stub
	}

	public Contraint(String context, String body, String head) {
		super();
		this.context = context;
		this.body = body;
		this.head = head;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}
	
	public String generateQuery() {
		return "MATCH "+ this.getContext() +
			   "\nWHERE "+ this.getBody() +
			   "\nSET "+ this.getHead() +
			   "\nReturn *";	
	}
}
