package no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.modell;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.Ytelseskontrakt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class YtelseskontraktBygger {

    protected Long id;
    protected LocalDateTime datoKravMottatt;
    protected Long fagsystemSakId;
    protected String status;
    protected String ytelsestype;
    protected List<ArenaVedtak> ihtVedtak;
    protected Long bortfallsprosentDagerIgjen;
    protected Long bortfallsprosentUkerIgjen;


    public YtelseskontraktBygger(ArenaYtelseskontrakt ytelseskontrakt, List<ArenaVedtak> arenaVedtak) {

        this.id = ytelseskontrakt.getId();
        this.datoKravMottatt = ytelseskontrakt.getDatoKravMottatt();
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
        }
        catch (Exception e) {
            e.getMessage();
        }
        try {
            ytelseskontrakt.setFagsystemSakId(fagsystemSakId.intValue());
        }
        catch (NullPointerException ne) {
            ne.getMessage();
        }
        try {
            ytelseskontrakt.setStatus(status);
        }
        catch (NullPointerException n) {
            n.getMessage();
        }
        try {
            ytelseskontrakt.setYtelsestype(ytelsestype);
        }
        catch (NullPointerException ne) {
            ne.getMessage();
        }
        try {
            ytelseskontrakt.setBortfallsprosentDagerIgjen(bortfallsprosentDagerIgjen.intValue());
        }
        catch (Exception e) {
            e.getMessage();
        }
        try {
            ytelseskontrakt.setBortfallsprosentUkerIgjen(bortfallsprosentUkerIgjen.intValue());
        }
        catch (Exception e) {
            e.getMessage();
        }

        return ytelseskontrakt;
}
}

