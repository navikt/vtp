package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FamilierelasjonModell {

    public enum Rolle {
        EKTE, BARN, FARA(true), MORA(true), SAMB, REPA, MMOR(true);

        private boolean forelder;

        private Rolle() {
        }

        private Rolle(boolean forelder) {
            this.forelder = forelder;
        }

        public boolean erForelder() {
            return forelder;
        }
    }

    @JsonProperty("rolle")
    private Rolle rolle;

    @JsonProperty("til")
    private BrukerModell til;

    @JsonProperty("sammeBosted")
    private Boolean sammeBosted;

    public FamilierelasjonModell() {
    }

    public FamilierelasjonModell(Rolle rolle, BrukerModell til) {
        this.rolle = rolle;
        this.til = til;
    }

    public Rolle getRolle() {
        return rolle;
    }

    public boolean getSammeBosted() {
        return sammeBosted == null ? false : sammeBosted;
    }

    public BrukerModell getTil() {
        return til;
    }

    public void setRolle(Rolle rolle) {
        this.rolle = rolle;
    }

    public void setSammeBosted(boolean sammeBosted) {
        this.sammeBosted = sammeBosted;
    }

    public void setTil(BrukerModell til) {
        this.til = til;
    }

    public String getRolleKode() {
        return getRolle().name();
    }

}
