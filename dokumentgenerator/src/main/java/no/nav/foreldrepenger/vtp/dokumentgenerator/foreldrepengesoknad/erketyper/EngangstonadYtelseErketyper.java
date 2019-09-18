package no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper;

import java.time.LocalDate;

import no.nav.vedtak.felles.xml.soeknad.engangsstoenad.v3.Engangsstønad;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.AnnenForelder;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.AnnenForelderMedNorskIdent;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.UkjentForelder;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.Ytelse;

class EngangstonadYtelseErketyper {

    public static Engangsstønad engangsstønadUkjentForelderNorgeFødselEtterFødsel() {
        Engangsstønad engangsstønad = new Engangsstønad();
        AnnenForelder annenForelder = new UkjentForelder();
        engangsstønad.setAnnenForelder(annenForelder);
        engangsstønad.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        engangsstønad.setSoekersRelasjonTilBarnet(SoekersRelasjonErketyper.søkerFødselEtterFødsel());
        return engangsstønad;
    }

    public static Engangsstønad engangsstønadUkjentForelderNorgeFødselEtterFødselFlereBarn() {
        Engangsstønad engangsstønad = new Engangsstønad();
        AnnenForelder annenForelder = new UkjentForelder();
        engangsstønad.setAnnenForelder(annenForelder);
        engangsstønad.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        engangsstønad.setSoekersRelasjonTilBarnet(SoekersRelasjonErketyper.søkerFødselEtterFødselFlereBarn());
        return engangsstønad;
    }

    public static Ytelse engangsstønadUkjentForelderNorgeFødselEtterSøknadsfrist() {
        Engangsstønad engangsstønad = new Engangsstønad();
        AnnenForelder annenForelder = new UkjentForelder();
        engangsstønad.setAnnenForelder(annenForelder);
        engangsstønad.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        engangsstønad.setSoekersRelasjonTilBarnet(SoekersRelasjonErketyper.søkerFødselEtterSøknadsfrist());
        return engangsstønad;
    }

    public static Ytelse engangsstønadUkjentForelderNorgeTerminFørTermin() {
        Engangsstønad engangsstønad = new Engangsstønad();
        AnnenForelder annenForelder = new UkjentForelder();
        engangsstønad.setAnnenForelder(annenForelder);
        engangsstønad.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        engangsstønad.setSoekersRelasjonTilBarnet(SoekersRelasjonErketyper.søkerTerminFørTermin());
        return engangsstønad;
    }

    public static Ytelse engangsstønadUkjentForelderNorgeTermin(LocalDate termindato) {
        Engangsstønad engangsstønad = new Engangsstønad();
        AnnenForelder annenForelder = new UkjentForelder();
        engangsstønad.setAnnenForelder(annenForelder);
        engangsstønad.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        engangsstønad.setSoekersRelasjonTilBarnet(SoekersRelasjonErketyper.søkerTermin(termindato));
        return engangsstønad;
    }

    public static Ytelse engangsstønadUkjentForelderNorgeAdopsjon() {
        Engangsstønad engangsstønad = new Engangsstønad();
        AnnenForelder annenForelder = new UkjentForelder();
        engangsstønad.setAnnenForelder(annenForelder);
        engangsstønad.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        engangsstønad.setSoekersRelasjonTilBarnet(SoekersRelasjonErketyper.søkerAdopsjon());
        return engangsstønad;
    }

    public static Ytelse engangsstønadUkjentForelderNorgeOmsorgsovertakelse() {
        Engangsstønad engangsstønad = new Engangsstønad();
        AnnenForelder annenForelder = new UkjentForelder();
        engangsstønad.setAnnenForelder(annenForelder);
        engangsstønad.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        engangsstønad.setSoekersRelasjonTilBarnet(SoekersRelasjonErketyper.søkerOmsorgsovertakelseGrunnetDød());
        return engangsstønad;
    }

    public static Ytelse engangsstønadUkjentForelderNorgeFødsel4DagerEtterFødsel() {
        Engangsstønad engangsstønad = new Engangsstønad();
        AnnenForelder annenForelder = new UkjentForelder();
        engangsstønad.setAnnenForelder(annenForelder);
        engangsstønad.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        engangsstønad.setSoekersRelasjonTilBarnet(SoekersRelasjonErketyper.søkerFødsel4DagerEtterFødsel());
        return engangsstønad;
    }

    public static Ytelse engangsstønadTerminKjentAnnenForelder(String annenpartAktørId) {
        Engangsstønad engangsstønad = new Engangsstønad();
        AnnenForelderMedNorskIdent annenForelder = new AnnenForelderMedNorskIdent();
        annenForelder.setAktoerId(annenpartAktørId);
        engangsstønad.setAnnenForelder(annenForelder);
        engangsstønad.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        engangsstønad.setSoekersRelasjonTilBarnet(SoekersRelasjonErketyper.søkerTermin(LocalDate.now().plusDays(5)));
        return engangsstønad;
    }
}
