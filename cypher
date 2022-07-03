MATCH (x:User), (y:User)
WHERE NOT (y)-[]-() 
AND x.id = y.id AND x.a IS NOT NULL OR y.c IS NOT NULL
SET x.c = y.c, x.d = y.d
DELETE y
RETURN x.id


CREATE  (u1:User{id:1,a:1,b:2}),
		(u2:User{id:1,c:3,d:4}),
		(u3:User{id:3,c:5,d:4}),
		(u4:User{id:2,c:3,d:4}),
		(u5:User{id:2,a:2,b:3})
	   
CREATE (u3)-[:rel]->(u1)
CREATE (u3)-[:rel]->(u2)
CREATE (u1)-[:rel]->(u5)
CREATE (u4)-[:rel]->(u2)
CREATE (u5)-[:rel]->(u4)
CREATE (u2)-[:rel]->(u5)
	   
MATCH (x:User),(y:User)
WHERE x.a = y.a 
RETURN x, y
	   
MATCH (n)   
OPTIONAL MATCH (n)-[r]-()
DELETE n,r

MATCH (x:User)
WHERE x.a = 1 AND x.b = 2 
RETURN x