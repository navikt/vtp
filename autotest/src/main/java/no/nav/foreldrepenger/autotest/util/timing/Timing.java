package no.nav.foreldrepenger.autotest.util.timing;

import java.time.Duration;
import java.time.LocalDateTime;

public class Timing {
    
    LocalDateTime intervalStart;
    
    public void startTiming() {
        intervalStart = LocalDateTime.now();
    }
    
    public void endInterval (String intervalAction){
        LocalDateTime intervalEnd = LocalDateTime.now();
        Duration duration = Duration.between(intervalStart, intervalEnd);
        System.out.println("Ferdig " + intervalAction + ". Tok " + duration.toMillis() + " millisekunder - " + intervalEnd);
        intervalStart = intervalEnd;
    }
    
}