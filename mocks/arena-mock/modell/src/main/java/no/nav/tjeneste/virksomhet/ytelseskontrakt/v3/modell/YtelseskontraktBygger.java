package no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.modell;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.Ytelseskontrakt;

import java.time.LocalDateTime;
import java.util.List;

public class YtelseskontraktBygger {

    protected Long id;
    protected LocalDateTime datoKravMottatt;
    protected LocalDateTime fomGyldighetsperiode;
    protected LocalDateTime tomGyldighetsperiode;
    protected Long fagsystemSakId;
    protected String status;
    protected String ytelsestype;
    protected List<ArenaVedtak> ihtVedtak;
    protected Long bortfallsprosentDagerIgjen;
    protected Long bortfallsprosentUkerIgjen;


    public YtelseskontraktBygger(ArenaYtelseskontrakt ytelseskontrakt, List<ArenaVedtak> arenaVedtak) {

        this.id = ytelseskontrakt.getId();
        this.datoKravMottatt = ytelseskontrakt.getDatoKravMottatt();
        this.fomGyldighetsperiode = ytelseskontrakt.getFomGyldighetsperiode();
        this.tomGyldighetsperiode = ytelseskontrakt.getTomGyldighetsperiode();
        this.fagsystemSakId = ytelseskontrakt.getFagsystemSakId();
        this.status = ytelseskontrakt.getStatus();
        this.ytelsestype = ytelseskontrakt.getYtelsestype();
        this.ihtVedtak = arenaVedtak;
        this.bortfallsprosentDagerIgjen = ytelseskontrakt.getBortfallsprosentDagerIgjen();
        this.bortfallsprosentUkerIgjen = ytelseskontrakt.getBortfallsprosentUkerIgjen();
    }


    public Ytelseskontrakt byggYtelseskontrakt() {

        Ytelseskontrakt ytelseskontrakt = new Ytelseskontrakt();

        for (ArenaVedtak v : ihtVedtak) {
            VedtakBygger vedtakBygger = new VedtakBygger(v);
            ytelseskontrakt.getIhtVedtak().add(vedtakBygger.byggVedtak());
        }


        try {
            ytelseskontrakt.setDatoKravMottatt(ConversionUtils.convertToXMLGregorianCalendar(datoKravMottatt));
            ytelseskontrakt.setFomGyldighetsperiode(ConversionUtils.convertToXMLGregorianCalendar(fomGyldighetsperiode));
            ytelseskontrakt.setTomGyldighetsperiode(ConversionUtils.convertToXMLGregorianCalendar(tomGyldighetsperiode));
            ytelseskontrakt.setFagsystemSakId(fagsystemSakId.intValue());
            ytelseskontrakt.setStatus(status);
            ytelseskontrakt.setYtelsestype(ytelsestype);
            ytelseskontrakt.setBortfallsprosentDagerIgjen(bortfallsprosentDagerIgjen.intValue());
            ytelseskontrakt.setBortfallsprosentUkerIgjen(bortfallsprosentUkerIgjen.intValue());
        }
        catch (Exception e) {
            e.getMessage();
        }

        return ytelseskontrakt;
}
}

