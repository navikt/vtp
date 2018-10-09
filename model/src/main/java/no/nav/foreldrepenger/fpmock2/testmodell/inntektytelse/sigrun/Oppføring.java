package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.sigrun;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Oppføring {

    @JsonProperty("tekniskNavn")
    private String tekniskNavn;

    @JsonProperty("beløp")
    private Integer belop;

    public String getTekniskNavn() { return tekniskNavn; }

    public void setTekniskNavn(String tekniskNavn) { this.tekniskNavn = tekniskNavn; }

    public Integer getBelop() { return belop; }

    public void setBelop(Integer belop) { this.belop = belop; }

    @Override
    public String toString(){
        return "{\n" + "\"tekniskNavn\"" + ": \""+ tekniskNavn + "\"," + "\n" + "\"beløp\"" + ": \"" + belop +"\"\n}";
    }
}
