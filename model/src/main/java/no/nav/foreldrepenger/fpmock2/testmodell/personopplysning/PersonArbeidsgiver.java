package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("person")
public class PersonArbeidsgiver extends BrukerModell {

    public PersonArbeidsgiver() {
        super();
    }

    public PersonArbeidsgiver(String ident) {
        super(ident);
    }

    @Override
    public String getIdent() {
        // trenger kjønn for å få FNR. Tar en spansk en
        Kjønn kjønn = getLokalIdent().hashCode() % 2 == 0 ? Kjønn.K : Kjønn.M;
        return getIdenter() == null
            ? null
            : getIdenter().getVoksenIdentForLokalIdent(getLokalIdent(), kjønn);
    }

}
