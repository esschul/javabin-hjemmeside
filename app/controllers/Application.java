package controllers;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import play.mvc.*;
import play.libs.*;
import play.cache.*;
import play.data.validation.*;
import java.util.*;
import models.*;
import notifiers.*;

public class Application extends Controller {

    public static void index() {
		List<Event> events = Event.findBy("current is true ");
		String randomId = Codec.UUID();
        render(events, randomId);
    }

	public static void signUpForEvent(Long eventId, String randomId, String code, @Required @Email String email, @Required String name, @Required Integer howMany) {
		validation.equals(code, Cache.get(randomId)).message("Feil kode!");
		validation.match(howMany, "[1-9]").message("Feltet må være et siffer mellom 1 og 9");
		if(!validation.hasErrors()) {
			Participant participant = null;
			List<Participant> participantList = Participant.find("email = ?", email).fetch();
			if(participantList == null || participantList.isEmpty()){
				participant = new Participant(email, name);							
			} else {
				participant = participantList.get(0);
			}
			
			Event event = Event.findById(eventId);
            String crypto = Crypto.encryptAES(participant.email + "_" + event.title);
			if(event.participants.contains(participant)){
				event.participants.add(participant);
				event.participantCount += howMany;
				event.save();


                MailMan.signUp(participant, event, crypto);
			} else {
				// add to json that participant already was signed up. we have sent you another email with the details.
				MailMan.signUp(participant, event, crypto);
			}
		} else {			
			params.flash();
			validation.keep();
			renderJSON(validation.errors()); // gi tilbakemelding.
		}
		Cache.delete(randomId);		
		renderJSON("status:ok"); // be bruker sjekke postkassa si.
	}
	
	public static void regretSigningUp(String id) {
        String decrypted = Crypto.decryptAES(id);
        String[] strings = StringUtils.split(decrypted, '_');

        Event event = Event.find("title = ?", strings[1]).first();
	
		Participant p = null;
		for(Participant participant : event.participants) {
			if(participant.email.equalsIgnoreCase(strings[0])){
				event.participants.remove(participant);
				p = participant;
			}
		}
		event.save();
		render(p, event);
	}
	
	public static void listOldEvents() {

        List<Event> osloEvents = Event.find("current is false and region = ?", Event.Region.OSLO).fetch();
        List<Event> trondheimEvents = Event.find("current is false and region = ?", Event.Region.TRONDHEIM).fetch();
        List<Event> sorlandetEvents = Event.find("current is false and region = ?", Event.Region.SORLANDET).fetch();
        List<Event> bergenEvents = Event.find("current is false and region = ?", Event.Region.BERGEN).fetch();
        List<Event> stavangerEvents = Event.find("current is false and region = ?", Event.Region.STAVANGER).fetch();
		render(osloEvents, trondheimEvents, sorlandetEvents, bergenEvents, stavangerEvents);
	}
	
	public static void captcha(String id) {
	    Images.Captcha captcha = Images.captcha();
	    String code = captcha.getText("#FFFFFF");
	    Cache.set(id, code, "10mn");
	    renderBinary(captcha);
	}
	
	
	// todo : finn ut hvordan man router til statiske sider.
	public static void about() { render(); }
	public static void heroes() { render(); }
	public static void contact() { render(); }
	public static void membership() { render(); }

}