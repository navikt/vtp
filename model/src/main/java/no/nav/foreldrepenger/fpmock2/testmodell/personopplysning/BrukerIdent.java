package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ident")
public class BrukerIdent extends BrukerModell {

    @Override
    public String getIdent() {
        return getIdenter().getIdent(getLokalIdent());
    }

}
