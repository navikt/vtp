package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class VoksenModell extends PersonModell {

    @JsonProperty("harUføretrygd")
    private boolean harUføretrygd;

    public VoksenModell() {
    }

    public VoksenModell(String lokalIdent, String navn, LocalDate fødselsdato, boolean harUføretrygd, Kjønn kjønn) {
        super(lokalIdent, navn, fødselsdato);
        this.harUføretrygd = harUføretrygd;
        setKjønn(kjønn);
    }

    public boolean harUføretrygd() {
        return harUføretrygd;
    }

    @Override
    public String getIdent() {
        return getIdenter() == null
            ? null
            : getIdenter().getVoksenIdentForLokalIdent(getLokalIdent(), getKjønn());
    }
}
