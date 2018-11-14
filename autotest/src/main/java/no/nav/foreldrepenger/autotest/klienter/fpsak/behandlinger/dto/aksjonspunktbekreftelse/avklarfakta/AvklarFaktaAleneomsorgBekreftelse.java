package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

import java.time.LocalDate;
import java.util.List;

@BekreftelseKode(kode="5060")
public class AvklarFaktaAleneomsorgBekreftelse extends AksjonspunktBekreftelse{

    protected Boolean aleneomsorg;
    protected Boolean omsorg;
    protected List<PeriodeOmsorg> ikkeOmsorgPerioder;

    public AvklarFaktaAleneomsorgBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }

    public AvklarFaktaAleneomsorgBekreftelse harAleneomsorg() {
        this.aleneomsorg = true;
        return this;
    }

    public AvklarFaktaAleneomsorgBekreftelse harIkkeAleneomsorg() {
        this.omsorg = true;
        return this;
    }

    public class PeriodeOmsorg {
        protected LocalDate periodeFom;
        protected LocalDate periodeTom;

        public LocalDate getPeriodeFom() { return periodeFom;}

        public void setPeriodeFom(LocalDate periodeFom) { this.periodeFom = periodeFom; }

        public LocalDate getPeriodeTom() { return periodeTom; }

        public void setPeriodeTom(LocalDate periodeTom) { this.periodeTom = periodeTom; }
    }
}
