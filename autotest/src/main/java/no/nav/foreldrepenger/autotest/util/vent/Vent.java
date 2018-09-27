package no.nav.foreldrepenger.autotest.util.vent;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class Vent {
    
    public static void til(Callable<Boolean> callable, int timeoutInSeconds, String failReason) throws Exception {
        LocalDateTime end = LocalDateTime.now().plusSeconds(timeoutInSeconds);

        while (!callable.call()) {
            if (LocalDateTime.now().isAfter(end)) {
                throw new RuntimeException(String.format("Async venting timet ut etter %s sekunder fordi: %s", timeoutInSeconds, failReason));
            }
            Thread.sleep(100);
        }
    }
}
