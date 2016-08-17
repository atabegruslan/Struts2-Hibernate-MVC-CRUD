package com.ruslan;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

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
