package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stonadskontoer {
    protected String stonadskontotype;
    protected int maxDager;
    protected  int saldo;
    //protected  List<AktivitetSaldoDto> aktivitetSaldoDtoList;

    public String getStonadskontotype() {return this.stonadskontotype;}

    public int getMaxDager() {return this.maxDager;}

    public int getSaldo() {return this.saldo;}

}
