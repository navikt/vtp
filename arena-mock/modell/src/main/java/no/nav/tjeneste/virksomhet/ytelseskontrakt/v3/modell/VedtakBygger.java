package no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.modell;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.ObjectFactory;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.Periode;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.Vedtak;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VedtakBygger {
    protected Long id;
    protected LocalDateTime beslutningsdato;
    protected String periodetypeForYtelse;
    protected Long uttaksgrad;
    protected Long vedtakBruttoBeloep;
    protected Long vedtakNettoBeloep;
    protected LocalDateTime vedtaksperiodeFom;
    protected LocalDateTime vedtaksperiodeTom;
    protected String status;
    protected String vedtakstype;
    protected String aktivitetsfase;
    protected Long dagsats;


    public VedtakBygger(ArenaVedtak arenaVedtak) {

        this.id = arenaVedtak.getId();
        this.beslutningsdato = arenaVedtak.getBeslutningsdato();
        this.periodetypeForYtelse = arenaVedtak.getPeriodetypeForYtelse();
        this.uttaksgrad = arenaVedtak.getUttaksgrad();
        this.vedtakBruttoBeloep = arenaVedtak.getVedtakBruttoBeloep();
        this.vedtakNettoBeloep = arenaVedtak.getVedtakNettoBeloep();
        this.vedtaksperiodeFom = arenaVedtak.getVedtakseriodeFom();
        this.vedtaksperiodeTom = arenaVedtak.getVedtaksperiodeTom();
        this.status = arenaVedtak.getStatus();
        this.vedtakstype = arenaVedtak.getVedtaksType();
        this.aktivitetsfase = arenaVedtak.getAktivitetsfase();
        this.dagsats = arenaVedtak.getDagsats();
    }


    public Vedtak byggVedtak() {

        Vedtak vedtak = new Vedtak();
        ObjectFactory of = new ObjectFactory();
        Periode periode = of.createPeriode();

        try {
            if (vedtaksperiodeFom != null) {
                periode.setFom(ConversionUtils.convertToXMLGregorianCalendar(vedtaksperiodeFom));
            }
        } catch (Exception e) {
        }

        try {
            if (vedtaksperiodeTom != null) {
                periode.setTom(ConversionUtils.convertToXMLGregorianCalendar(vedtaksperiodeTom));
            }
        } catch (Exception e) {
        }

        try {
            if (beslutningsdato != null) {
                vedtak.setBeslutningsdato(ConversionUtils.convertToXMLGregorianCalendar(beslutningsdato));
            }
        } catch (Exception e) {
        }

        try {
            vedtak.setPeriodetypeForYtelse(periodetypeForYtelse);
            vedtak.setUttaksgrad(Integer.valueOf(uttaksgrad.intValue()));
            vedtak.setVedtakBruttoBeloep(Integer.valueOf(vedtakBruttoBeloep.intValue()));
            vedtak.setVedtakNettoBeloep(Integer.valueOf(vedtakNettoBeloep.intValue()));
            vedtak.setVedtaksperiode(periode);
            vedtak.setStatus(status);
            vedtak.setVedtakstype(vedtakstype);
            vedtak.setAktivitetsfase(aktivitetsfase);
            vedtak.setDagsats(dagsats.intValue());
        }
        catch(NullPointerException npe){
            npe.getMessage();
        }

        return vedtak;
    }

}
