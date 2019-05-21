package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.sigrun;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Oppføring {

    @JsonProperty("tekniskNavn")
    private String tekniskNavn;

    @JsonProperty("beløp")
    private String verdi;

    public String getTekniskNavn() { return tekniskNavn; }

    public void setTekniskNavn(String tekniskNavn) { this.tekniskNavn = tekniskNavn; }

    public String getVerdi() { return verdi; }

    public void setVerdi(String verdi) { this.verdi = verdi; }

    @Override
    public String toString(){
        return "{\n" + "\"tekniskNavn\"" + ": \""+ tekniskNavn + "\"," + "\n" + "\"verdi\"" + ": \"" + verdi +"\"\n}";
    }
}
