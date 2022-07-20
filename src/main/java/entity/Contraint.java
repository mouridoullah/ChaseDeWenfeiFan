package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Contraint {
	private String context;
	private List<String> body;
	private String head;

	public Contraint() {
		// TODO Auto-generated constructor stub
	}

	public Contraint(String context, String body, String head) {
		super();
		if(body != null) {
			this.body = new ArrayList<>(Arrays.asList(body.split(",")));
		}else {
			this.body = new ArrayList<String>();
			this.body.clear();
		}
		this.context = context;
		this.head = head;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getBody() {
		String delimiter = " AND";
		String resultBody;
		if (this.body.size() >= 2) {
			resultBody = String.join(delimiter, body);
			return resultBody;
		} else {
			resultBody = this.body.get(0);
			return resultBody;
		}
	}

	public void setBody(List<String> body) {
		this.body = body;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String afficherContraint() {
		return this.getContext().toString() + "(" + this.getBody().toString() + " -> " + this.getHead().toString()
				+ ")";
	}
	
	public String afficherRequete() {
		return this.generateQuery().toString();
	}

	public String generateQuery() {
		Pattern literalID = Pattern.compile("(.\\.id=.\\.id|.\\.id = .\\.id)");
		Matcher matcherId = literalID.matcher(this.getHead());
		
		if(!this.body.isEmpty()) {
			if (matcherId.find()) {
				return "MATCH " + this.getContext() + 
						"\nWHERE " + this.getBody() + 
						"\nWITH head(collect([x, y])) as nodes" + 
						"\nCALL apoc.refactor.mergeNodes(nodes,{properties:\"combine\", mergeRels:true})" + 
						"\nYIELD node" + 
						"\nRETURN node";
			} else {
				return "MATCH " + this.getContext() + 
						"\nWHERE " + this.getBody() + 
						"\nSET " + this.getHead() + 
						"\nRETURN "	+ this.getHead().substring(0, 1); // * LIMIT 2;
			}
		}else {
			if (matcherId.find()) {
				return "MATCH " + this.getContext() + 
						"\nWITH head(collect([x, y])) as nodes" + 
						"\nCALL apoc.refactor.mergeNodes(nodes,{properties:\"combine\", mergeRels:true})" + 
						"\nYIELD node" + 
						"\nRETURN node";
			} else {
				return "MATCH " + this.getContext() + 
						"\nSET " + this.getHead() + 
						"\nRETURN " + this.getHead().substring(0, 1); // * LIMIT 2;
			}
		}

	}
}
