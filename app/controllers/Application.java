package controllers;

import groovy.lang.Closure;
import groovy.util.XmlSlurper;
import groovy.util.slurpersupport.GPathResult;
import org.apache.abdera.*;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.apache.abdera.parser.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.xml.sax.SAXException;
import play.mvc.*;
import play.libs.*;
import play.cache.*;
import play.data.validation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import models.*;
import notifiers.*;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

public class Application extends Controller {

    public static void index() {
        List<Announcement> announcements;

        announcements = getAnnouncements();

        List<Event> events = Event.find("current is true ").fetch();
		String randomId = Codec.UUID();
        render(announcements, events, randomId);
    }



    private static List<Announcement> getAnnouncements() {
        List<Announcement> announcements;
        Abdera abdera;
        Parser parser;
        URL url;
        Document<Feed> doc;
        Feed feed;

        announcements = new LinkedList<Announcement>();
        abdera = new Abdera();
        parser = abdera.getParser();

        try {
            url = new URL("http://wiki.java.no/createrssfeed.action?types=blogpost&sort=created&showContent=true&showDiff=true&spaces=javabin&labelString=forside&rssType=atom&maxResults=10&timeSpan=5&publicFeed=true&title=javaBin+RSS+Feed");

            doc = parser.parse(url.openStream());
            feed = doc.getRoot();

            for (Entry entry : feed.getEntries()) {
              announcements.add( new Announcement( entry.getTitle(), entry.getSummary(), entry.getLink("alternate").getHref().toString() ) );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return announcements;
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
	public static void lectureholders() { render(); }
	public static void contact() { render(); }

}