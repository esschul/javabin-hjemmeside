package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class LectureHolder extends Model {
	
	public String name;
	public String picture;
	
}

