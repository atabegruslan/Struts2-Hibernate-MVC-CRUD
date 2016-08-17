<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

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