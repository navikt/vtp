package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util.DateUtil;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.Utsettelsesaarsaker;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.Uttaksperiodetyper;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.*;

import java.time.LocalDate;

public class FordelingErketyper {

    public static final String STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL = "FORELDREPENGER_FØR_FØDSEL";
    public static final String STØNADSKONTOTYPE_MØDREKVOTE = "MØDREKVOTE";
    public static final String STØNADSKONTOTYPE_FELLESPERIODE = "FELLESPERIODE";
    public static final String STØNADSKONTOTYPE_FORELDREPENGER = "FORELDREPENGER";
    //public static final String STØNADSKONTOTYPE_FEDREKVOTE = "FEDREKVOTE";
    public static final String UTSETTELSETYPE_LOVBESTEMT_FERIE = "LOVBESTEMT_FERIE";
    public static final String UTSETTELSETYPE_ARBEID = "ARBEID";


    public static Fordeling fordelingMorHappyCase(LocalDate familehendelseDato) {
        return generiskFordeling(
                uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, familehendelseDato.minusWeeks(3), familehendelseDato.minusDays(1)),
                uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, familehendelseDato, familehendelseDato.plusWeeks(10)));
    }

    public static Fordeling fordelingMorHappyCaseMedEkstraUttak(LocalDate familiehendelseDato) {
        Fordeling fordeling = new Fordeling();
        fordeling.setAnnenForelderErInformert(true);
        fordeling.getPerioder().add(uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, familiehendelseDato.plusWeeks(10).plusDays(1), familiehendelseDato.plusWeeks(12)));
        return fordeling;
    }

    public static Fordeling fordelingMorMedAksjonspunkt(LocalDate familiehendelseDato) {
        Fordeling fordeling = new Fordeling();
        fordeling.setAnnenForelderErInformert(true);
        fordeling.getPerioder().add(uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, familiehendelseDato.minusWeeks(3), familiehendelseDato.minusDays(1)));
        //fordeling.getPerioder().add(uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, familiehendelseDato))
        return fordeling;
    }

    public static Gradering graderingPeriode(String stønadskontotype, LocalDate fom, LocalDate tom, String arbeidsgiverIdentifikator, double arbeidstidsprosent) {
        Gradering gradering = new Gradering();
        gradering.setArbeidsforholdSomSkalGraderes(true);
        gradering.setVirksomhetsnummer(arbeidsgiverIdentifikator);
        gradering.setArbeidtidProsent(arbeidstidsprosent);
        gradering.setErArbeidstaker(true);
        addStønadskontotype(stønadskontotype, gradering);

        addPeriode(fom, tom, gradering);
        return gradering;
    }

    public static Utsettelsesperiode utsettelsePeriode(String årsak, LocalDate fom, LocalDate tom) {
        Utsettelsesperiode utsettelse = new Utsettelsesperiode();
        Utsettelsesaarsaker årsaker = new Utsettelsesaarsaker();
        årsaker.setKode(årsak);
        utsettelse.setAarsak(årsaker);
        Uttaksperiodetyper uttaksperiodetyper = new Uttaksperiodetyper();
        uttaksperiodetyper.setKode(STØNADSKONTOTYPE_FELLESPERIODE);
        utsettelse.setUtsettelseAv(uttaksperiodetyper);
        utsettelse.setErArbeidstaker(true);
        Uttaksperiodetyper typer = new Uttaksperiodetyper();
        typer.setKode(STØNADSKONTOTYPE_MØDREKVOTE);
        utsettelse.setUtsettelseAv(typer);

        addPeriode(fom, tom, utsettelse);
        return utsettelse;
    }

    public static Uttaksperiode uttaksperiode(String stønadskontotype, LocalDate fom, LocalDate tom) {
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
        return generiskFordeling(uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, familehendelseDato.plusWeeks(3), familehendelseDato.plusWeeks(5)));
    }

    public static Fordeling fordelingFarHappyCaseMedMor(LocalDate familiehendelseDato) {
        return generiskFordeling(uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, familiehendelseDato.plusWeeks(6), familiehendelseDato.plusWeeks(9)));
    }

    public static Fordeling fordelingFarUtenOverlapp(LocalDate familehendelseDato) {
        return generiskFordeling(uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, familehendelseDato.plusWeeks(6).plusDays(1), familehendelseDato.plusWeeks(8)));
    }
    
    public static Fordeling generiskFordeling(Uttaksperiode... perioder) {
        Fordeling fordeling = new Fordeling();
        fordeling.setAnnenForelderErInformert(true);

        for (Uttaksperiode uttaksperiode : perioder) {
            fordeling.getPerioder().add(uttaksperiode);
        }

        return fordeling;
    }

    public static Fordeling fordelingMorAleneomsorgHappyCase(LocalDate familehendelseDato) {
        Fordeling fordeling = new Fordeling();
        fordeling.setAnnenForelderErInformert(false);

        fordeling.getPerioder().add(uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, familehendelseDato.minusWeeks(3), familehendelseDato.minusDays(1)));
        fordeling.getPerioder().add(uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER, familehendelseDato, familehendelseDato.plusWeeks(100)));

        return fordeling;
    }

    public static Fordeling fordelingFarAleneomsorg(LocalDate familehendelseDato) {
        Fordeling fordeling = new Fordeling();
        fordeling.setAnnenForelderErInformert(false);

        fordeling.getPerioder().add(uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER, familehendelseDato, familehendelseDato.plusWeeks(20)));

        return fordeling;
    }
}
