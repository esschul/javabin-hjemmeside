package models;

import play.*;
import java.util.*;

public class Announcement {
	
public String title;
public String description;
public String url;

    public Announcement(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }
	
	
}

