# Struts2 - Hibernate - MySQL - MVC - CRUD

### Setup

![Database](https://raw.githubusercontent.com/atabegruslan/Struts2-Hibernate-MVC/master/Illustrations/01.PNG)

![Server](https://raw.githubusercontent.com/atabegruslan/Struts2-Hibernate-MVC/master/Illustrations/02.PNG)

To include the needed .jars in Eclipse: Right click the project, Properties, Add an user library, add in the jar files (downloaded from internet for Struts2 and Hibernate). 

Then right click the project, Properties, Add external archives, add in the downloaded JDBC MySQL driver jar (also downloaded from internet).

![JARs](https://raw.githubusercontent.com/atabegruslan/Struts2-Hibernate-MVC/master/Illustrations/03.PNG)

### Results

![Results](https://raw.githubusercontent.com/atabegruslan/Struts2-Hibernate-MVC/master/Illustrations/04.PNG)

### Code

#### hibernate.cfg.xml

```xml
<hibernate-configuration>
    <session-factory>
        <property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>
        <property name="hibernate.connection.driver_class">com.mysql.jdbc.Driver</property>
        <property name="hibernate.connection.username">root</property>
        <property name="hibernate.connection.password"></property>
        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/tripadvisor</property>
        <property name="hibernate.hbm2ddl.auto">update</property>
        <mapping class="com.ruslan.RatingsEntry"/>
    </session-factory>
</hibernate-configuration>
```

#### web.xml

```xml
<display-name>Struts2</display-name>

<filter>
	<filter-name>struts2</filter-name>
	<filter-class>org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter</filter-class>     
</filter>

<filter-mapping>
	<filter-name>struts2</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>
```

#### struts.xml

```xml
<struts>
	<constant name="struts.action.extension" value="" />

	<package name="default" namespace="/tripadvisor" extends="struts-default">
	
		<action name="read" method="read" class="com.ruslan.action.RatingsAction">
			<result name="success">/ratings.jsp</result>
		</action>
		
		<action name="readOne" method="readOne" class="com.ruslan.action.RatingsAction">
			<result name="success">/ratings.jsp</result>
		</action>
		
		<action name="create" method="create" class="com.ruslan.action.RatingsAction">
			<result name="success" type="redirect">read</result>
		</action>
		
		<action name="update" method="update" class="com.ruslan.action.RatingsAction">
			<result name="success" type="redirect">read</result>
		</action>
		
		<action name="delete" method="delete" class="com.ruslan.action.RatingsAction">
			<result name="success" type="redirect">read</result>
		</action>  
		
	</package>	
</struts>
```

#### RatingsAction.java

```java
package com.ruslan.action;

public class RatingsAction extends ActionSupport implements ModelDriven{

	private RatingsEntry ratingsEntry = new RatingsEntry();
	private List<RatingsEntry> ratingsEntries = new ArrayList<RatingsEntry>();
	
	private Database database = new Database();
	
	private String newRating;
	private String destForNewRating;
	
	@Override
	public RatingsEntry getModel() {
		return ratingsEntry;
	}

	public List<RatingsEntry> getRatingsEntries() {
		return ratingsEntries;
	}
	public String read(){
		ratingsEntries = database.read();
		return Action.SUCCESS;
	}
	public String readOne(){
		HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get( ServletActionContext.HTTP_REQUEST );		
		ratingsEntries = database.readOne( request.getParameter("oneDestination") );
		return Action.SUCCESS;		
	}
	public String create(){
		database.create(ratingsEntry);
		return Action.SUCCESS;
	}
	public String update(){
		database.update(newRating, destForNewRating);
		return Action.SUCCESS;
	}
	public String delete(){
		HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get( ServletActionContext.HTTP_REQUEST );
		database.delete( request.getParameter("destination") );
		return Action.SUCCESS;
	}

	public void setNewRating(String newRating) {
		this.newRating = newRating;
	}
	public String getNewRating() {
		return newRating;
	}

	public void setDestForNewRating(String destForNewRating) {
		this.destForNewRating = destForNewRating;
	}
	public String getDestForNewRating() {
		return destForNewRating;
	}
}
```

#### RatingsEntry.java

```java
package com.ruslan;

@Entity
@Table(name="wholeentry")
public class RatingsEntry {

	@Id
	@Column(name="destination")
	private String destination;
	private String country;
	private String rating;
	
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDestination() {
		return destination;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountry() {
		return country;
	}
	
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getRating() {
		return rating;
	}
	
}
```

#### Database.java

```java
package com.ruslan.service;

public class Database {
	
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;

	public List<RatingsEntry> read(){
		List<RatingsEntry> entries = null;
		
		SessionFactory sf = createSessionFactory();
		Session session = sf.openSession();
		
		session.beginTransaction();
		
		try{
			entries = (List<RatingsEntry>) session.createQuery("from RatingsEntry").list();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		session.getTransaction().commit();
		session.close();
		
		return entries;
	}
	
	public List<RatingsEntry> readOne(String dest){
		List<RatingsEntry> entries = null;
		
		SessionFactory sf = createSessionFactory();
		Session session = sf.openSession();
		
		session.beginTransaction();
		
		try{
			Query query = session.createQuery("from RatingsEntry where destination = :dest ");
			query.setParameter("dest", dest);
			entries = (List<RatingsEntry>) query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		session.getTransaction().commit();
		session.close();
		
		return entries;		
	}
	
	public void create(RatingsEntry ratingsEntry){
		SessionFactory sf = createSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();		
		session.save(ratingsEntry);
		session.getTransaction().commit();
		session.close();		
	}
	
	public void update(String newRating, String destForNewRating){
		SessionFactory sf = createSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();		
		
		Query query = session.createQuery("update RatingsEntry set rating = :rating where destination = :dest ");
		query.setParameter("rating", newRating);
		query.setParameter("dest", destForNewRating);
		query.executeUpdate();
		session.getTransaction().commit();
		session.close();	
	}
	
	public void delete(String dest){
		SessionFactory sf = createSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();		
		
		try{
			Query query = session.createQuery("from RatingsEntry where destination = :dest ");
			query.setParameter("dest", dest);
			List<RatingsEntry> entries = (List<RatingsEntry>) query.list();
			session.delete(entries.get(0));
		}catch(Exception e){
			e.printStackTrace();
		}
				
		session.getTransaction().commit();
		session.close();		
	}

	public static SessionFactory createSessionFactory() {
	    Configuration configuration = new Configuration();
	    configuration.configure();
	    serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
	    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	    return sessionFactory;
	}
}
```

#### ratings.jsp

```jsp
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Trip Advisor</title>
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0-alpha/css/bootstrap.min.css">
		<style>
		    .row{
		        position:relative;
		        margin:30px;
		    }		
		</style>
	</head>
	<body>
		<div class="container">
		    <div class="row">
		        <h1>Trip Advisor</h1>
		    </div>
		    <div class="row">
		        <h3>
					<s:url id="readURL" action="read"></s:url>
			        <s:a href="%{readURL}">See All</s:a>		        
		        </h3>
    		</div>
		    <div class="row">
				<table class="table">
					<thead>
						<tr>
							<th>Destination</th>
							<th>Country</th>
							<th>Rating</th>
							<th>Delete</th>
						</tr>
					</thead>
					<tbody>
						<s:iterator value="ratingsEntries">
							<tr>
								<td>
									<s:url id="readOneURL" action="readOne">
										<s:param name="oneDestination" value="%{destination}"></s:param>
									</s:url>
					                <s:a href="%{readOneURL}"><s:property value="destination" /></s:a>
								</td>
								<td><s:property value="country" /></td>
								<td>
									<s:form action="update" theme="simple">
										<s:hidden name="destForNewRating" value="%{destination}" />
										<s:textfield name = "newRating" label = "Update Rating Here" value = "%{rating}"/>
										<s:submit />
									</s:form>						
								</td>
								<td>
					                <s:url id="deleteURL" action="delete">
										<s:param name="destination" value="%{destination}"></s:param>
									</s:url>
					                <s:a href="%{deleteURL}">Delete</s:a>
				                </td>
							</tr>
						</s:iterator>
					</tbody>
				</table>	
		    </div> 
		    <div class="row">
				<s:form action="create" theme="simple" >
					 <s:textfield name="destination" label="Destination" />
					 <s:textfield name="country" label="Country" />
					 <s:textfield name="rating" label="Rating" />
					 <s:submit />
				</s:form>
		    </div>   		
    	</div>				

		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0-alpha/js/bootstrap.min.js"></script>
	</body>
</html>
```
