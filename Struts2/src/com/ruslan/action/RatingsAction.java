package com.ruslan.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.ruslan.RatingsEntry;
import com.ruslan.service.Database;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

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
		//ratingsEntries.add( (RatingsEntry) this.getModel() );
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
