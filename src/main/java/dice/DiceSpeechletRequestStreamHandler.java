package dice;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;


public class DiceSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {

    private static final Set<String> supportedApplicationIds;

    static {
 
        supportedApplicationIds = new HashSet<String>();
        
    }

    public DiceSpeechletRequestStreamHandler() {
        super(new DiceSpeechlet(), supportedApplicationIds);
    }

    public DiceSpeechletRequestStreamHandler(Speechlet speechlet,
            Set<String> supportedApplicationIds) {
        super(speechlet, supportedApplicationIds);
    }

}
