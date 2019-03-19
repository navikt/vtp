package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import java.time.LocalDate;

import no.nav.vedtak.felles.xml.soeknad.endringssoeknad.v3.Endringssoeknad;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.AnnenForelder;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.AnnenForelderMedNorskIdent;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.Rettigheter;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.SoekersRelasjonTilBarnet;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Dekningsgrad;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Foreldrepenger;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v3.Dekningsgrader;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Fordeling;

public class ForeldrepengeYtelseErketyper {

    //todo builder?
    public static Foreldrepenger foreldrepengeYtelseNorskBorgerINorgeTerminMor(LocalDate termindato) {
        return foreldrepengeYtelseNorskBorgerINorgeTerminMor(termindato, FordelingErketyper.fordelingMorHappyCase(termindato));
    }

    public static Foreldrepenger foreldrepengeYtelseNorskBorgerINorgeTerminMorEkstraUttakFørFødsel(LocalDate termindato) {
        return foreldrepengeYtelseNorskBorgerINorgeTerminMor(termindato, FordelingErketyper.fordelingMorHappyCaseEkstraUttakFørFødsel(termindato));
    }

    //todo builder?
    public static Foreldrepenger foreldrepengeYtelseNorskBorgerINorgeTerminMor(LocalDate termindato, Fordeling value) {
        Foreldrepenger foreldrepenger = new Foreldrepenger();
        foreldrepenger.setDekningsgrad(standardDekningsgrader());
        foreldrepenger.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        foreldrepenger.setRettigheter(RettigheterErketyper.beggeForeldreRettIkkeAleneomsorg());
        foreldrepenger.setRelasjonTilBarnet(SoekersRelasjonErketyper.søkerTermin(termindato));
        foreldrepenger.setFordeling(value);
        return foreldrepenger;
    }

    public static Foreldrepenger foreldrepengeYtelseNorskBorgerINorgeTerminFar(LocalDate termindato) {
        Foreldrepenger foreldrepenger = new Foreldrepenger();
        foreldrepenger.setDekningsgrad(standardDekningsgrader());
        foreldrepenger.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        foreldrepenger.setRettigheter(RettigheterErketyper.beggeForeldreRettIkkeAleneomsorg());
        foreldrepenger.setRelasjonTilBarnet(SoekersRelasjonErketyper.søkerTermin(termindato));
        foreldrepenger.setFordeling(FordelingErketyper.fordelingFarHappyCase(termindato));
        return foreldrepenger;
    }


    public static Foreldrepenger foreldrepengerYtelseNorskBorgerINorgeFødselMor(LocalDate fødselsdato, int antallBarn) {
        return foreldrepengerYtelseNorskBorgerINorgeFødselMor(FordelingErketyper.fordelingMorHappyCase(fødselsdato), fødselsdato, antallBarn);
    }
    
    public static Foreldrepenger foreldrepengerYtelseNorskBorgerINorgeFødselMor(LocalDate fødselsdato) {
        return foreldrepengerYtelseNorskBorgerINorgeFødselMor(FordelingErketyper.fordelingMorHappyCase(fødselsdato), fødselsdato);
    }

    public static Foreldrepenger foreldrepengerYtelseNorskBorger(Rettigheter rettigheter, SoekersRelasjonTilBarnet relasjonTilBarnet,
                                                                 Fordeling fordeling, AnnenForelder annenForelder) {
        Foreldrepenger foreldrepenger = new Foreldrepenger();
        foreldrepenger.setDekningsgrad(standardDekningsgrader());
        foreldrepenger.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        foreldrepenger.setRettigheter(rettigheter);
        foreldrepenger.setRelasjonTilBarnet(relasjonTilBarnet);
        foreldrepenger.setFordeling(fordeling);
        foreldrepenger.setAnnenForelder(annenForelder);
        return foreldrepenger;
    }

