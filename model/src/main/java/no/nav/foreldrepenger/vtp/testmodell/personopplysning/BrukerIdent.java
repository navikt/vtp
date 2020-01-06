package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

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
        String lokalIdent = getLokalIdent();
        return getIdenter() == null ? null : getIdenter().getIdent(lokalIdent);
    }

}
