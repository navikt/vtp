package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ident")
public class BrukerIdent extends BrukerModell {

    public BrukerIdent() {
        super();
    }

    public BrukerIdent(String ident) {
        super(ident);
    }

    @Override
    public String getIdent() {
        return getIdenter().getIdent(getLokalIdent());
    }

}
