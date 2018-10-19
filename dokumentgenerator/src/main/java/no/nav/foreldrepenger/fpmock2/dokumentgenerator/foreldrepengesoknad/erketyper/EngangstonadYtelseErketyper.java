package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import no.nav.vedtak.felles.xml.soeknad.engangsstoenad.v1.Engangsstønad;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.AnnenForelder;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.UkjentForelder;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Ytelse;

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
}
