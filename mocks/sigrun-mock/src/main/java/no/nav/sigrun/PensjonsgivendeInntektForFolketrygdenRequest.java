package no.nav.sigrun;

public record PensjonsgivendeInntektForFolketrygdenRequest(
    String personident,
    String inntektsaar,
    String rettighetspakke) {
}
