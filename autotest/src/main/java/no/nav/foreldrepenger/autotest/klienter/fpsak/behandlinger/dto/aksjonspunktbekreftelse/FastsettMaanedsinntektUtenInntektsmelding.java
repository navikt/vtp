package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.util.List;

class FastsettMaanedsinntektUtenInntektsmelding {


    protected List<FastsettMaanedsinntektUtenInntektsmeldingAndel> andelListe;

    public FastsettMaanedsinntektUtenInntektsmelding(List<FastsettMaanedsinntektUtenInntektsmeldingAndel> andelListe) {
        this.andelListe = andelListe;
    }

    public List<FastsettMaanedsinntektUtenInntektsmeldingAndel> getAndelListe() {
        return andelListe;
    }
}
