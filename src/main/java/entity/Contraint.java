package entity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Contraint {
	private String context;
	private String body;
	private String head;
	private boolean isLiteralId = false;
	
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
		Pattern literalID = Pattern.compile("(.\\.id=.\\.id|.\\.id = .\\.id)");
		Matcher matcherId = literalID.matcher(this.getHead());
		
		if(matcherId.find()) {
			isLiteralId = true;
		}else {
			isLiteralId = false;
		}
		
		if(isLiteralId == false) {
			return "MATCH "+ this.getContext() +
				   "\nWHERE "+ this.getBody() +
				   "\nSET "+ this.getHead() +
				   "\nRETURN *";	
		} else {
			return "MATCH "+this.getContext() +
					"\nWITH head(collect([y,x])) as nodes" + 
					"\nCALL apoc.refactor.mergeNodes(nodes,{properties:\"combine\", mergeRels:true})" + 
					"\nYIELD node" + 
					"\nRETURN node";
		}
	}
}
