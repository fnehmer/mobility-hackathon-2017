package mobilityPan;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;


public class MobilitySpeechlet implements Speechlet {
	private static final Logger log = LoggerFactory.getLogger(MobilitySpeechlet.class);
	private static final String SESSION_NUMBEROFBIKES = "bikeCount";
	private static final String INTENT_BIKESTATION = "getBikeStation";
	private static final String INTENT_BIKECOUNT = "getBikeCount";
	private static final String SLOT_STATIONNAME = "name";
	private static final String INTENT_FRIEND = "friendtrivia";
	private static final String SLOT_FRIENDNAME = "friendname";
	
	public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		// Initial wird mit einem 6-seitigen Würfel gestartet
		session.setAttribute(SESSION_NUMBEROFBIKES, new Integer(6));
	}

	
	public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText("Willkommen bei MobilityMate. Wie kann ich dir helfen?");
		return SpeechletResponse.newAskResponse(speech, createRepromptSpeech());
	}

	
	public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		System.out.println("Session:"+session+ " Intent:"+request.getIntent().getName());
		String intentName = request.getIntent().getName();
		if (INTENT_BIKESTATION.equals(intentName)) {
			return handleBikeStation();
		} else if (INTENT_BIKECOUNT.equals(intentName)) {
			return handleBikeCount();
		} else if (INTENT_FRIEND.equals(intentName)) {
			return handleFriendTrivia(request.getIntent());
		} else if ("AMAZON.HelpIntent".equals(intentName)) {
			return handleHelpIntent();
		} else if ("AMAZON.StopIntent".equals(intentName)) {
			return handleStopIntent();
		} else {
			throw new SpeechletException("Invalid Intent");
		}
	}

	
	public void onSessionEnded(final SessionEndedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
	}

	
	private SpeechletResponse handleBikeStation() {
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText("Dies ist ein Beispiel: Die nächste Stadtrad Station ist Saarlandstrasse. Dort sind aktuell 12 Fahrräder verfügbar.");
		return SpeechletResponse.newAskResponse(speech, createRepromptSpeech());
	}
	
	
	private SpeechletResponse handleBikeCount() {
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText("Dies ist ein Beispiel: An der Station Saarlandstrasse sind aktuell 12 Fahrräder verfügbar.");
		return SpeechletResponse.newAskResponse(speech, createRepromptSpeech());		
	
	}
	
	
	private SpeechletResponse handleFriendTrivia(Intent intent) {
		
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		int factIndex;
        String fact;
        String speechText = null;

		
		if (intent.getSlot(SLOT_FRIENDNAME).getValue() == null) {
			speech.setText("Du musst den Namen einen Freundes nennen.");		
		} else {
			   
	        String intentName = intent.getSlot(SLOT_FRIENDNAME).getValue();
	        switch(intentName){
	     
	        case "Jonas":
	        	factIndex = (int) Math.floor(Math.random() * JONAS_FACTS.length);
		        fact = JONAS_FACTS[factIndex];
		        speechText = fact;
	        	break;
	        case "Pinguine":
	        case "Winn":
	        case "Pingu":
	        	factIndex = (int) Math.floor(Math.random() * VINH_FACTS.length);
		        fact = VINH_FACTS[factIndex];
		        speechText = fact;
	        	break;
	        case "Flo":
	        	factIndex = (int) Math.floor(Math.random() * FLO_FACTS.length);
		        fact = FLO_FACTS[factIndex];
		        speechText = fact;
	        	break;
	        case "Pandas":
	        case "Jenny":
	        case "Apfel":
	        	factIndex = (int) Math.floor(Math.random() * PANDA_FACTS.length);
		        fact = PANDA_FACTS[factIndex];
		        speechText = fact;
	        	break;
	       default:
		        speechText = "Ich habe den Namen leider nicht verstanden.";
	        	break;
	        }

	        speech.setText(speechText);		
	       
		}
		return SpeechletResponse.newAskResponse(speech, createRepromptSpeech());
	
	}

	
	private SpeechletResponse handleStopIntent() {
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText("auf wiedersehen.");
		return SpeechletResponse.newTellResponse(speech);
	}

	private SpeechletResponse handleHelpIntent() {
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText("bestimme wieviel seiten dein würfel hat oder würfle");
		return SpeechletResponse.newTellResponse(speech);
	}

	

	private Reprompt createRepromptSpeech() {
		PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText("ich habe dich nicht verstanden");
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);
		return reprompt;
	}
	

    private static final String[] VINH_FACTS = new String[] {
            "Pinguine sind verfressen.",
            "Quas, wex, exort!",
            "Pinguine stinken."
    };
    
    private static final String[] JONAS_FACTS = new String[] {
            "Jonas liebt Katzen.",
            "Jonas nennt man auch Silencio.",
            "Jonas hat Modelbeine."
    };
    
    private static final String[] FLO_FACTS = new String[] {
            "Flo liebt Laura.",
            "Flo spielt gerne Magnus.",
            "Flo hat einen Bruder."
    };
    
    private static final String[] PANDA_FACTS = new String[] {
            "Pandas sind flauschig.",
            "Pandas sind verfressen.",
            "Pandas schlagen gerne Pinguine."
    };
	
	
}
