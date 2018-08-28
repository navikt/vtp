package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.fpmock2.testmodell.Feilkode;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ArenaModell {
    
    @JsonProperty("feilkode")
    private Feilkode feilkode;
    
    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("saker")
    private List<ArenaSak> saker = new ArrayList<>();

    public Feilkode getFeilkode() {
        return feilkode;
    }

    public void setFeilkode(Feilkode feilkode) {
        this.feilkode = feilkode;
    }

    public List<ArenaSak> getSaker() {
        return Collections.unmodifiableList(saker);
    }
    
    public void leggTil(ArenaSak sak) {
        this.saker.add(sak);
    }

}
