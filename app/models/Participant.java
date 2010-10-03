package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;
import play.data.validation.*;

@Entity
public class Participant extends Model {

	public Participant(String email, String name, Integer howMany){
		this.email = email;
		this.name = name;
		this.howMany = howMany;
	}


	@Email
	public String email;	
	
	public String name;
	
	public Integer howMany;
	
	public String toString(){
		return name + "(" + email + ")";
	}
	
}