    public static Foreldrepenger foreldrepengerYtelseNorskBorgerINorgeFødselMor(Fordeling fordeling, LocalDate fødselsdato) {
        return foreldrepengerYtelseNorskBorgerINorgeFødselMor(fordeling, fødselsdato, 1);
    }
    
    public static Foreldrepenger foreldrepengerYtelseNorskBorgerINorgeFødselMor(Fordeling fordeling, LocalDate fødselsdato, int antallBarn) {
        Foreldrepenger foreldrepenger = new Foreldrepenger();
        foreldrepenger.setDekningsgrad(standardDekningsgrader());
        foreldrepenger.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        foreldrepenger.setRettigheter(RettigheterErketyper.beggeForeldreRettIkkeAleneomsorg());
        foreldrepenger.setRelasjonTilBarnet(SoekersRelasjonErketyper.fødsel(antallBarn, fødselsdato));
        foreldrepenger.setFordeling(fordeling);
        return foreldrepenger;
    }

    public static Foreldrepenger foreldrepengerYtelseNorskBorgerINorgeFødselMorMedFar(LocalDate fødselsdato, String aktørIdFar, Fordeling fordeling) {
        Foreldrepenger foreldrepenger = new Foreldrepenger();
        foreldrepenger.setDekningsgrad(standardDekningsgrader());
        foreldrepenger.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        foreldrepenger.setRettigheter(RettigheterErketyper.beggeForeldreRettIkkeAleneomsorg());
        foreldrepenger.setRelasjonTilBarnet(SoekersRelasjonErketyper.fødsel(1, fødselsdato));
        foreldrepenger.setFordeling(fordeling);
        foreldrepenger.setAnnenForelder(standardAnnenForelder(aktørIdFar));
        return foreldrepenger;
    }

    public static Endringssoeknad endringssoeknadForeldrepengerYtelseNorskBorgerINorgeFødselMor(LocalDate startDatoForeldrepenger, String saksnummer) {
        LocalDate  fødselsdato = startDatoForeldrepenger.plusWeeks(3);
        Endringssoeknad endringssoeknad = new Endringssoeknad();
        endringssoeknad.setSaksnummer(saksnummer);
        endringssoeknad.setFordeling(FordelingErketyper.fordelingMorHappyCaseMedEkstraUttak(fødselsdato));
        return endringssoeknad;
    }

    public static Endringssoeknad endringssoeknadYtelse(Fordeling fordeling, String saksnummer) {
        Endringssoeknad endringssoeknad = new Endringssoeknad();
        endringssoeknad.setSaksnummer(saksnummer);
        endringssoeknad.setFordeling(fordeling);
        return endringssoeknad;
    }

    public static Foreldrepenger foreldrepengerYtelseNorskBorgerINorgeFødselFar(LocalDate fødselsdato) {
        Foreldrepenger foreldrepenger = new Foreldrepenger();
        foreldrepenger.setDekningsgrad(standardDekningsgrader());
        foreldrepenger.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        foreldrepenger.setRettigheter(RettigheterErketyper.beggeForeldreRettIkkeAleneomsorg());
        foreldrepenger.setRelasjonTilBarnet(SoekersRelasjonErketyper.fødsel(1, fødselsdato));
        foreldrepenger.setFordeling(FordelingErketyper.fordelingFarHappyCase(fødselsdato));
        return foreldrepenger;
    }

    public static Foreldrepenger foreldrepengerYtelseNorskBorgerINorgeFødselFarMedMor(String aktørIdMor, LocalDate fødselsdato, Fordeling fordeling) {
        Foreldrepenger foreldrepenger = new Foreldrepenger();
        foreldrepenger.setDekningsgrad(standardDekningsgrader());
        foreldrepenger.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        foreldrepenger.setRettigheter(RettigheterErketyper.beggeForeldreRettIkkeAleneomsorg());
        foreldrepenger.setRelasjonTilBarnet(SoekersRelasjonErketyper.fødsel(1, fødselsdato));
        foreldrepenger.setFordeling(fordeling);
        foreldrepenger.setAnnenForelder(standardAnnenForelder(aktørIdMor));

        return foreldrepenger;
    }

