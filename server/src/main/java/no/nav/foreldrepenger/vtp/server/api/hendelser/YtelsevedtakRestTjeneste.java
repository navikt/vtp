package no.nav.foreldrepenger.vtp.server.api.hendelser;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.abakus.vedtak.ytelse.Aktør;
import no.nav.abakus.vedtak.ytelse.Desimaltall;
import no.nav.abakus.vedtak.ytelse.Kildesystem;
import no.nav.abakus.vedtak.ytelse.Periode;
import no.nav.abakus.vedtak.ytelse.Status;
import no.nav.abakus.vedtak.ytelse.Ytelser;
import no.nav.abakus.vedtak.ytelse.v1.YtelseV1;
import no.nav.abakus.vedtak.ytelse.v1.anvisning.Anvisning;
import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaProducer;
import no.nav.foreldrepenger.vtp.kontrakter.hendelser.YtelsevedtakDto;
import no.nav.vedtak.mapper.json.DefaultJsonMapper;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.person.ident.PersonIdent;

@Path("/api/hendelser/ytelsevedtak")
public class YtelsevedtakRestTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(YtelsevedtakRestTjeneste.class);

    private static final String VEDTAK_TOPIC = "teamforeldrepenger.familie-vedtakfattet-v1";

    @Context
    private LocalKafkaProducer localKafkaProducer;

    public YtelsevedtakRestTjeneste() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendYtelsevedtak(YtelsevedtakDto dto) {
        var key = UUID.randomUUID().toString();
        var json = byggYtelseV1Json(dto, key);
        LOG.info("Publiserer {} vedtak for fnr {} ({} - {}, {}%) på topic {}",
                dto.ytelseType(), dto.fnr(), dto.fom(), dto.tom(), dto.utbetalingsgrad(), VEDTAK_TOPIC);
        localKafkaProducer.sendMelding(VEDTAK_TOPIC, key, json);
        return Response.status(Response.Status.CREATED).build();
    }

    private static String byggYtelseV1Json(YtelsevedtakDto dto, String vedtakReferanse) {
        var aktørId = new PersonIdent(dto.fnr()).aktørId();

        var aktør = new Aktør();
        aktør.setVerdi(aktørId);

        var ytelsePeriode = new Periode();
        ytelsePeriode.setFom(dto.fom());
        ytelsePeriode.setTom(dto.tom());

        var anvisningPeriode = new Periode();
        anvisningPeriode.setFom(dto.fom());
        anvisningPeriode.setTom(dto.tom());

        var utbetalingsgrad = new Desimaltall();
        utbetalingsgrad.setVerdi(dto.utbetalingsgrad());

        var anvisning = new Anvisning();
        anvisning.setPeriode(anvisningPeriode);
        anvisning.setUtbetalingsgrad(utbetalingsgrad);

        var kildesystem = mapKildesystem(dto.ytelseType());

        var ytelse = new YtelseV1();
        ytelse.setAktør(aktør);
        ytelse.setVedtattTidspunkt(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        ytelse.setYtelse(mapYtelser(dto.ytelseType()));
        ytelse.setSaksnummer("VTP-" + vedtakReferanse.substring(0, 8).toUpperCase());
        ytelse.setVedtakReferanse(vedtakReferanse);
        ytelse.setYtelseStatus(Status.LØPENDE);
        ytelse.setKildesystem(kildesystem);
        ytelse.setPeriode(ytelsePeriode);
        ytelse.setAnvist(List.of(anvisning));
        ytelse.setTilleggsopplysninger(mapTilleggsopplysninger(dto));

        return DefaultJsonMapper.toJson(ytelse);
    }

    private static String mapTilleggsopplysninger(YtelsevedtakDto dto) {
        return switch (dto.ytelseType()) {
            case PLEIEPENGER_SYKT_BARN -> {
                //Forenkling her, søker har bare ett barn, og vi går ut i fra at det er innleggelse
                var barnAktørId = PersonRepository.hentBarnForPerson(dto.fnr()).aktørId();
                var opplysninger = new PsbTilleggsopplysninger(
                        barnAktørId,
                        List.of(new PsbTilleggsopplysninger.Innleggelsesperiode(dto.fom(), dto.tom()))
                );
                yield DefaultJsonMapper.toJson(opplysninger);
            }
        };
    }

    private static Ytelser mapYtelser(YtelsevedtakDto.YtelseType ytelseType) {
        return switch (ytelseType) {
            case PLEIEPENGER_SYKT_BARN -> Ytelser.PLEIEPENGER_SYKT_BARN;
        };
    }

    private static Kildesystem mapKildesystem(YtelsevedtakDto.YtelseType ytelseType) {
        return switch (ytelseType) {
            case PLEIEPENGER_SYKT_BARN -> Kildesystem.K9SAK;
        };
    }
}
