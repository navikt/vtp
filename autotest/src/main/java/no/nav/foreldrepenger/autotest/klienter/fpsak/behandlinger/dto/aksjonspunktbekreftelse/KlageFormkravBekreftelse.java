package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

public abstract class KlageFormkravBekreftelse extends AksjonspunktBekreftelse {

    protected boolean erKlagerPart;
    protected boolean erFristOverholdt;
    protected boolean erKonkret;
    protected boolean erSignert;
    protected Long vedtak; // påKlagdBehandlingsId;

    public KlageFormkravBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }

    public KlageFormkravBekreftelse erKlagerPart(boolean verdi) {
        erKlagerPart = verdi;
        return this;
    }

    public KlageFormkravBekreftelse erFristOverholdt(boolean verdi) {
        erFristOverholdt = verdi;
        return this;
    }

    public KlageFormkravBekreftelse erKonkret(boolean verdi) {
        erKonkret = verdi;
        return this;
    }

    public KlageFormkravBekreftelse erSignert(boolean verdi) {
        erSignert = verdi;
        return this;
    }

    public KlageFormkravBekreftelse setPåklagdVedtak(Long vedtakId) {
        this.vedtak = vedtakId;
        return this;
    }

    public KlageFormkravBekreftelse godkjennAlleFormkrav(Long vedtakId) {
        erKlagerPart(true);
        erFristOverholdt(true);
        erKonkret(true);
        erSignert(true);
        this.vedtak = vedtakId;
        return this;
    }

    public KlageFormkravBekreftelse klageErIkkeKonkret(Long vedtakId) {
        erKlagerPart(true);
        erFristOverholdt(true);
        erKonkret(false);
        erSignert(true);
        this.vedtak = vedtakId;
        return this;
    }


    @BekreftelseKode(kode="5082")
    public static class KlageFormkravNfp extends KlageFormkravBekreftelse {

        public KlageFormkravNfp(Fagsak fagsak, Behandling behandling) {
            super(fagsak, behandling);
        }
    }

    @BekreftelseKode(kode="5083")
    public static class KlageFormkravKa extends KlageFormkravBekreftelse {

        public KlageFormkravKa(Fagsak fagsak, Behandling behandling) {
            super(fagsak, behandling);
        }
    }


}
