package no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper;

import no.nav.vedtak.felles.xml.soeknad.felles.v3.Rettigheter;

public class RettigheterErketyper {
    public static Rettigheter beggeForeldreRettIkkeAleneomsorg(){
        Rettigheter rettigheter = new Rettigheter();
        rettigheter.setHarAleneomsorgForBarnet(false);
        rettigheter.setHarAnnenForelderRett(true);
        rettigheter.setHarOmsorgForBarnetIPeriodene(true);

        return rettigheter;
    }

    public static Rettigheter harAleneOmsorgOgEnerett(){
        Rettigheter rettigheter = new Rettigheter();
        rettigheter.setHarOmsorgForBarnetIPeriodene(true);
        rettigheter.setHarAnnenForelderRett(false);
        rettigheter.setHarAleneomsorgForBarnet(true);

        return rettigheter;
    }

    public static Rettigheter harIkkeAleneomsorgOgAnnenpartIkkeRett() {
        Rettigheter rettigheter = new Rettigheter();
        rettigheter.setHarOmsorgForBarnetIPeriodene(true);
        rettigheter.setHarAnnenForelderRett(false);
        rettigheter.setHarAleneomsorgForBarnet(false);

        return rettigheter;
    }
}
