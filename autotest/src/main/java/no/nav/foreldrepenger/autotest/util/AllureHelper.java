package no.nav.foreldrepenger.autotest.util;

import java.util.List;

import io.qameta.allure.Step;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto.HistorikkInnslag;

public class AllureHelper {

    @Step("Henter aksjonspunkter: {aksjonspunkter}")
    public static void debugListUtAksjonspunkter(String aksjonspunkter){ }

    @Step("Informasjon om behandling: {behandling}")
    public static void debugLoggBehandling(Behandling behandling){ }

    @Step("Informasjon om behandling ({tekst}): {behandling}")
    public static void debugLoggBehandling(String tekst, Behandling behandling){ }

    @Step("Sender inn dokument {type} med innhold: {xml}")
    public static void debugSenderInnDokument(String type, String xml){ }

    @Step("Fritekstlogg {fritekst}")
    public static void debugFritekst(String fritekst){}

    public static void debugLoggHistorikkinnslag(List<HistorikkInnslag> historikkInnslagList) {
        StringBuilder sb = new StringBuilder();
        sb.append("Historikkinnslag\n");
        for(HistorikkInnslag historikkInnslag : historikkInnslagList){
            sb.append(String.format("\t{%s}",historikkInnslag.getTekst()));
        }
        loggHistorikkinnslag(sb.toString());
    }

    @Step("Informasjon om historikkinnslag: {historikkinnslag}")
    private static void loggHistorikkinnslag(String historikkinnslag){}

    @Step("Informasjon om behandlinger: ")
    public static void debugLoggBehandlingsliste(List<Behandling> behandlinger) {
        for(Behandling behandling : behandlinger){
            debugLoggBehandling(behandling);
        }
    }


    @Step("Informasjon om behandlinger: {tekst} ")
    public static void debugLoggBehandlingsliste(String tekst, List<Behandling> behandlinger) {
        for(Behandling behandling : behandlinger){
            debugLoggBehandling(behandling);
        }
    }
}
