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
		} else if(this.body.size() == 1){
			resultBody = this.body.get(0);
			return resultBody;
		}else {
			return null;
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
		if(!this.body.isEmpty()) {
			String resultBody = String.join(", ", body);
			return this.getContext().toString() + "(" + resultBody + " --> " + this.getHead().toString()
				+ ")";
		} else {
			return this.getContext().toString() + "(Ø --> " + this.getHead().toString()
					+ ")";
		}
	}
	
	public String afficherRequete() {
		return this.generateQuery().toString();
	}

	public String generateQuery() {
		Pattern literalID = Pattern.compile("(.\\.id=.\\.id|.\\.id = .\\.id)");
		Matcher matcherId = literalID.matcher(this.getHead());
		
		if(!this.body.isEmpty()) {
			if (matcherId.find()) {
				 								
				return	
//						"MATCH " + this.getContext() + 
//						"\nMATCH (y)<-[r1]-()" +
//						"\nWHERE NOT (x)-[]-(x) AND " + this.getBody() + 
//						"\nSET x += y" +
//						"\nWITH *" +
//						"\nCALL apoc.refactor.to(r1, x) YIELD input, output " +
//						"\nMATCH (y)-[r2]->() " +
//						"\nCALL apoc.refactor.from(r2, x) YIELD input AS i, output AS o " +
//						"\nDELETE y" +
//						"\nRETURN x LIMIT 1;";
						
//												
						"MATCH " + this.getContext() + 
						"\nWHERE id(x)<>id(y) AND " + this.getBody() + 
						"\nWITH head(collect(DISTINCT[x, y])) as nodes" + 
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
				return  
//						"MATCH " + this.getContext() + 
//						"\nMATCH (y)<-[r1]-()" +
//						"\nWHERE NOT (x)-[]-(x) AND " + 
//						"\nSET x += y" +
//						"\nWITH *" +
//						"\nCALL apoc.refactor.to(r1, x) YIELD input, output " +
//						"\nMATCH (y)-[r2]->() " +
//						"\nCALL apoc.refactor.from(r2, x) YIELD input AS i, output AS o " +
//						"\nDELETE y" +
//						"\nRETURN x LIMIT 1;";
						
						
						
						"MATCH " + this.getContext() + 
						"\nWHERE id(x)<>id(y)" +  
						"\nWITH head(collect(DISTINCT[x, y])) as nodes" + 
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
