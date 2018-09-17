package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

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
        foreldrepenger.setRelasjonTilBarnet(SoekersRelasjonErketyper.søkerTerminFørTermin());
        foreldrepenger.setFordeling(FordelingErketyper.uttaksPeriodeAltTilMor());
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
