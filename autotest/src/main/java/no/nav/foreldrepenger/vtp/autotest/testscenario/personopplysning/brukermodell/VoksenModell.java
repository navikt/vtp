package no.nav.foreldrepenger.vtp.autotest.testscenario.personopplysning.brukermodell;


import java.time.LocalDate;

public abstract class VoksenModell extends PersonModell {

    public VoksenModell() {
    }

    public VoksenModell(String lokalIdent, String navn, LocalDate fødselsdato, BrukerModell.Kjønn kjønn) {
        super(lokalIdent, navn, fødselsdato);
        setKjønn(kjønn);
    }

    @Override
    public String getIdent() {
        return getIdenter() == null
            ? null
            : getIdenter().getVoksenIdentForLokalIdent(getLokalIdent(), getKjønn());
    }
}
