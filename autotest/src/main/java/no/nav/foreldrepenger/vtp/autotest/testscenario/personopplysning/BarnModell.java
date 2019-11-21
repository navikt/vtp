package no.nav.foreldrepenger.vtp.autotest.testscenario.personopplysning;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.time.LocalDate;

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
    public BrukerModell.Kjønn getKjønn() {
        // er ikke nødvendigvis satt, avled av fødselsnummer
        BrukerModell.Kjønn kjønn = super.getKjønn();
        if (kjønn != null) {
            return kjønn;
        }
        String ident = getIdent();
        int k = Integer.parseInt(Character.toString(ident.charAt(KJØNNSINDEKS)));
        return k % 2 == 0 ? BrukerModell.Kjønn.K : BrukerModell.Kjønn.M;
    }

}
