package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.RelatertYtelseTema;

public class ArenaSak {

    @JsonProperty("saksnummer")
    private String saksnummer;
    
    @JsonProperty("tema")
    private RelatertYtelseTema tema;
    
    @JsonProperty("status")
    private SakStatus status;
    
    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("vedtak")
    private List<ArenaVedtak> vedtak = new ArrayList<>();

    public String getSaksnummer() {
        return saksnummer;
    }

    public void setSaksnummer(String saksnummer) {
        this.saksnummer = saksnummer;
    }

    public RelatertYtelseTema getTema() {
        return tema;
    }

    public void setTema(RelatertYtelseTema tema) {
        this.tema = tema;
    }

    public SakStatus getStatus() {
        return status;
    }

    public void setStatus(SakStatus status) {
        this.status = status;
    }
    
    public void leggTil(ArenaVedtak vedtak) {
        this.vedtak.add(vedtak);
    }
    
    public List<ArenaVedtak> getVedtak() {
        return Collections.unmodifiableList(vedtak);
    }
}
