package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MottarYtelse {

    protected boolean frilansMottarYtelse;
    protected List<ArbeidstakerandelUtenIMMottarYtelse> arbeidstakerUtenIMMottarYtelse;

    public MottarYtelse(boolean frilansMottarYtelse, List<ArbeidstakerandelUtenIMMottarYtelse> arbeidstakerandelUtenIMMottarYtelses){
        this.frilansMottarYtelse = frilansMottarYtelse;
        this.arbeidstakerUtenIMMottarYtelse = arbeidstakerandelUtenIMMottarYtelses;
    }

    public void setFrilansMottarYtelse(boolean frilansMottarYtelse) {
        this.frilansMottarYtelse = frilansMottarYtelse;
    }

    public void leggTilArbeidstakerandelUtenIMMottarYtelse(ArbeidstakerandelUtenIMMottarYtelse arbeidstakerandelUtenIMMottarYtelse){
        this.arbeidstakerUtenIMMottarYtelse.add(arbeidstakerandelUtenIMMottarYtelse);
    }

}
