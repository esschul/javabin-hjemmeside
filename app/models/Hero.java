package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Hero extends Model {
	
	public String name;
	public String description;
	public Boolean active = Boolean.FALSE;
	public String picture;
	
}

