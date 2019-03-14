package no.nav.foreldrepenger.autotest.util.vent;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Callable;

import org.threeten.extra.Interval;

public class Vent {

    public static void til(Callable<Boolean> callable, int timeoutInSeconds, String failReason) throws Exception {
        til(callable, timeoutInSeconds, new Callable<String>() {

            @Override
            public String call() throws Exception {
                return failReason;
            }
        });
    }

    public static void til(Callable<Boolean> callable, int timeoutInSeconds, Callable<String> errorMessageProducer) throws Exception {
        LocalDateTime start = LocalDateTime.now(); 
        LocalDateTime end = start.plusSeconds(timeoutInSeconds);
        
        while (!callable.call()) {
            if (LocalDateTime.now().isAfter(end)) {
                throw new RuntimeException(String.format("Async venting timet ut etter %s sekunder fordi: %s", timeoutInSeconds, errorMessageProducer.call()));
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(
                    String.format("Async venting interrupted ut etter %s sekunder fordi: %s", ChronoUnit.SECONDS.between(start, LocalDateTime.now()), errorMessageProducer.call()), e);
            }
        }
    }
}
