package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.arbeid.InntektArbeidYtelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.Beregningsresultat;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.BeregningsresultatMedUttaksplan;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag.Beregningsgrunnlag;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.medlem.Medlem;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.opptjening.Opptjening;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.Saldoer;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPeriode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPerioder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;
import no.nav.foreldrepenger.autotest.util.deferred.Deffered;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Behandling {
    
    public int id;
    public int versjon;
    public long fagsakId;
    public Kode type;
    public Kode status;
    
    public LocalDate skjaringstidspunkt;
    public LocalDateTime avsluttet;
    public LocalDate fristBehandlingPaaVent;
    
    public String ansvarligSaksbehandler;
    public String behandlendeEnhet;
    public String behandlendeEnhetNavn;
    public Boolean behandlingHenlagt;
    public Boolean behandlingPaaVent;
    
    private Deffered<List<Vilkar>> vilkar;
    private Deffered<List<Aksjonspunkt>> aksjonspunkter;
    
    private Deffered<Personopplysning> personopplysning;
    private Deffered<Verge> verge;
    private Behandlingsresultat behandlingsresultat;
    private Deffered<Beregningsgrunnlag> beregningsgrunnlag;
    private Deffered<Beregningsresultat> beregningResultatEngangsstonad;
    private Deffered<BeregningsresultatMedUttaksplan> beregningResultatForeldrepenger;
    private Deffered<UttakResultatPerioder> uttakResultatPerioder;
    private Deffered<Soknad> soknad;
    private Deffered<Familiehendelse> familiehendelse;
    private Deffered<Opptjening> opptjening;
    private Deffered<InntektArbeidYtelse> inntektArbeidYtelse;
    private Deffered<KontrollerFaktaData> kontrollerFaktaData;
    private Deffered<Medlem> medlem;
    private Deffered<KlageInfo> klagevurdering;
    private Deffered<Saldoer> saldoer;
    
    public List<UttakResultatPeriode> hentUttaksperioder() {
        return getUttakResultatPerioder().getPerioderForSøker().stream().sorted(Comparator.comparing(UttakResultatPeriode::getFom)).collect(Collectors.toList());
    }
    
    public UttakResultatPeriode hentUttaksperiode(int index) {
        return hentUttaksperioder().get(index);
    }
    
    
    public boolean erSattPåVent() {
        return behandlingPaaVent;
    }
    
    public boolean erHenlagt() {
        return behandlingHenlagt;
    }

    public String hentBehandlingsresultat() {
        return getBehandlingsresultat().type.kode;
    }
    
    public String hentAvslagsarsak() {
        if(null != getBehandlingsresultat() && null != getBehandlingsresultat().avslagsarsak) {
            return getBehandlingsresultat().avslagsarsak.kode;
        }
        return "Ingen avslagsårsak";
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("{Behandlingsid: %s}\n",this.id));
        sb.append(String.format("{Behandlingsstatus: %s}\n", this.status.kode));
        sb.append(String.format("{Behandlingstype: %s}",  this.type.kode));
        if(this.getBehandlingsresultat() != null && this.getBehandlingsresultat().avslagsarsak != null) {
            sb.append(String.format("{Årsak avslag: %s}\n", this.getBehandlingsresultat().avslagsarsak.kode));
        }
        sb.append("Aksjonspunkter:\n");
        if(getAksjonspunkter() != null) {
            for (Aksjonspunkt aksjonspunkt : getAksjonspunkter()) {
                sb.append(String.format("\t{%s}\n", aksjonspunkt.definisjon.kode));
            }
        }
        sb.append("Vilkår:\n");
        if(getVilkar() != null) {
            for (Vilkar vilkar : getVilkar()) {
                sb.append(String.format("\t{%s}\n", vilkar.vilkarType.kode));
            }
        }
        return sb.toString();
    }

    public List<Vilkar> getVilkar() {
        return get(vilkar);
    }

    public void setVilkar(Deffered<List<Vilkar>> dVilkår) {
        this.vilkar = dVilkår;
    }

    public Personopplysning getPersonopplysning() {
        return get(personopplysning);
    }

    public void setPersonopplysning(Deffered<Personopplysning> dPersonopplysninger) {
        this.personopplysning = dPersonopplysninger;
    }

    public Verge getVerge() {
        return get(verge);
    }

    public void setVerge(Deffered<Verge> dVerge) {
        this.verge = dVerge;
    }

    public Behandlingsresultat getBehandlingsresultat() {
        return behandlingsresultat;
    }

    public void setBehandlingsresultat(Behandlingsresultat behandlingsresultat) {
        this.behandlingsresultat = behandlingsresultat;
    }

    public Beregningsgrunnlag getBeregningsgrunnlag() {
        return get(beregningsgrunnlag);
    }

    public void setBeregningsgrunnlag(Deffered<Beregningsgrunnlag> dBeregningsgrunnlag) {
        this.beregningsgrunnlag = dBeregningsgrunnlag;
    }

    public Beregningsresultat getBeregningResultatEngangsstonad() {
        return get(beregningResultatEngangsstonad);
    }

    public void setBeregningResultatEngangsstonad(Deffered<Beregningsresultat> dBeregningsresultat) {
        this.beregningResultatEngangsstonad = dBeregningsresultat;
    }

    public BeregningsresultatMedUttaksplan getBeregningResultatForeldrepenger() {
        return get(beregningResultatForeldrepenger);
    }

    public void setBeregningResultatForeldrepenger(Deffered<BeregningsresultatMedUttaksplan> dBeregningsresultatMedUttaksplan) {
        this.beregningResultatForeldrepenger = dBeregningsresultatMedUttaksplan;
    }

    public UttakResultatPerioder getUttakResultatPerioder() {
        return get(uttakResultatPerioder);
    }

    public void setUttakResultatPerioder(Deffered<UttakResultatPerioder> dUttakResultatPerioder) {
        this.uttakResultatPerioder = dUttakResultatPerioder;
    }

    public Soknad getSoknad() {
        return get(soknad);
    }

    public void setSoknad(Deffered<Soknad> dSoknad) {
        this.soknad = dSoknad;
    }

    public Familiehendelse getFamiliehendelse() {
        return get(familiehendelse);
    }

    public void setFamiliehendelse(Deffered<Familiehendelse> dFamiliehendelse) {
        this.familiehendelse = dFamiliehendelse;
    }

    public Opptjening getOpptjening() {
        return get(opptjening);
    }

    public void setOpptjening(Deffered<Opptjening> dOpptjening) {
        this.opptjening = dOpptjening;
    }

    public InntektArbeidYtelse getInntektArbeidYtelse() {
        return get(inntektArbeidYtelse);
    }

    public void setInntektArbeidYtelse(Deffered<InntektArbeidYtelse> dInntektArbeidYtelse) {
        this.inntektArbeidYtelse = dInntektArbeidYtelse;
    }

    public KontrollerFaktaData getKontrollerFaktaData() {
        return get(kontrollerFaktaData);
    }

    public void setKontrollerFaktaData(Deffered<KontrollerFaktaData> dKontrollerFaktaData) {
        this.kontrollerFaktaData = dKontrollerFaktaData;
    }

    public Medlem getMedlem() {
        return get(medlem);
    }

    public void setMedlem(Deffered<Medlem> dMedlem) {
        this.medlem = dMedlem;
    }

    public KlageInfo getKlagevurdering() {
        return get(klagevurdering);
    }

    public void setKlagevurdering(Deffered<KlageInfo> klagevurdering) {
        this.klagevurdering = klagevurdering;
    }

    public Saldoer getSaldoer() {
        return get(saldoer);
    }

    public void setSaldoer(Deffered<Saldoer> dStonadskontoer) {
        this.saldoer = dStonadskontoer;
    }

    public List<Aksjonspunkt> getAksjonspunkter() {
        return get(aksjonspunkter);
    }

    public void setAksjonspunkter(Deffered<List<Aksjonspunkt>> dAksonspunkter) {
        this.aksjonspunkter = dAksonspunkter;
    }
    
    private static <V> V get(Deffered<V> deferred) {
        try {
            return deferred.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
