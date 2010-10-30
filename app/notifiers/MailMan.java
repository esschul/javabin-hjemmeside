package notifiers;
 
import play.*;
import play.mvc.*;
import java.util.*;
import models.*;
 
public class MailMan extends Mailer {
 
   public static void signUp(Participant participant, Event event, String crypto) {
      setSubject("javaBin: %s er registrert som deltager!", participant.name);
      addRecipient(participant.email);
      setFrom("JavaBin <motegruppa@java.no>");
      send(participant, event, crypto);
   }

   public static void takeMeOff(Participant participant, Event event) {
      setSubject("javaBin: %s er avmeldt fra møte", participant.name);
      addRecipient(participant.email);
      setFrom("JavaBin <motegruppa@java.no>");
      send(participant, event);
   }
 
   public static void nextMeeting(Event event, String emailFrom, String emailTo) {
      setSubject("Neste javaBin-møte : %s", event.title);
      setFrom(emailFrom);
      addRecipient(emailTo);
      send(event);
   }

 
}