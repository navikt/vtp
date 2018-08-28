package no.nav.tjeneste.virksomhet.infotrygd.infotrygdsak.v1.modell;

import java.time.LocalDateTime;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.ytelse.InfotrygdYtelse;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.Behandlingstema;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.InfotrygdVedtak;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.ObjectFactory;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.Resultat;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.Sakstyper;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.Status;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.Tema;

public class InfotrygdVedtakBygger extends InfotrygdSakBygger {
    private LocalDateTime opphoerFom;

    public InfotrygdVedtakBygger(InfotrygdYtelse infotrygdYtelse) {
        super(infotrygdYtelse);
        this.opphoerFom = infotrygdYtelse.getOpphørFom();
    }

    public InfotrygdVedtak byggInfotrygdVedtak() {
        InfotrygdVedtak infotrygdVedtak = new InfotrygdVedtak();
        infotrygdVedtak.setSakId(sakId);
        infotrygdVedtak.setRegistrert(ConversionUtils.convertToXMLGregorianCalendar(this.registrert));

        ObjectFactory of = new ObjectFactory();
        //Tema
        Tema t = of.createTema();
        t.setValue(tema);
        infotrygdVedtak.setTema(t);
        //Behandlingstema
        Behandlingstema bt = of.createBehandlingstema();
        bt.setValue(behandlingstema);
        infotrygdVedtak.setBehandlingstema(bt);
        //Type
        Sakstyper st = of.createSakstyper();
        st.setValue(type);
        infotrygdVedtak.setType(st);
        //InfotrygdSakStatus
        Status s = of.createStatus();
        s.setValue(status);
        infotrygdVedtak.setStatus(s);
        //Resultat
        Resultat r = of.createResultat();
        r.setValue(resultat);
        infotrygdVedtak.setResultat(r);

        infotrygdVedtak.setSaksbehandlerId(this.saksbehandlerId);

        if (vedtatt != null) {
            infotrygdVedtak.setVedtatt(ConversionUtils.convertToXMLGregorianCalendar(vedtatt));
        }
        if (iverksatt != null) {
            infotrygdVedtak.setIverksatt(ConversionUtils.convertToXMLGregorianCalendar(iverksatt));
        }
        if (endret != null) {
            infotrygdVedtak.setEndret(ConversionUtils.convertToXMLGregorianCalendar(endret));
        }
        if (opphoerFom != null) {
            infotrygdVedtak.setOpphoerFom(ConversionUtils.convertToXMLGregorianCalendar(opphoerFom));
        }
        return infotrygdVedtak;
    }
}
