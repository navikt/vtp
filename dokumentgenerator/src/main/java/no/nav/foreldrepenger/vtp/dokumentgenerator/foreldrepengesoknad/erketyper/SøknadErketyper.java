package no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper;

import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.SøkersRolle;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders.SøknadBuilder;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders.ytelse.EngangstønadYtelseBuilder;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders.ytelse.ForeldrepengerYtelseBuilder;
import no.nav.foreldrepenger.vtp.kontrakter.TestscenarioDto;
import no.nav.vedtak.felles.xml.soeknad.endringssoeknad.v3.Endringssoeknad;
import no.nav.vedtak.felles.xml.soeknad.engangsstoenad.v3.Engangsstønad;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Foreldrepenger;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Fordeling;

import java.time.LocalDate;

public class SøknadErketyper {

    public static SøknadBuilder foreldrepengesøknadTerminErketype(String aktorID, SøkersRolle søkersRolle,
                                                                  int antallBarn, LocalDate termindato) {
        Foreldrepenger foreldrepenger = new ForeldrepengerYtelseBuilder(
                SoekersRelasjonErketyper.termin(antallBarn, termindato),
                FordelingErketyper.fordelingHappyCase(termindato, søkersRolle))
                .build();

        return new SøknadBuilder(foreldrepenger, aktorID, søkersRolle);
    }
    public static SøknadBuilder foreldrepengesøknadFødselErketype(String aktorID, SøkersRolle søkersRolle,
                                                                  int antallBarn, LocalDate fødselsdato) {
        Foreldrepenger foreldrepenger = new ForeldrepengerYtelseBuilder(
                SoekersRelasjonErketyper.fødsel(antallBarn, fødselsdato),
                FordelingErketyper.fordelingHappyCase(fødselsdato, søkersRolle))
                .build();;
        return new SøknadBuilder(foreldrepenger, aktorID, søkersRolle);
    }
    public static SøknadBuilder endringssøknadErketype(String aktoerId, SøkersRolle søkersRolle,
                                                       Fordeling fordeling, String saksnummer) {
        Endringssoeknad endringssoeknad = new Endringssoeknad();
        endringssoeknad.setFordeling(fordeling);
        endringssoeknad.setSaksnummer(saksnummer);
        return new SøknadBuilder(endringssoeknad, aktoerId, søkersRolle);
    }
    public static SøknadBuilder engangstønadsøknadFødselErketype(String aktørID, SøkersRolle søkersRolle,
                                                                 int antallBarn, LocalDate fødselsdato){
        Engangsstønad engangsstønad = new EngangstønadYtelseBuilder(
                SoekersRelasjonErketyper.fødsel(antallBarn,fødselsdato))
                .build();
        return new SøknadBuilder(engangsstønad, aktørID, søkersRolle)
                .withTilleggsopplysninger("Autogenerert erketypetest" + søkersRolle.toString() + "søker på fødsel");
    }
    public static SøknadBuilder engangstønadsøknadTerminErketype(String aktørID, SøkersRolle søkersRolle,
                                                                 int antallBarn, LocalDate termindato){
        Engangsstønad engangsstønad = new EngangstønadYtelseBuilder(
                SoekersRelasjonErketyper.termin(antallBarn, termindato))
                .build();
        return new SøknadBuilder(engangsstønad, aktørID, søkersRolle)
                .withTilleggsopplysninger("Autogenerert erketypetest" + søkersRolle.toString() + "søker på termin");
    }
    public static SøknadBuilder engangstønadsøknadAdopsjonErketype(String aktørID, SøkersRolle søkersRolle,
                                                                   Boolean ektefellesBarn){
        Engangsstønad engangsstønad = new EngangstønadYtelseBuilder(
                SoekersRelasjonErketyper.adopsjon(ektefellesBarn))
                .build();
        return new SøknadBuilder(engangsstønad, aktørID, søkersRolle)
                .withTilleggsopplysninger("Autogenerert erketypetest" + søkersRolle.toString() + "søker på adopsjon");
    }
}
