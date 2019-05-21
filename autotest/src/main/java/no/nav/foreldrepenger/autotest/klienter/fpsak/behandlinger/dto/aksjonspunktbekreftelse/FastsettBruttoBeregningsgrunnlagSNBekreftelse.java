package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5042")
public class FastsettBruttoBeregningsgrunnlagSNBekreftelse extends AksjonspunktBekreftelse {

    protected Integer bruttoBeregningsgrunnlag;

    public FastsettBruttoBeregningsgrunnlagSNBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }

    public Integer hentBruttoBeregningsgrunnlag() {
        return bruttoBeregningsgrunnlag;
    }

    public FastsettBruttoBeregningsgrunnlagSNBekreftelse setBruttoBeregningsgrunnlag(Integer bruttoBeregningsgrunnlag) {
        this.bruttoBeregningsgrunnlag = bruttoBeregningsgrunnlag;
        return this;
    }

}
