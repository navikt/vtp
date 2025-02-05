package no.nav.foreldrepenger.vtp.server.fagerportal;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.vtp.testmodell.arbeidsgiver.OppgaveModell;

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
        var fagerSaker = saker.stream()
                .map(this::mapTilSakDto)
                .sorted(Comparator.comparing(FagerSak::opprettet).reversed())
                .toList();
        return fagerPortal(fagerSaker);
    }

    private FagerSak mapTilSakDto(SakModell saker) {
        var oppgaver = arbeidsgiverPortalRepository.hentOppgaveFor(saker.grupperingsid());
        return new FagerSak(saker.merkelapp(), saker.virksomhetsnummer(), saker.tittel(), saker.lenke(), saker.status().name(),
                saker.overstyrtStatustekst(), saker.overstyrtTillegsinformasjon(), saker.opprettetTid(), saker.endretTid(),
                maptilOppgaveDto(List.of(oppgaver)));
    }

    private List<FagerOppgave> maptilOppgaveDto(List<OppgaveModell> oppgaver) {
        return oppgaver.stream()
                .map(oppgave -> new FagerOppgave(oppgave.tekst(), oppgave.lenke(), oppgave.tilstand().name(), oppgave.opprettetTid(), oppgave.endretTid()))
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
                                <a href="http://localhost:9300/fp-im-dialog/opprett?ytelseType=FORELDREPENGER"><h3>AGI Foreldrepenger</h3></a>
                                <a href="http://localhost:9300/fp-im-dialog/opprett?ytelseType=SVANGERSKAPSPENGER"><h3>AGI Svangerskapspenger</h3></a>
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
                "<tr style=\"border-bottom\"><a href=\"%s\"><h3>%s</h3></a><h4>%s - Status: %s - Overstyrt status: %s</h4>Virksomhet: %s</br>Tilleggsinfo: %s</br>Opprettet: %s</br>Endret: %s<h4>&emsp;Oppgaver:</h4>%s</tr>",
                sak.lenke(), sak.tittel(), sak.merkelapp(), sak.status(), sak.statusTekst(), sak.orgNr(), sak.tillegsInfo(),
                sak.opprettet(), sak.endret(), leggTilOppgaver(sak.oppgaver));
    }

    private static String leggTilOppgaver(List<FagerOppgave> oppgaver) {
        return oppgaver.stream().map(FagerPortalRestTjeneste::leggTilOppgaveITabell).collect(Collectors.joining("</br>"));
    }

    private static String leggTilOppgaveITabell(FagerOppgave oppgave) {
        return String.format("<b>&emsp;&emsp;Status: %s - <a href=\"%s\">%s</a></b></br>&emsp;&emsp;Opprettet: %s</br>&emsp;&emsp;Endret: %s", oppgave.status(), oppgave.lenke(), oppgave.tekst(), oppgave.opprettet(), oppgave.endret());
    }

    public record FagerSak(String merkelapp, String orgNr, String tittel, String lenke, String status, String statusTekst,
                           String tillegsInfo, LocalDateTime opprettet, LocalDateTime endret, List<FagerOppgave> oppgaver) {
    }

    public record FagerOppgave(String tekst, String lenke, String status, LocalDateTime opprettet, LocalDateTime endret) {
    }
}
