package no.nav.tjeneste.virksomhet.infotrygd.infotrygdsak.v1.modell;

import java.time.LocalDateTime;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.ytelse.InfotrygdYtelse;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.Behandlingstema;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.InfotrygdSak;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.ObjectFactory;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.Resultat;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.Sakstyper;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.Status;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.Tema;

public class InfotrygdSakBygger {
    protected String sakId;
    protected LocalDateTime registrert;
    protected String tema;
    protected String behandlingstema;
    protected String type;
    protected String status;
    protected String resultat;
    protected String saksbehandlerId;
    protected LocalDateTime vedtatt;
    protected LocalDateTime iverksatt;
    protected LocalDateTime endret;

    public InfotrygdSakBygger(InfotrygdYtelse infotrygdYtelse) {
        this.sakId = infotrygdYtelse.getSakId();
        this.registrert = infotrygdYtelse.getRegistrert();
        this.tema = infotrygdYtelse.getTema().getKode();
        this.behandlingstema = infotrygdYtelse.getBehandlingtema().getKode();
        this.type = infotrygdYtelse.getSakType().getKode();
        this.status = infotrygdYtelse.getSakStatus().getKode();
        this.resultat = infotrygdYtelse.getResultat().getKode();
        this.saksbehandlerId = infotrygdYtelse.getSaksbehandlerId();
        this.vedtatt = infotrygdYtelse.getVedtatt();
        this.iverksatt = infotrygdYtelse.getIverksatt();
        this.endret = infotrygdYtelse.getEndret();
    }

    public InfotrygdSak byggInfotrygdSak() {
        InfotrygdSak infotrygdSak = new InfotrygdSak();
        infotrygdSak.setSakId(sakId);
        infotrygdSak.setRegistrert(ConversionUtils.convertToXMLGregorianCalendar(this.registrert));

        ObjectFactory of = new ObjectFactory();
        //Tema
        Tema t = of.createTema();
        t.setValue(tema);
        infotrygdSak.setTema(t);
        //Behandlingstema
        Behandlingstema bt = of.createBehandlingstema();
        bt.setValue(behandlingstema);
        infotrygdSak.setBehandlingstema(bt);
        //Type
        Sakstyper st = of.createSakstyper();
        st.setValue(type);
        infotrygdSak.setType(st);
        //InfotrygdSakStatus
        Status s = of.createStatus();
        s.setValue(status);
        infotrygdSak.setStatus(s);
        //Resultat
        Resultat r = of.createResultat();
        r.setValue(resultat);
        infotrygdSak.setResultat(r);

        infotrygdSak.setSaksbehandlerId(this.saksbehandlerId);

        if (vedtatt != null) {
            infotrygdSak.setVedtatt(ConversionUtils.convertToXMLGregorianCalendar(vedtatt));
        }
        if (iverksatt != null) {
            infotrygdSak.setIverksatt(ConversionUtils.convertToXMLGregorianCalendar(iverksatt));
        }
        if (endret != null) {
            infotrygdSak.setEndret(ConversionUtils.convertToXMLGregorianCalendar(endret));
        }

        return infotrygdSak;
    }
}
