package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.arbeid.Arbeidsforhold;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5080")
public class AvklarArbeidsforholdBekreftelse extends AksjonspunktBekreftelse {

    List<Arbeidsforhold> arbeidsforhold = new ArrayList<>();
    
    public AvklarArbeidsforholdBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        
        arbeidsforhold = behandling.inntektArbeidYtelse.arbeidsforhold;
        
        for (Arbeidsforhold arbeidsforholdBehandling : arbeidsforhold) {
            arbeidsforholdBehandling.brukArbeidsforholdet = true;
        }
    }
    
    public void bekreftArbeidsforholdErRelevant(String navn, boolean fortsettUtenInntekt) {
        Arbeidsforhold forhold = finnArbeidsforhold(navn);
        if(forhold == null) {
            throw new RuntimeException("fant ikke arbeidsforhold: " + navn);
        }
        bekreftArbeidsforholdErRelevant(forhold, fortsettUtenInntekt);
    }
    
    public void bekreftArbeidsforholdErRelevant(Arbeidsforhold forhold, boolean fortsettUtenInntekt) {
        forhold.brukArbeidsforholdet = true;
        forhold.fortsettBehandlingUtenInntektsmelding = fortsettUtenInntekt;
    }
    
    public void bekreftArbeidsforholdErIkkeRelevant(String navn) {
        Arbeidsforhold forhold = finnArbeidsforhold(navn);
        if(forhold == null) {
            throw new RuntimeException("fant ikke arbeidsforhold: " + navn);
        }
        bekreftArbeidsforholdErIkkeRelevant(forhold);
    }
    
    public void bekreftArbeidsforholdErIkkeRelevant(Arbeidsforhold forhold) {
        forhold.brukArbeidsforholdet = false;
    }
    
    private Arbeidsforhold finnArbeidsforhold(String navn) {
        for (Arbeidsforhold arbeidsforhold : this.arbeidsforhold) {
            if(arbeidsforhold.navn.equals(navn)) {
                return arbeidsforhold;
            }
        }
        return null;
    }

}
