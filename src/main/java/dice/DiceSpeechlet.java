package dice;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
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

public class DiceSpeechlet implements Speechlet {
	private static final Logger log = LoggerFactory.getLogger(DiceSpeechlet.class);
	private static final String SESSION_NUMBEROFSIDES = "diceNumberOfSides";
	private static final String INTENT_CHOOSE = "chooseDice";
	private static final String INTENT_ROLEDICE = "rollDice";
	private static final String SLOT_NUMBEROFSIDES = "sides";
	
	public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		// Initial wird mit einem 6-seitigen Würfel gestartet
		session.setAttribute(SESSION_NUMBEROFSIDES, new Integer(6));
	}

	
	public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText("Willkommen bei Würfel.");
		return SpeechletResponse.newAskResponse(speech, createRepromptSpeech());
	}

	
	public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		System.out.println("Session:"+session+ " Intent:"+request.getIntent().getName());
		String intentName = request.getIntent().getName();
		if (INTENT_CHOOSE.equals(intentName)) {
			return handleChooseDice(request.getIntent(), session);
		} else if (INTENT_ROLEDICE.equals(intentName)) {
			return handleRollDice(session);
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

	private SpeechletResponse handleRollDice(Session session) {
		int maxValue = (Integer) session.getAttribute(SESSION_NUMBEROFSIDES);
		int randomValue = new Random().nextInt(maxValue) + 1;
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText("ich habe eine " + String.valueOf(randomValue) + " gewürfelt.");
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

	private SpeechletResponse handleChooseDice(Intent intent, Session session) {
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		if (intent.getSlot(SLOT_NUMBEROFSIDES).getValue() == null) {
			speech.setText("ich habe nicht verstanden wieviele seiten der würfel haben soll.");		
		} else {
			Integer numberOfSides = Integer.valueOf(intent.getSlot(SLOT_NUMBEROFSIDES).getValue().toString());
			session.setAttribute(SESSION_NUMBEROFSIDES, numberOfSides);
			speech.setText("ich benutze jetzt einen " + numberOfSides.toString() + " seitigen würfel");			
		}
		return SpeechletResponse.newAskResponse(speech, createRepromptSpeech());
	}

	private Reprompt createRepromptSpeech() {
		PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText("ich habe dich nicht verstanden");
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);
		return reprompt;
	}
}
