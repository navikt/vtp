package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import no.nav.vedtak.felles.xml.soeknad.felles.v1.Rettigheter;

public class RettigheterErketyper {
    public static Rettigheter beggeForeldreRettIkkeAleneomsorg(){
        Rettigheter rettigheter = new Rettigheter();
        rettigheter.setHarAleneomsorgForBarnet(false);
        rettigheter.setHarAnnenForelderRett(true);
        rettigheter.setHarOmsorgForBarnetIPeriodene(true);

        return rettigheter;
    }

    public static Rettigheter morHarAleneOmsorgOgEnerett(){
        Rettigheter rettigheter = new Rettigheter();
        rettigheter.setHarOmsorgForBarnetIPeriodene(true);
        rettigheter.setHarAnnenForelderRett(false);
        rettigheter.setHarAleneomsorgForBarnet(true);

        return rettigheter;
    }
}
