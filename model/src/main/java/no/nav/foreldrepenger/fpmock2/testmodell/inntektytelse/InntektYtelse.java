package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InntektYtelse {

    @JsonInclude(value=Include.ALWAYS)
    @JsonProperty("modeller")
    private List<InntektYtelseModell> modeller = new ArrayList<>();
    
    public InntektYtelse() {
    }
    
    public InntektYtelse(List<InntektYtelseModell> modeller) {
        this.modeller = modeller;
    }
    
    public List<InntektYtelseModell> getModeller() {
        return modeller;
    }

    public void leggTil(InntektYtelseModell iyModell) {
        modeller.add(iyModell);
    }
    
}
