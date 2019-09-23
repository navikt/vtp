package no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper;

import java.time.LocalDate;

import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.SøkersRolle;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders.GraderingBuilder;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders.UttaksperiodeBuilder;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v3.Oppholdsaarsaker;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v3.Utsettelsesaarsaker;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v3.Uttaksperiodetyper;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Fordeling;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Gradering;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.LukketPeriodeMedVedlegg;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Oppholdsperiode;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Utsettelsesperiode;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Uttaksperiode;

public class FordelingErketyper {

    public static final String STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL = "FORELDREPENGER_FØR_FØDSEL";
    public static final String STØNADSKONTOTYPE_MØDREKVOTE = "MØDREKVOTE";
    public static final String STØNADSKONTOTYPE_FEDREKVOTE = "FEDREKVOTE";
    public static final String STØNADSKONTOTYPE_FELLESPERIODE = "FELLESPERIODE";
    public static final String STØNADSKONTOTYPE_FORELDREPENGER = "FORELDREPENGER";
    public static final String UTSETTELSETYPE_LOVBESTEMT_FERIE = "LOVBESTEMT_FERIE";
    public static final String UTSETTELSETYPE_ARBEID = "ARBEID";
    public static final String UTSETTELSETYPE_SYKDOMSKADE = "SYKDOM";
    public static final String UTSETTELSETYPE_INNLAGTSØKER = "INSTITUSJONSOPPHOLD_SØKER";
    public static final String UTSETTELSETYPE_INNLAGTBARN = "INSTITUSJONSOPPHOLD_BARNET";
    public static final String UTSETTELSETYPE_UDEFINERT = "-";
    public static final String OPPHOLDSPTYPE_UDEFINERT = "-";
    public static final String OPPHOLDSTYPE_INGEN = "INGEN";
    public static final String OPPHOLDSTYPE_MØDREKVOTE_ANNEN_FORELDER = "UTTAK_MØDREKVOTE_ANNEN_FORELDER";
    public static final String OPPHOLDSTYPE_FEDREKVOTE_ANNEN_FORELDER = "UTTAK_FEDREKVOTE_ANNEN_FORELDER";
    public static final String OPPHOLDSTYPE_KVOTE_FELLESPERIODE_ANNEN_FORELDER = "UTTAK_FELLESP_ANNEN_FORELDER";
    public static final String OPPHOLDSTYPE_KVOTE_FORELDREPENGER_ANNEN_FORELDER = "UTTAK_FORELDREPENGER_ANNEN_FORELDER";


    public static Fordeling fordelingHappyCase(LocalDate familehendelseDato, SøkersRolle søkerRolle){
        if(søkerRolle == SøkersRolle.MOR){
            return fordelingMorHappyCaseLong(familehendelseDato);
        }
        else {
            return fordelingFarHappyCase(familehendelseDato);
        }
    }
    public static Fordeling fordelingMorHappyCase(LocalDate familehendelseDato) {
        return generiskFordeling(
                uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, familehendelseDato.minusWeeks(3), familehendelseDato.minusDays(1)),
                uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, familehendelseDato, familehendelseDato.plusWeeks(10)));
    }

    public static Fordeling fordelingMorHappyCaseLong(LocalDate familehendelseDato) {
        return generiskFordeling(
                uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, familehendelseDato.minusWeeks(3), familehendelseDato.minusDays(1)),
                uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, familehendelseDato, familehendelseDato.plusWeeks(15).minusDays(1)),
                uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, familehendelseDato.plusWeeks(15), familehendelseDato.plusWeeks(31).minusDays(1)));

    }

    public static Fordeling fordelingMorHappyCaseEkstraUttakFørFødsel(LocalDate familehendelseDato) {
        return generiskFordeling(
                uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, familehendelseDato.minusWeeks(12), familehendelseDato.minusDays(1)),
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
        return fordeling;
    }

    public static Gradering graderingsperiodeArbeidstaker(String stønadskontotype, LocalDate fom, LocalDate tom, String arbeidsgiverIdentifikator, Integer arbeidstidsprosent) {
        Gradering graderinsperiode = new GraderingBuilder()
                .medStønadskontoType(stønadskontotype)
                .medTidsperiode(fom, tom)
                .medGraderingArbeidstaker(arbeidsgiverIdentifikator, arbeidstidsprosent)
                .build();
        return graderinsperiode;
    }
    public static Gradering graderingsperiodeFL(String stønadskontotype, LocalDate fom, LocalDate tom, Integer arbeidstidsprosent) {
        Gradering graderinsperiode = new GraderingBuilder()
                .medStønadskontoType(stønadskontotype)
                .medTidsperiode(fom, tom)
                .medGraderingFL(arbeidstidsprosent)
                .build();
        return graderinsperiode;
    }
    public static Gradering graderingsperiodeSN(String stønadskontotype, LocalDate fom, LocalDate tom, Integer arbeidstidsprosent) {
        Gradering graderinsperiode = new GraderingBuilder()
                .medStønadskontoType(stønadskontotype)
                .medTidsperiode(fom, tom)
                .medGraderingSN(arbeidstidsprosent)
                .build();
        return graderinsperiode;
    }

    public static Utsettelsesperiode utsettelsesperiode(String årsak, LocalDate fom, LocalDate tom) {
        Utsettelsesperiode utsettelsesperiode = new Utsettelsesperiode();
        utsettelsesperiode.setFom(fom);
        utsettelsesperiode.setTom(tom);
        Utsettelsesaarsaker årsaker = new Utsettelsesaarsaker();
        årsaker.setKode(årsak);
        utsettelsesperiode.setAarsak(årsaker);

        return utsettelsesperiode;
    }

    public static Uttaksperiode uttaksperiode(String stønadskontotype, LocalDate fom, LocalDate tom) {
        return (new UttaksperiodeBuilder()
                .medTidsperiode(fom, tom)
                .medStønadskontoType(stønadskontotype)
                .build());
    }

    public static Oppholdsperiode oppholdsperiode(String oppholdsårsak, LocalDate fom, LocalDate tom) {
        Oppholdsperiode oppholdsperiode = new Oppholdsperiode();
        Oppholdsaarsaker oppholdsaarsaker = new Oppholdsaarsaker();
        oppholdsaarsaker.setKode(oppholdsårsak);
        oppholdsperiode.setAarsak(oppholdsaarsaker);
        addPeriode(fom, tom, oppholdsperiode);
        return oppholdsperiode;

    }

    public static void addPeriode(LocalDate fom, LocalDate tom, LukketPeriodeMedVedlegg uttaksperiode) {
        uttaksperiode.setFom((fom));
        uttaksperiode.setTom((tom));
    }

    public static void addStønadskontotype(String stønadskontotype, Uttaksperiode uttaksperiode) {
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

    public static Fordeling fordelingFarHappycaseKobletMedMorHappycase(LocalDate familehendelseDato) {
        return generiskFordeling(uttaksperiode(STØNADSKONTOTYPE_FEDREKVOTE, familehendelseDato.plusWeeks(10).plusDays(1), familehendelseDato.plusWeeks(16)));
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
