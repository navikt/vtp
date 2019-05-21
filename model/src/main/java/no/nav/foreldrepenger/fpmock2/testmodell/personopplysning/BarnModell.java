package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("barn")
public class BarnModell extends PersonModell {

    private static final int KJØNNSINDEKS = 8; // pos i FNR

    public BarnModell() {
        super();
    }

    public BarnModell(String lokalIdent, String navn, LocalDate fødselsdato) {
        super(lokalIdent, navn, fødselsdato);
    }

    @Override
    public String getIdent() {
        return getIdenter() == null ? null : getIdenter().getBarnIdentForLokalIdent(getLokalIdent());
    }

    @Override
    public Kjønn getKjønn() {
        // er ikke nødvendigvis satt, avled av fødselsnummer
        Kjønn kjønn = super.getKjønn();
        if (kjønn != null) {
            return kjønn;
        }
        String ident = getIdent();
        int k = Integer.parseInt(Character.toString(ident.charAt(KJØNNSINDEKS)));
        return k % 2 == 0 ? Kjønn.K : Kjønn.M;
    }

}
