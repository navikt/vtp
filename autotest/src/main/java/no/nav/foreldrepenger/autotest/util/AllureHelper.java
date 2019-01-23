package no.nav.foreldrepenger.autotest.util;

import java.util.List;

import io.qameta.allure.Step;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto.HistorikkInnslag;

public class AllureHelper {

    @Step("Henter aksjonspunkter: {aksjonspunkter}")
    public static void debugListUtAksjonspunkter(String aksjonspunkter){ }

    @Step("Informasjon om behandling: {behandling}")
    public static void debugListUtBehandling(Behandling behandling){ }

    @Step("Sender inn dokument {} med innhold: {xml}")
    public static void debugSenderInnDokument(String type, String xml){ }

    @Step("Fritekstlogg {fritekst}")
    public static void debugFritekst(String fritekst){}

    public static void debugListHistorikkinnslag(List<HistorikkInnslag> historikkInnslagList) {
        StringBuilder sb = new StringBuilder();
        sb.append("Historikkinnslag\n");
        for(HistorikkInnslag historikkInnslag : historikkInnslagList){
            sb.append(String.format("\t{%s}",historikkInnslag.getTekst()));
        }
        loggHistorikkinnslag(sb.toString());
    }

    @Step("Informasjon om historikkinnslag: {historikkinnslag}")
    private static void loggHistorikkinnslag(String historikkinnslag){}



}
