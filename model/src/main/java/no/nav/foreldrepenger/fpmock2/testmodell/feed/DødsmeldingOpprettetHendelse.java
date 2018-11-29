package no.nav.foreldrepenger.fpmock2.testmodell.feed;

public class DødsmeldingOpprettetHendelse extends PersonHendelse {
    private static final String TYPE = "DOEDSMELDINGOPPRETTET";

    @Override
    public String getType() {
        return TYPE;
    }

}
