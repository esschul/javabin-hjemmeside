package controllers;

import play.mvc.*;
import play.libs.*;
import play.cache.*;
import play.data.validation.*;
import java.util.*;
import models.*;
import notifiers.*;

public class Application extends Controller {

    public static void index() {
		List<Event> events = Event.findBy("current is true");
		String randomId = Codec.UUID();
        render(events, randomId);
    }

	public static void signUpForEvent(Long eventId, String randomId, String code, @Required @Email String email, @Required String name, @Required Integer howMany) {
		validation.equals(code, Cache.get(randomId)).message("Feil kode!");
		Participant participant = new Participant(email, name, howMany);
		if(!validation.hasErrors()) {
			Event event = Event.findById(eventId);
			event.participants.add(participant);
			event.save();
			MailMan.signUp(participant, event);
		} else {
			renderJSON(validation.errors()); // gi tilbakemelding.
		}
		Cache.delete(randomId);		
		renderJSON("status:ok"); // be bruker sjekke postkassa si.
	}
	
	public static void regretSigningUp(Long eventId, String code, String email) {
		Event event = Event.findById(eventId);
		Participant p = null;
		for(Participant participant : event.participants) {
			if(participant.email.equals(email)){
				event.participants.remove(participant);
				p = participant;
			}
		}
		event.save();
		MailMan.takeMeOff(p, event);
	}
	
	public static void listOldEvents() {
		List<Event> osloEvents = Event.findBy("date >= ? and current is false and region = ? orderby date asc", new Date(), Event.Region.OSLO);
		List<Event> trondheimEvents = Event.findBy("date >= ? and current is false and region = ? orderby date asc", new Date(), Event.Region.TRONDHEIM);
		List<Event> sorlandetEvents = Event.findBy("date >= ? and current is false and region = ? orderby date asc", new Date(), Event.Region.SORLANDET);
		List<Event> bergenEvents = Event.findBy("date >= ? and current is false and region = ? orderby date asc", new Date(), Event.Region.BERGEN);
		List<Event> stavangerEvents = Event.findBy("date >= ? and current is false and region = ? orderby date asc", new Date(), Event.Region.STAVANGER);
		render(osloEvents, trondheimEvents, sorlandetEvents, bergenEvents, stavangerEvents);
	}
	
	public static void captcha(String id) {
	    Images.Captcha captcha = Images.captcha();
	    String code = captcha.getText("#FFFFFF");
	    Cache.set(id, code, "10mn");
	    renderBinary(captcha);
	}

}