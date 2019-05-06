package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
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
    
    public List<Vilkar> vilkar;
    public List<Aksjonspunkt> aksjonspunkter;
    
    public Personopplysning personopplysning;
    public Verge verge;
    public Behandlingsresultat behandlingsresultat;
    public Beregningsgrunnlag beregningsgrunnlag;
    public Beregningsresultat beregningResultatEngangsstonad;
    public BeregningsresultatMedUttaksplan beregningResultatForeldrepenger;
    public UttakResultatPerioder uttakResultatPerioder;
    public Soknad soknad;
    public Familiehendelse familiehendelse;
    public Opptjening opptjening;
    public InntektArbeidYtelse inntektArbeidYtelse;
    public KontrollerFaktaData kontrollerFaktaData;
    public Medlem medlem;
    public KlageInfo klagevurdering;
    public Saldoer saldoer;
    
    public AvklartDataFodsel avklartDataFodsel;

    public List<UttakResultatPeriode> hentUttaksperioder() {
        return uttakResultatPerioder.getPerioderForSøker().stream().sorted(Comparator.comparing(UttakResultatPeriode::getFom)).collect(Collectors.toList());
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
        return behandlingsresultat.type.kode;
    }
    
    public String hentAvslagsarsak() {
        if(null != behandlingsresultat && null != behandlingsresultat.avslagsarsak) {
            return behandlingsresultat.avslagsarsak.kode;
        }
        return "Ingen avslagsårsak";
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("{Behandlingsid: %s}\n",this.id));
        sb.append(String.format("{Behandlingsstatus: %s}\n", this.status.kode));
        sb.append(String.format("{Behandlingstype: %s}",  this.type.kode));
        if(this.behandlingsresultat != null && this.behandlingsresultat.avslagsarsak != null) {
            sb.append(String.format("{Årsak avslag: %s}\n", this.behandlingsresultat.avslagsarsak.kode));
        }
        sb.append("Aksjonspunkter:\n");
        if(aksjonspunkter != null) {
            for (Aksjonspunkt aksjonspunkt : aksjonspunkter) {
                sb.append(String.format("\t{%s}\n", aksjonspunkt.definisjon.kode));
            }
        }
        sb.append("Vilkår:\n");
        if(vilkar != null) {
            for (Vilkar vilkar : vilkar) {
                sb.append(String.format("\t{%s}\n", vilkar.vilkarType.kode));
            }
        }
        return sb.toString();
    }

    public List<Vilkar> getVilkar() {
        return vilkar;
    }

    public void setVilkar(List<Vilkar> vilkar) {
        this.vilkar = vilkar;
    }
}
