package no.nav.foreldrepenger.vtp.server.fagerportal;

import static java.util.Collections.emptyList;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.testmodell.arbeidsgiver.BeskjedModell;
import no.nav.foreldrepenger.vtp.testmodell.arbeidsgiver.OppgaveModell;
import no.nav.foreldrepenger.vtp.testmodell.arbeidsgiver.SakModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.ArbeidsgiverPortalRepository;

@Tag(name = "ArbeidsgiverPorlalMock")
@Path("/api/fager")
public class FagerPortalRestTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(FagerPortalRestTjeneste.class);

    @Context
    private ArbeidsgiverPortalRepository arbeidsgiverPortalRepository;

    @GET
    @Path("/portal")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response portalResponse() {
        var saker = arbeidsgiverPortalRepository.hentSaker();
        LOG.info("Det finnes {} saker i fager-mocken.", saker.size());
        var fagerSaker = saker.stream().map(this::mapTilSakDto).sorted(Comparator.comparing(FagerSak::opprettet).reversed()).toList();
        return fagerPortal(fagerSaker);
    }

    private FagerSak mapTilSakDto(SakModell sak) {
        var oppgaver = arbeidsgiverPortalRepository.hentOppgaveFor(sak.grupperingsid());
        var beskjeder = arbeidsgiverPortalRepository.hentBeskjedFor(sak.grupperingsid());

        return new FagerSak(sak.merkelapp(), sak.virksomhetsnummer(), sak.tittel(), sak.lenke(), sak.status().name(),
                sak.overstyrtStatustekst(), sak.overstyrtTillegsinformasjon(), sak.opprettetTid(), sak.endretTid(),
                (oppgaver != null ? maptilOppgaveDto(List.of(oppgaver)) : emptyList()),
                (beskjeder) != null ? mapTilBeskjederDto(List.of(beskjeder)) : emptyList());
    }

    private List<FagerBeskjed> mapTilBeskjederDto(List<BeskjedModell> beskjeder) {
        return beskjeder.stream()
                .map(beskjed -> new FagerBeskjed(beskjed.tekst(), beskjed.lenke(), beskjed.opprettetTid(), beskjed.endretTid()))
                .sorted(Comparator.comparing(FagerBeskjed::opprettet).reversed())
                .toList();
    }

    private List<FagerOppgave> maptilOppgaveDto(List<OppgaveModell> oppgaver) {
        return oppgaver.stream()
                .map(oppgave -> new FagerOppgave(oppgave.tekst(), oppgave.lenke(), oppgave.tilstand().name(), oppgave.opprettetTid(),
                        oppgave.endretTid()))
                .sorted(Comparator.comparing(FagerOppgave::opprettet).reversed())
                .toList();
    }

    public static Response fagerPortal(List<FagerSak> saker) {
        var htmlSideForInnlogging = String.format("""
                    <!DOCTYPE html>
                    <html>
                    <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                    <title>Arbeidsgiver portal saker:</title>
                    </head>
                        <body>
                        <div style="text-align:left;width:100%%;">
                            <div>
                                <a href="http://localhost:9300/fp-im-dialog/agi?ytelseType=FORELDREPENGER"><h3>AGI Foreldrepenger</h3></a>
                                <a href="http://localhost:9300/fp-im-dialog/agi?ytelseType=SVANGERSKAPSPENGER"><h3>AGI Svangerskapspenger</h3></a>
                            </div>
                            <caption><h1>Arbeidsgiver Portal saker og oppgaver:</h1></caption>
                            <table>
                                <tbody>
                                    %s
                                </tbody>
                            </table>
                        </div>
                    </body>
                    </html>
                """, leggTilRaderITabellMedFagerSaker(saker));
        return Response.ok(htmlSideForInnlogging, MediaType.TEXT_HTML).build();
    }

    private static String leggTilRaderITabellMedFagerSaker(List<FagerSak> saker) {
        return saker.stream().map(FagerPortalRestTjeneste::leggTilRadITabell).collect(Collectors.joining("</br>"));
    }

    private static String leggTilRadITabell(FagerSak sak) {
        return String.format(
                "<tr style=\"border-bottom\"><a href=\"%s\"><h3>%s</h3></a><h4>%s - Status: %s - Overstyrt status: %s</h4>Virksomhet: %s</br>Tilleggsinfo: %s</br>Opprettet: %s</br>Endret: %s<h4>&emsp;Oppgaver:</h4>%s<h4>&emsp;Beskjeder:</h4>%s</tr>",
                sak.lenke(), sak.tittel(), sak.merkelapp(), sak.status(), sak.statusTekst(), sak.orgNr(), sak.tillegsInfo(),
                sak.opprettet(), sak.endret(), leggTilOppgaver(sak.oppgaver()), leggTilBeskjeder(sak.beskjeder()));
    }

    private static String leggTilOppgaver(List<FagerOppgave> oppgaver) {
        return oppgaver.stream().map(FagerPortalRestTjeneste::leggTilOppgaveITabell).collect(Collectors.joining("</br>"));
    }

    private static String leggTilOppgaveITabell(FagerOppgave oppgave) {
        return String.format(
                "<b>&emsp;&emsp;Status: %s - <a href=\"%s\">%s</a></b></br>&emsp;&emsp;Opprettet: %s</br>&emsp;&emsp;Endret: %s",
                oppgave.status(), oppgave.lenke(), oppgave.tekst(), oppgave.opprettet(), oppgave.endret());
    }

    private static String leggTilBeskjeder(List<FagerBeskjed> oppgaver) {
        return oppgaver.stream().map(FagerPortalRestTjeneste::leggTilBeskjedITabell).collect(Collectors.joining("</br>"));
    }

    private static String leggTilBeskjedITabell(FagerBeskjed beskjed) {
        return String.format(
                "<b>&emsp;&emsp;<a href=\"%s\">%s</a></b></br>&emsp;&emsp;Opprettet: %s</br>&emsp;&emsp;Endret: %s",
                beskjed.lenke(), beskjed.tekst(), beskjed.opprettet(), beskjed.endret());
    }

    public record FagerSak(String merkelapp, String orgNr, String tittel, String lenke, String status, String statusTekst,
                           String tillegsInfo, LocalDateTime opprettet, LocalDateTime endret, List<FagerOppgave> oppgaver,
                           List<FagerBeskjed> beskjeder) {
    }

    public record FagerOppgave(String tekst, String lenke, String status, LocalDateTime opprettet, LocalDateTime endret) {
    }

    public record FagerBeskjed(String tekst, String lenke, LocalDateTime opprettet, LocalDateTime endret) {
    }
}
