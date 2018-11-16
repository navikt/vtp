package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.arbeid.InntektArbeidYtelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.Beregningsgrunnlag;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.Beregningsresultat;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.BeregningsresultatMedUttaksplan;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.medlem.Medlem;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.opptjening.Opptjening;
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
    private UttakResultatPerioder uttakResultatPerioder;
    public Soknad soknad;
    public Familiehendelse familiehendelse;
    public Opptjening opptjening;
    public InntektArbeidYtelse inntektArbeidYtelse;
    public KontrollerFaktaData kontrollerFaktaData;
    public Medlem medlem;
    
    public AvklartData avklartData;

    public List<UttakResultatPeriode> hentUttaksperioder() {
        if (uttakResultatPerioder == null) {
            return Collections.emptyList();
        }
        return uttakResultatPerioder.getPerioder().stream().sorted(Comparator.comparing(UttakResultatPeriode::getFom)).collect(Collectors.toList());
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
        return behandlingsresultat.type.navn;
    }
}
