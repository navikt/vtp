package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("person")
public class PersonArbeidsgiver extends BrukerModell {

    private static final Pattern NUMERIC = Pattern.compile("^\\d+$");
    
    public PersonArbeidsgiver() {
        super();
    }

    public PersonArbeidsgiver(String ident) {
        super(ident);
    }

    @Override
    public String getIdent() {
        // trenger kjønn for å få FNR. Tar en spansk en
        String lokalIdent = getLokalIdent();
        if (lokalIdent != null && NUMERIC.matcher(lokalIdent).matches()) {
            return lokalIdent;
        }
        Kjønn kjønn = lokalIdent.hashCode() % 2 == 0 ? Kjønn.K : Kjønn.M;
        return getIdenter() == null
            ? null
            : getIdenter().getVoksenIdentForLokalIdent(lokalIdent, kjønn);
    }

}
