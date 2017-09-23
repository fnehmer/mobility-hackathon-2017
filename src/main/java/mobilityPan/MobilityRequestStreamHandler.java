package mobilityPan;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;


public class MobilityRequestStreamHandler extends SpeechletRequestStreamHandler {

    private static final Set<String> supportedApplicationIds;

    static {
 
        supportedApplicationIds = new HashSet<String>();
        
    }

    public MobilityRequestStreamHandler() {
        super(new MobilitySpeechlet(), supportedApplicationIds);
    }

    public MobilityRequestStreamHandler(Speechlet speechlet,
            Set<String> supportedApplicationIds) {
        super(speechlet, supportedApplicationIds);
    }

}
