package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;
import java.util.List;
import play.data.validation.*;

@Entity
public class Event extends Model {
	
	public Boolean current = false;

	@MaxSize(1000)
	public String notepad;
	
	public String title;
	
	@MaxSize(200)
	public String description;
	
	public String location;
	
	@Enumerated(EnumType.STRING) 
	public Region region = Region.OSLO;
	
	public Date date;
	
	@ManyToMany(cascade=CascadeType.ALL)
	public List<Participant> participants;

	public enum Region {
		OSLO("OSLO"), BERGEN("BERGEN"), SORLANDET("SØRLANDET"), TRONDHEIM("TRONDHEIM"), STAVANGER("STAVANGER");
	
		String value;
		private Region(String value) {
		        this.value = value;
		}
		
		public String toString(){
			return value;
		}
	}
	
	public String toString(){
		return title;
	}
	
}

