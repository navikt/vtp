package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.xml.datatype.DatatypeConfigurationException;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util.DateUtil;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.Utsettelsesaarsaker;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.Uttaksperiodetyper;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Fordeling;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Gradering;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.LukketPeriodeMedVedlegg;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Utsettelsesperiode;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Uttaksperiode;

public class FordelingErketyper {

    public static final String STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL = "FORELDREPENGER_FØR_FØDSEL";
    public static final String STØNADSKONTOTYPE_MØDREKVOTE = "MØDREKVOTE";
    public static final String STØNADSKONTOTYPE_FELLESPERIODE = "FELLESPERIODE";

    public static Fordeling fordelingMorHappyCase(LocalDate familehendelseDato) {
        Fordeling fordeling = new Fordeling();
        fordeling.setAnnenForelderErInformert(true);

        fordeling.getPerioder().add(uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, familehendelseDato.minusWeeks(3), familehendelseDato.minusDays(1)));
        fordeling.getPerioder().add(uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, familehendelseDato, familehendelseDato.plusWeeks(10)));

        return fordeling;
    }

    public static Fordeling fordelingMorHappyCaseMedUtsettelseOgGradering(LocalDate familehendelseDato, String arbeidsgiverIdentifikator) {
        Fordeling fordeling = fordelingMorHappyCase(familehendelseDato);
        fordeling.getPerioder().add(utsettelsePeriode(familehendelseDato.plusWeeks(10).plusDays(1), familehendelseDato.plusWeeks(10)));
        fordeling.getPerioder().add(graderingPeriode(STØNADSKONTOTYPE_FELLESPERIODE, familehendelseDato.plusWeeks(10).plusDays(1), familehendelseDato.plusWeeks(13), arbeidsgiverIdentifikator));
        return fordeling;
    }

    private static Gradering graderingPeriode(String stønadskontotype, LocalDate fom, LocalDate tom, String arbeidsgiverIdentifikator) {
        Gradering gradering = new Gradering();
        gradering.setArbeidsforholdSomSkalGraderes(true);
        gradering.setVirksomhetsnummer(arbeidsgiverIdentifikator);
        gradering.setArbeidtidProsent(BigDecimal.TEN.doubleValue());
        gradering.setErArbeidstaker(true);
        addStønadskontotype(stønadskontotype, gradering);

        addPeriode(fom, tom, gradering);
        return gradering;
    }

    private static Utsettelsesperiode utsettelsePeriode(LocalDate fom, LocalDate tom) {
        Utsettelsesperiode utsettelse = new Utsettelsesperiode();
        Utsettelsesaarsaker årsaker = new Utsettelsesaarsaker();
        årsaker.setKode("FERIE");
        utsettelse.setAarsak(årsaker);
        utsettelse.setErArbeidstaker(true);

        addPeriode(fom, tom, utsettelse);
        return utsettelse;
    }

    private static Uttaksperiode uttaksperiode(String stønadskontotype, LocalDate fom, LocalDate tom) {
        Uttaksperiode uttaksperiode = new Uttaksperiode();
        addStønadskontotype(stønadskontotype, uttaksperiode);

        addPeriode(fom, tom, uttaksperiode);
        return uttaksperiode;
    }

    private static void addPeriode(LocalDate fom, LocalDate tom, LukketPeriodeMedVedlegg uttaksperiode) {
        uttaksperiode.setFom(DateUtil.convertToXMLGregorianCalendar(fom));
        uttaksperiode.setTom(DateUtil.convertToXMLGregorianCalendar(tom));
    }

    private static void addStønadskontotype(String stønadskontotype, Uttaksperiode uttaksperiode) {
        Uttaksperiodetyper uttaksperiodetyper = new Uttaksperiodetyper();
        uttaksperiodetyper.setKode(stønadskontotype);
        uttaksperiode.setType(uttaksperiodetyper);
    }

    public static Fordeling fordelingFarHappyCase(LocalDate familehendelseDato) {
        Fordeling fordeling = new Fordeling();
        fordeling.setAnnenForelderErInformert(true);

        fordeling.getPerioder().add(uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, familehendelseDato.plusWeeks(3), familehendelseDato.plusWeeks(5)));

        return fordeling;
    }
}
