package MainProgram;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import static MainProgram.Controller.*;


/**
 * Project: Mill
 * Package: MainProgram
 * Created by keno on 15.12.16.
 */
public class Controller_Feedback {

    @FXML
    private Button sendbutton;
    @FXML
    private ImageView background;
    @FXML
    private TextField namefield,adressfield;
    @FXML
    private TextArea messagearea;
    @FXML
    private Label statuslabel,charsleft;

    /**
     * The Timer.
     */
    static Timer timer;

    /**
     * Initialize.
     *
     * @throws MalformedURLException the malformed url exception
     */
//Initialisierung aller benoetigten Komponenten
    @FXML
    @SuppressWarnings("unused")
    public void initialize() throws MalformedURLException {

        background.setImage(new Image(Controller.MENUE.toURI().toURL().toString()));

        //Neuen Timer erstellen, welcher im Hintergrund eingaben ueberprueft
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    //Wenn das Adressfeld und das Nachrichtenfeld ausreihend Text enthalten, wird der Senden-Button freigeschaltet
                    if (adressfield.getText().length() > 10 &&
                            messagearea.getText().length() >= 30){
                        if (sendbutton.isDisable())sendbutton.setDisable(false);
                    }
                    else if (!sendbutton.isDisable())sendbutton.setDisable(true);

                    //Aktualisieren des "Verbleibende Zeichen"-Labels
                    int temp = 300 - messagearea.getText().length();
                    if (temp < 0)temp = 0;
                    charsleft.setText(new DecimalFormat("000").format(temp));
                });

            }
        };
        timer.schedule(task,500,200);   //Timer starten

        //Standardtext in TextArea schieben
        messagearea.setPromptText("Vielen Dank, dass Sie sich dazu entschieden haben, uns Ihr Feedback zu diesem Programm mitzuteilen.\r\n" +
                "Sollte es sich hierbei um eine Supportanfrage handeln, benötigen wir folgende Angaben:\r\n\r\n" +
                "Beschreiben Sie bitte das Problem und mit welchen Schritten dies reproduziert werden kann\r\n" +
                "- Eine klare Beschreibung des Problems\r\n" +
                "- Eine schrittweise Beschreibung, wie das Problem reproduziert werden kann (falls möglich)\r\n" +
                "- Welches Ergebnis Sie erwartet haben\r\n" +
                "- Welches Ergebnis tatsächlich eingetreten ist\r\n" +
                "- Wie häufig dieser Fehler auftritt\r\n");
    }

    @FXML
    @SuppressWarnings("unused")
    private void sendmessage(ActionEvent event){
        statuslabel.setText("Feedback wird gesendet. Bitte warten...");
        statuslabel.setTextFill(Color.BLACK);
        boolean mailfail = true;
        String MailAdress;
        String Name;
        String TextMessage;
        //Pruefen ob Name eingegeben wurde
        if (namefield.getText().length() > 2) Name = namefield.getText(); else Name = "Anonymous";
        //Adresse und Nachricht auslesen
        MailAdress = adressfield.getText();
        TextMessage = messagearea.getText();

        try {
            //Pruefen ob E-Mail Adresse gluetigem Format entspricht
            new InternetAddress(adressfield.getText());
            if (MailAdress.contains("@") && MailAdress.contains(".")) {
                String[] checkEMail = MailAdress.split("@");
                if (checkEMail.length == 2) {
                    String[] checkEMail1 = checkEMail[1].split("\\.");

                    if (checkEMail[0] != null && checkEMail1[0] != null && checkEMail1[1] != null) {
                        mailfail = false;
                    }
                }
            }

        }catch(Exception e){
            Controller.writeerror(e);
            mailfail = true;
        }finally {
            if (mailfail){
                statuslabel.setText("Die Mailadresse muss gültig sein!");
                statuslabel.setTextFill(Color.RED);

            }
        }

        if (!mailfail)  //Wenn es keine Fehler gab
        try {
            //Einstellungen fuer die Spaetere uebertragung anlegen
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            //Username und Passwort in Variable speichern
            Session session = Session.getInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });
            //Das eigentliche zu uebertragene "Paket" erstellen
            Message message = new MimeMessage(session);

            //Zieladressen erstellen
            InternetAddress adresses[] = {new InternetAddress(MailAdress),new InternetAddress(SupportMailAdress)};

            //Zieladressen hinzufuegen, Betreff und Nachricht
            message.setRecipients(Message.RecipientType.TO,adresses);
            message.setSubject("Mill - Feedback Assistent");
            Controller.Sout("Try to send Feedback");
            message.setText("Name: " + Name + "\nE-Mail: " + MailAdress + "\nNachricht: " + TextMessage + "\n\nSystem Information:\nOS: " + System.getProperty("os.name") + "\nArchitektur: " + System.getProperty("os.arch") + "\nVersion: " + System.getProperty("os.version") + "\nProgrammVersion: " + VERSION_ASNUMBER);
            //E-Mail senden
            Transport.send(message);
            //Wenn erfolgreich -> Nachricht ausgeben und Felder zuruecksetzen
            Controller.Sout("Feedback Send success");
            statuslabel.setText("Feedback wurde erfolgreich versendet!\nEine Kopie des Feedbacks wurde an Ihre E-Mail Adresse versendet");
            statuslabel.setTextFill(Color.BLACK);
            namefield.setText("");
            adressfield.setText("");
            messagearea.setText("");
        } catch (Exception e) { //Wenn Fehler -> Ausgeben
            statuslabel.setText("Fehler beim Senden des Feedbacks, bitte überprüfe deine Internetverbindung");
            statuslabel.setTextFill(Color.RED);
            Controller.Sout("Failed to send Feedback");
            Controller.writeerror(e);

        }
    }

    //Wenn Feedbak Assistent beendet wird Fenster schliessen
    @SuppressWarnings("unused")
    @FXML
    private void backbuttonaction(ActionEvent event){
        Controller.stage.close();
        Controller.FEEDBACKWINDOW_ACTIVE = false;
        timer.cancel();
    }


}
