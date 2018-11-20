package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import java.time.LocalDate;

import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Dekningsgrad;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Foreldrepenger;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.Dekningsgrader;

public class ForeldrepengeYtelseErketyper {

    //todo builder?
    public static Foreldrepenger foreldrepengeYtelseNorskBorgerINorgeTermin() {
        Foreldrepenger foreldrepenger = new Foreldrepenger();
        foreldrepenger.setDekningsgrad(standardDekningsgrader());
        foreldrepenger.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        foreldrepenger.setRettigheter(RettigheterErketyper.beggeForeldreRettIkkeAleneomsorg());
        LocalDate termindato = LocalDate.now();
        foreldrepenger.setRelasjonTilBarnet(SoekersRelasjonErketyper.søkerTerminFørTermin(termindato));
        foreldrepenger.setFordeling(FordelingErketyper.fordelingMorHappyCase(termindato));
        return foreldrepenger;
    }

    public static Foreldrepenger foreldrepengerYtelseNorskBorgerINorgeFødselMor(LocalDate startDatoForeldrepenger) {
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

    public static Foreldrepenger foreldrepengeYtelseNorskBorgerINorgeTerminMedFrilans() {
        Foreldrepenger foreldrepenger = foreldrepengeYtelseNorskBorgerINorgeTermin();
        foreldrepenger.setOpptjening(OpptjeningErketyper.medFrilansOpptjening());
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
