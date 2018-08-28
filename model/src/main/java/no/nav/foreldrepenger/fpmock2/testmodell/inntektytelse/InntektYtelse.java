package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import no.nav.foreldrepenger.fpmock2.testmodell.ScenarioIdenter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InntektYtelse {

    @JsonInclude(value=Include.ALWAYS)
    @JsonProperty("modeller")
    private List<InntektYtelseModell> modeller = new ArrayList<>();
    
    @JacksonInject
    private ScenarioIdenter identer;
    
    public InntektYtelse() {
    }
    
    public InntektYtelse(List<InntektYtelseModell> modeller) {
        this.modeller = modeller;
    }
    
    public List<InntektYtelseModell> getModeller() {
        return modeller;
    }

    public void setIdenter(ScenarioIdenter identer) {
        this.identer = identer;
    }

    public void leggTil(InntektYtelseModell iyModell) {
        modeller.add(iyModell);
    }
    
}
