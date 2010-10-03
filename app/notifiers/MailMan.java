package notifiers;
 
import play.*;
import play.mvc.*;
import java.util.*;
import models.*;
 
public class MailMan extends Mailer {
 
   public static void signUp(Participant participant, Event event) {
      setSubject("%s er registrert som deltager!", participant.name);
      addRecipient(participant.email);
      setFrom("JavaBin <motegruppa@java.no>");
      send(participant, event);
   }

   public static void takeMeOff(Participant participant, Event event) {
      setSubject("%s er avmeldt", participant.name);
      addRecipient(participant.email);
      setFrom("JavaBin <motegruppa@java.no>");
      send(participant, event);
   }
 
   public static void nextMeeting(Event event) {
      setSubject("Neste javaBin-møte : %s", event.title);
      addRecipient(event.region); // legg på mailadresse til region
      setFrom("JavaBin <motegruppa@java.no>");
      send(event);
   }

 
}