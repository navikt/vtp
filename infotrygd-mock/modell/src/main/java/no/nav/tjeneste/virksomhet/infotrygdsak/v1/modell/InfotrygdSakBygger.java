package no.nav.tjeneste.virksomhet.infotrygdsak.v1.modell;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.Behandlingstema;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.InfotrygdSak;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.ObjectFactory;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.Resultat;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.Sakstyper;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.Status;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.Tema;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;

public class InfotrygdSakBygger {

    protected Long id;
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
    protected LocalDateTime opphoerFom;


    public InfotrygdSakBygger(InfotrygdYtelse infotrygdYtelse){

        this.id = infotrygdYtelse.getId();
        this.sakId = infotrygdYtelse.getSakId();
        this.registrert = infotrygdYtelse.getRegistrert();
        this.tema = infotrygdYtelse.getTema();
        this.behandlingstema = infotrygdYtelse.getBehandlingstema();
        this.type = infotrygdYtelse.getType();
        this.status = infotrygdYtelse.getStatus();
        this.resultat = infotrygdYtelse.getResultat();
        this.saksbehandlerId = infotrygdYtelse.getSaksbehandlerId();
        this.vedtatt = infotrygdYtelse.getVedtatt();
        this.iverksatt = infotrygdYtelse.getIverksatt();
        this.endret = infotrygdYtelse.getEndret();
        this.opphoerFom = infotrygdYtelse.getOpphoerFom();
    }


    public InfotrygdSak byggInfotrygdSak(){
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
        Sakstyper st =of.createSakstyper();
        st.setValue(type);
        infotrygdSak.setType(st);
        //Status
        Status s = of.createStatus();
        s.setValue(status);
        infotrygdSak.setStatus(s);
        //Resultat
        Resultat r = of.createResultat();
        r.setValue(resultat);
        infotrygdSak.setResultat(r);

        infotrygdSak.setSaksbehandlerId(this.saksbehandlerId);

        try{
            if(vedtatt != null){
                infotrygdSak.setVedtatt(ConversionUtils.convertToXMLGregorianCalendar(vedtatt));
            }
        } catch(NullPointerException npe){
        }

        try{
            if(iverksatt != null){
                infotrygdSak.setIverksatt(ConversionUtils.convertToXMLGregorianCalendar(iverksatt));
            }
        } catch(NullPointerException npe){
        }

        try{
            if(endret != null){
                infotrygdSak.setEndret(ConversionUtils.convertToXMLGregorianCalendar(endret));
            }
        } catch(NullPointerException npe){
        }

        try{
            if(opphoerFom != null){
                infotrygdSak.setOpphoerFom(ConversionUtils.convertToXMLGregorianCalendar(opphoerFom));
            }
        } catch(NullPointerException npe){
        }

        return infotrygdSak;
    }

}
