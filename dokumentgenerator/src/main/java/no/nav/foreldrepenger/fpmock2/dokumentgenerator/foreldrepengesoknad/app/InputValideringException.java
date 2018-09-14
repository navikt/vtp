package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.app;

public class InputValideringException extends Exception {

    public InputValideringException(String message, Exception e) {
        super(message, e);
    }
}