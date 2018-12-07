package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import java.time.LocalDate;

import no.nav.vedtak.felles.xml.soeknad.felles.v1.AnnenForelder;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.AnnenForelderMedNorskIdent;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Dekningsgrad;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Foreldrepenger;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.Dekningsgrader;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Fordeling;

public class ForeldrepengeYtelseErketyper {

    //todo builder?
    public static Foreldrepenger foreldrepengeYtelseNorskBorgerINorgeTerminMor(LocalDate termindato) {
        Foreldrepenger foreldrepenger = new Foreldrepenger();
        foreldrepenger.setDekningsgrad(standardDekningsgrader());
        foreldrepenger.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        foreldrepenger.setRettigheter(RettigheterErketyper.beggeForeldreRettIkkeAleneomsorg());
        foreldrepenger.setRelasjonTilBarnet(SoekersRelasjonErketyper.søkerTermin(termindato));
        foreldrepenger.setFordeling(FordelingErketyper.fordelingMorHappyCase(termindato));
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

    public static Foreldrepenger foreldrepengerYtelseNorskBorgerINorgeFødselMor(LocalDate startDatoForeldrepenger) {
        LocalDate fødselsdato = startDatoForeldrepenger.plusWeeks(3);
        return foreldrepengerYtelseNorskBorgerINorgeFødselMor(FordelingErketyper.fordelingMorHappyCase(fødselsdato), fødselsdato);
    }

    public static Foreldrepenger foreldrepengerYtelseNorskBorgerINorgeFødselMor(Fordeling fordeling, LocalDate fødselsdato) {
        Foreldrepenger foreldrepenger = new Foreldrepenger();
        foreldrepenger.setDekningsgrad(standardDekningsgrader());
        foreldrepenger.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        foreldrepenger.setRettigheter(RettigheterErketyper.beggeForeldreRettIkkeAleneomsorg());
        foreldrepenger.setRelasjonTilBarnet(SoekersRelasjonErketyper.fødsel(1, fødselsdato));
        foreldrepenger.setFordeling(fordeling);
        return foreldrepenger;
    }
    
    public static Foreldrepenger foreldrepengerYtelseNorskBorgerINorgeFødselMorMedFar(LocalDate startDatoForeldrepenger, String aktørIdFar) {
        LocalDate fødselsdato = startDatoForeldrepenger.plusWeeks(3);
        Foreldrepenger foreldrepenger = new Foreldrepenger();
        foreldrepenger.setDekningsgrad(standardDekningsgrader());
        foreldrepenger.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        foreldrepenger.setRettigheter(RettigheterErketyper.beggeForeldreRettIkkeAleneomsorg());
        foreldrepenger.setRelasjonTilBarnet(SoekersRelasjonErketyper.fødsel(1, fødselsdato));
        foreldrepenger.setFordeling(FordelingErketyper.fordelingMorHappyCase(fødselsdato));
        return foreldrepenger;
    }
    
    public static Foreldrepenger foreldrepengerYtelseNorskBorgerINorgeFødselFar(LocalDate startDatoForeldrepenger) {
        LocalDate fødselsdato = startDatoForeldrepenger.minusWeeks(3);
        Foreldrepenger foreldrepenger = new Foreldrepenger();
        foreldrepenger.setDekningsgrad(standardDekningsgrader());
        foreldrepenger.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        foreldrepenger.setRettigheter(RettigheterErketyper.beggeForeldreRettIkkeAleneomsorg());
        foreldrepenger.setRelasjonTilBarnet(SoekersRelasjonErketyper.fødsel(1, fødselsdato));
        foreldrepenger.setFordeling(FordelingErketyper.fordelingFarHappyCase(fødselsdato));
        return foreldrepenger;
    }
    
    public static Foreldrepenger foreldrepengerYtelseNorskBorgerINorgeFødselFarMedMor(LocalDate startDatoForeldrepenger, String aktørIdMor) {
        LocalDate fødselsdato = startDatoForeldrepenger.minusWeeks(3);
        Foreldrepenger foreldrepenger = new Foreldrepenger();
        foreldrepenger.setDekningsgrad(standardDekningsgrader());
        foreldrepenger.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        foreldrepenger.setRettigheter(RettigheterErketyper.beggeForeldreRettIkkeAleneomsorg());
        foreldrepenger.setRelasjonTilBarnet(SoekersRelasjonErketyper.fødsel(1, fødselsdato));
        foreldrepenger.setFordeling(FordelingErketyper.fordelingFarHappyCase(fødselsdato));
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

    public static Foreldrepenger foreldrepengeYtelseNorskBorgerINorgeTerminMedFrilans() {
        Foreldrepenger foreldrepenger = foreldrepengeYtelseNorskBorgerINorgeTerminMor(LocalDate.now().plusWeeks(3));
        foreldrepenger.setOpptjening(OpptjeningErketyper.medFrilansOpptjening());
        return foreldrepenger;
    }
    
    public static AnnenForelder standardAnnenForelder(String aktørId) {
        AnnenForelderMedNorskIdent forelder = new AnnenForelderMedNorskIdent();
        forelder.setAktoerId(aktørId);
        return forelder;
    }

    public static Foreldrepenger foreldrepengerYtelseNorskBorgerINorgeFødselMorMedEgenNaering(LocalDate startDatoForeldrepenger) {
        Foreldrepenger foreldrepenger = foreldrepengerYtelseNorskBorgerINorgeFødselMor(startDatoForeldrepenger);
        foreldrepenger.setOpptjening(OpptjeningErketyper.medEgenNaeringOpptjening());
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
