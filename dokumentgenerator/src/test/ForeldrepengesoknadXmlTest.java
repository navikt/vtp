import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.app.ForeldrepengesoknadXmlGenerator;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.app.InputValideringException;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.SoeknadDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.erketyper.ForeldrepengeSoknadDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.*;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1.*;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1.*;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class ForeldrepengesoknadXmlTest {

    @Test
    public void ForeldrepengeXmlTest(){
        ForeldrepengesoknadXmlGenerator fp = new ForeldrepengesoknadXmlGenerator();

        SoeknadDto soknad = new SoeknadDto();
        soknad.setMottattDato(LocalDate.now());

        ForeldrepengerDto ytelse = new ForeldrepengerDto();

        // Bruker
        BrukerDto bruker = new BrukerDto();
        String fnr = "12345678910";
        String aktoerid = String.valueOf("13371337");
        bruker.setAktoerId(aktoerid);
        BrukerrollerDto brukerrolle = new BrukerrollerDto();
        brukerrolle.setKodeverk("BrukerrollerDto");
        brukerrolle.setKode("-");
        bruker.setSoeknadsrolle(brukerrolle);

        //08069600264 -- 9000000082791
        FullmektigDto fullmektig = new FullmektigDto();

        soknad.setSoeker(bruker);

        // Rettigheter
        RettigheterDto rettigheter = new RettigheterDto();
        rettigheter.setHarOmsorgForBarnetIPeriodene(true);
        rettigheter.setHarAnnenForelderRett(true);
        rettigheter.setHarAleneomsorgForBarnet(false);
        ytelse.setRettigheter(rettigheter);

        // Medlemskap
        MedlemskapDto medlemskap = new MedlemskapDto();
        medlemskap.setBorINorgeNeste12Mnd(true);
        medlemskap.setBoddINorgeSiste12Mnd(false);
        medlemskap.setiNorgeVedFoedselstidspunkt(true);

        OppholdUtlandetDto oppholdUtlandet = new OppholdUtlandetDto();
        LandDto land = new LandDto();
        land.setKode("AFG");
        land.setKodeverk("LandDto");
        oppholdUtlandet.setLand(land);

        PeriodeDto periode = new PeriodeDto();
        periode.setFom(LocalDate.of(2018,1,1));
        periode.setTom(LocalDate.of(2018,2,1));
        oppholdUtlandet.setPeriode(periode);
        medlemskap.getOppholdUtlandet().add(oppholdUtlandet);

        OppholdNorgeDto oppholdNorge = new OppholdNorgeDto();
        PeriodeDto periodeNorge = new PeriodeDto();
        periodeNorge.setFom(LocalDate.now().minusMonths(1));
        periodeNorge.setTom(LocalDate.now().plusMonths(13));
        oppholdNorge.setPeriode(periodeNorge);
        medlemskap.getOppholdNorge().add(oppholdNorge);

        ytelse.setMedlemskap(medlemskap);

        // Dekningsgrad
        DekningsgradDto dekningsgrad = new DekningsgradDto();
        DekningsgraderDto dekningsgrader = new DekningsgraderDto();
        dekningsgrader.setKodeverk("DekningsgraderDto");
        dekningsgrader.setKode("100");
        dekningsgrad.setDekningsgrad(dekningsgrader);
        ytelse.setDekningsgrad(dekningsgrad);

        // Relasjon til barnet
        TerminDto termin = new TerminDto();
        termin.setTermindato(LocalDate.of(2018,7,9));
        termin.setUtstedtdato(LocalDate.of(2018,6,12));
        termin.setAntallBarn(1);
        ytelse.setRelasjonTilBarnet(termin);

        // Fordeling
        FordelingDto fordeling = new FordelingDto();
        fordeling.setAnnenForelderErInformert(true);

        // Opptjening
        OpptjeningDto opptjening = new OpptjeningDto();
        AnnenOpptjeningDto annenOpptjening = new AnnenOpptjeningDto();
        PeriodeDto opptjeningsperiodeMilitaer = new PeriodeDto();
        opptjeningsperiodeMilitaer.setFom(LocalDate.of(2018,3,1));
        opptjeningsperiodeMilitaer.setTom(LocalDate.of(2018,4,1));
        annenOpptjening.setPeriode(opptjeningsperiodeMilitaer);
        AnnenOpptjeningTyperDto opptjeningMilitaer = new AnnenOpptjeningTyperDto();
        opptjeningMilitaer.setKodeverk("OPPTJENING_AKTIVITET_TYPE"); //todo finn riktig kode
        opptjeningMilitaer.setKode("MILITÆR_ELLER_SIVILTJENESTE"); // todo finn riktig
        annenOpptjening.setType(opptjeningMilitaer);
        opptjening.getAnnenOpptjening().add(annenOpptjening);

        // Periode 1
        UttaksperiodeDto uttaksperiode = new UttaksperiodeDto();
        UttaksperiodetyperDto uttaksperiodetyper = new UttaksperiodetyperDto();
        uttaksperiodetyper.setKodeverk("UttaksperiodetyperDto");
        uttaksperiodetyper.setKode("FORELDREPENGER_FØR_FØDSEL");
        uttaksperiode.setType(uttaksperiodetyper);
        uttaksperiode.setOenskerSamtidigUttak(false);
        uttaksperiode.setFom(LocalDate.of(2018,6,18));
        uttaksperiode.setTom(LocalDate.of(2018,7,8));

        MorsAktivitetsTyperDto morsAktivitet = new MorsAktivitetsTyperDto();
        morsAktivitet.setKodeverk("MorsAktivitetsTyperDto");
        morsAktivitet.setKode("-");

        fordeling.getPerioder().add(uttaksperiode);

        // Periode 2
        UttaksperiodeDto uttaksperiode2 = new UttaksperiodeDto();
        UttaksperiodetyperDto uttaksperiodetyper2 = new UttaksperiodetyperDto();
        uttaksperiodetyper2.setKodeverk("UttaksperiodetyperDto");
        uttaksperiodetyper2.setKode("MØDREKVOTE");
        uttaksperiode2.setType(uttaksperiodetyper2);
        uttaksperiode2.setOenskerSamtidigUttak(false);
        uttaksperiode2.setFom(LocalDate.of(2018,7,9));
        uttaksperiode2.setTom(LocalDate.of(2018,10,22));

        MorsAktivitetsTyperDto morsAktivitet2 = new MorsAktivitetsTyperDto();
        morsAktivitet2.setKodeverk("MorsAktivitetsTyperDto");
        morsAktivitet2.setKode("-");

        fordeling.getPerioder().add(uttaksperiode2);

        ytelse.setFordeling(fordeling);



        // Tilleggsopplysninger
        soknad.setTilleggsopplysninger("Autogenerert av ytelsestest");



        soknad.setOmYtelse(ytelse);


        try {
            String xml = fp.lagXml(soknad);

            Assert.assertTrue(xml.contains("13371337"));
        } catch (InputValideringException e) {
            Assert.fail("Skal ikke kaste exception");
        }


    }
}
