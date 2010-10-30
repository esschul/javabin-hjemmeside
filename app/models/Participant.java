package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;
import play.data.validation.*;

@Entity
public class Participant extends Model {

	public Participant(String email, String name){
		this.email = email;
		this.name = name;
	}

	@Email
	public String email;	
	
	public String name;
	
	@ManyToMany(cascade = CascadeType.ALL)
	public List<Event> events;
	
	public String toString(){
		return name + "(" + email + ")";
	}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Participant that = (Participant) o;

        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (events != null ? !events.equals(that.events) : that.events != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (events != null ? events.hashCode() : 0);
        return result;
    }
}

