package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import no.nav.vedtak.felles.xml.soeknad.engangsstoenad.v1.Engangsstønad;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.AnnenForelder;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.UkjentForelder;

class EngangstonadYtelseErketyper {

    public static Engangsstønad engangsstønadUkjentForelderNorgeFødselEtterFødsel() {
        Engangsstønad engangsstønad = new Engangsstønad();
        AnnenForelder annenForelder = new UkjentForelder();
        engangsstønad.setAnnenForelder(annenForelder);
        engangsstønad.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        engangsstønad.setSoekersRelasjonTilBarnet(SoekersRelasjonErketyper.søkerFødselEtterFødsel());
        return engangsstønad;
    }



}
