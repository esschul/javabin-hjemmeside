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
	
	@ManyToMany(cascade=CascadeType.ALL, mappedBy="events")
	public List<Participant> participants;

	public Integer participantCount = 0;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Event event = (Event) o;

        if (current != null ? !current.equals(event.current) : event.current != null) return false;
        if (date != null ? !date.equals(event.date) : event.date != null) return false;
        if (description != null ? !description.equals(event.description) : event.description != null) return false;
        if (location != null ? !location.equals(event.location) : event.location != null) return false;
        if (notepad != null ? !notepad.equals(event.notepad) : event.notepad != null) return false;
        if (participantCount != null ? !participantCount.equals(event.participantCount) : event.participantCount != null)
            return false;
        if (participants != null ? !participants.equals(event.participants) : event.participants != null) return false;
        if (region != event.region) return false;
        if (title != null ? !title.equals(event.title) : event.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (current != null ? current.hashCode() : 0);
        result = 31 * result + (notepad != null ? notepad.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (participants != null ? participants.hashCode() : 0);
        result = 31 * result + (participantCount != null ? participantCount.hashCode() : 0);
        return result;
    }
}

