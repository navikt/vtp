package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("person")
public class PersonArbeidsgiver extends PersonModell {

    private static final Pattern NUMERIC = Pattern.compile("^\\d+$");

    public PersonArbeidsgiver() {
        super();
    }

    public PersonArbeidsgiver(String ident) {
        super(ident, "Privat Arbeidsgiver", null);
    }

    @Override
    public String getIdent() {
        // trenger kjønn for å få FNR. Tar en spansk en
        var lokalIdent = getLokalIdent();
        if (lokalIdent != null && NUMERIC.matcher(lokalIdent).matches()) {
            return lokalIdent;
        }
        Kjønn kjønn;
        if (lokalIdent == null) {
            kjønn = Kjønn.K;
        } else {
            kjønn = lokalIdent.hashCode() % 2 == 0 ? Kjønn.K : Kjønn.M;
        }
        return getIdenter() == null
            ? null
            : getIdenter().getVoksenIdentForLokalIdent(lokalIdent, kjønn);
    }

}