    public static Foreldrepenger foreldrepengeYtelseNorskBorgerINorgeFødselMorAleneomsorg(LocalDate fødselsdato) {
        Foreldrepenger foreldrepenger = new Foreldrepenger();
        foreldrepenger.setDekningsgrad(standardDekningsgrader());
        foreldrepenger.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        foreldrepenger.setRettigheter(RettigheterErketyper.harAleneOmsorgOgEnerett());
        foreldrepenger.setRelasjonTilBarnet(SoekersRelasjonErketyper.fødsel(1, fødselsdato));
        foreldrepenger.setFordeling(FordelingErketyper.fordelingMorAleneomsorgHappyCase(fødselsdato));
        return foreldrepenger;
    }

    public static Foreldrepenger foreldrepengeYtelseNorskBorgerINorgeFødselFarAleneomsorg(LocalDate fødselsdato) {
        Foreldrepenger foreldrepenger = new Foreldrepenger();
        foreldrepenger.setDekningsgrad(standardDekningsgrader());
        foreldrepenger.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        foreldrepenger.setRettigheter(RettigheterErketyper.harAleneOmsorgOgEnerett());
        foreldrepenger.setRelasjonTilBarnet(SoekersRelasjonErketyper.fødsel(1, fødselsdato));
        foreldrepenger.setFordeling(FordelingErketyper.fordelingFarAleneomsorg(fødselsdato));
        return foreldrepenger;
    }

    public static Foreldrepenger foreldrepengeYtelseNorskBorgerINorgeFodselMedFrilans(LocalDate fodselsdato) {
        Foreldrepenger foreldrepenger = foreldrepengerYtelseNorskBorgerINorgeFødselMor(fodselsdato);
        foreldrepenger.setOpptjening(OpptjeningErketyper.medFrilansOpptjening());
        return foreldrepenger;
    }
    public static Foreldrepenger foreldrepengeYtelseNorskBorgerINorgeFodselMedFrilans(Fordeling fordeling,LocalDate fodselsdato) {
        Foreldrepenger foreldrepenger = foreldrepengerYtelseNorskBorgerINorgeFødselMor(fordeling,fodselsdato);
        foreldrepenger.setOpptjening(OpptjeningErketyper.medFrilansOpptjening());
        return foreldrepenger;
    }

    public static AnnenForelder standardAnnenForelder(String aktørId) {
        AnnenForelderMedNorskIdent forelder = new AnnenForelderMedNorskIdent();
        forelder.setAktoerId(aktørId);
        return forelder;
    }

    public static Foreldrepenger foreldrepengerYtelseNorskBorgerINorgeFødselMorMedEgenNaering(LocalDate fodselsdato) {
        Foreldrepenger foreldrepenger = foreldrepengerYtelseNorskBorgerINorgeFødselMor(fodselsdato);
        foreldrepenger.setOpptjening(OpptjeningErketyper.medEgenNaeringOpptjening());
        return foreldrepenger;
    }


    public static Foreldrepenger foreldrepengerYtelseNorskBorgerINorgeFødselMorVentelonnVartpenger(LocalDate fodselsdato) {
        Foreldrepenger foreldrepenger = foreldrepengerYtelseNorskBorgerINorgeFødselMor(fodselsdato);
        foreldrepenger.setOpptjening(OpptjeningErketyper.medEgenNaeringOpptjening());
        foreldrepenger.setOpptjening(OpptjeningErketyper.medVentelonnVartpengerOpptjening());
        return foreldrepenger;
    }

    public static Dekningsgrad standardDekningsgrader(){
        Dekningsgrad dekningsgrad = new Dekningsgrad();
        Dekningsgrader dekningsgrader = new Dekningsgrader();
        dekningsgrader.setKode("100");
        dekningsgrader.setKodeverk("IKKE_DEFINERT");
        dekningsgrad.setDekningsgrad(dekningsgrader);

        return dekningsgrad;
    }

}
