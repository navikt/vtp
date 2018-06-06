package no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.modell;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.ObjectFactory;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.Periode;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.Vedtak;

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
            if (vedtaksperiodeTom != null) {
                periode.setTom(ConversionUtils.convertToXMLGregorianCalendar(vedtaksperiodeTom));
            }
            if (beslutningsdato != null) {
                vedtak.setBeslutningsdato(ConversionUtils.convertToXMLGregorianCalendar(beslutningsdato));
            }
            vedtak.setPeriodetypeForYtelse(periodetypeForYtelse);
            vedtak.setUttaksgrad(uttaksgrad.intValue());
            vedtak.setVedtakBruttoBeloep(vedtakBruttoBeloep.intValue());
            vedtak.setVedtakNettoBeloep(vedtakNettoBeloep.intValue());
            vedtak.setVedtaksperiode(periode);
            vedtak.setStatus(status);
            vedtak.setVedtakstype(vedtakstype);
            vedtak.setAktivitetsfase(aktivitetsfase);
            vedtak.setDagsats(dagsats.intValue());

        } catch (Exception e) {
            e.getMessage();
        }

        return vedtak;
    }

}
