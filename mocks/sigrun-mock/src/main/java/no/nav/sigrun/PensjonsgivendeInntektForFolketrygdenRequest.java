package no.nav.sigrun;

public record PensjonsgivendeInntektForFolketrygdenRequest(
    String personident,
    String inntektsaar,
    String rettighetspakke) {

    public PensjonsgivendeInntektForFolketrygdenRequest(String personident, String inntektsaar) {
        this(personident, inntektsaar, "navpleieogomsorgspenger");
    }
}
