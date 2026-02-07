package no.nav.foreldrepenger.vtp.testmodell.jackson;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.AleneOmOmsorgen;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.Kilde;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.OmsorgspengerModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.OmsorgspengerRammemeldingerModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.OverføringFått;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.OverføringGitt;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.KoronaOverføringFått;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.KoronaOverføringGitt;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.Person;

class OmsorgpengerSeraliseringDeseraliseringsTest extends TestscenarioSerializationTestBase {

    @Test
    void PersonSeraliseringDeseraliseringTest() {
        test(lagPerson());
    }

    @Test
    void KildeSeraliseringDeseraliseringTest() {
        test(lagKilde());
    }

    @Test
    void AleneOmOmsorgenSeraliseringDeseraliseringTest() {
        test(lagAleneOmOmsorgen());
    }

    @Test
    void OverføringFåttSeraliseringDeseraliseringTest() {
        test(lagOverføringFått());
    }

    @Test
    void OverføringGittSeraliseringDeseraliseringTest() {
        test(lagOverføringGitt());
    }

    @Test
    void KoronaOverføringFåttSeraliseringDeseraliseringTest() {
        test(lagKoronaOverføringFått());
    }

    @Test
    void KoronaOverføringGittSeraliseringDeseraliseringTest() {
        test(lagKoronaOverføringGitt());
    }

    @Test
    void OmsorgspengerRammemeldingerModellSeraliseringDeseraliseringTest() {
        test(lagOmsorgspengerRammemeldingerModell());
    }
    @Test
    void OmsorgspengerModellSeraliseringDeseraliseringTest() {
        test(lagOmsorgspengerModell());
    }

    protected OmsorgspengerModell lagOmsorgspengerModell() {
        return new OmsorgspengerModell(lagOmsorgspengerRammemeldingerModell());
    }

    private OmsorgspengerRammemeldingerModell lagOmsorgspengerRammemeldingerModell() {
        return new OmsorgspengerRammemeldingerModell(
                List.of(lagAleneOmOmsorgen()),
                List.of(lagOverføringGitt()),
                List.of(lagOverføringFått()),
                List.of(lagKoronaOverføringGitt()),
                List.of(lagKoronaOverføringFått())
        );
    }

    private OverføringGitt lagOverføringGitt() {
        return new OverføringGitt(LocalDate.now(), LocalDate.now(), LocalDate.now(), Duration.ofMillis(2000), lagPerson(), List.of(lagKilde()));
    }

    private OverføringFått lagOverføringFått() {
        return new OverføringFått(LocalDate.now(), LocalDate.now(), LocalDate.now(), Duration.ofMillis(2000), lagPerson(), List.of(lagKilde()));
    }

    private AleneOmOmsorgen lagAleneOmOmsorgen() {
        return new AleneOmOmsorgen(LocalDate.now(), LocalDate.now(), LocalDate.now(), LocalDate.now(), lagPerson(), List.of(lagKilde()));
    }

    private KoronaOverføringGitt lagKoronaOverføringGitt() {
        return new KoronaOverføringGitt(LocalDate.now(), LocalDate.now(), LocalDate.now(), Duration.ofMillis(2000), lagPerson(), List.of(lagKilde()));
    }

    private KoronaOverføringFått lagKoronaOverføringFått() {
        return new KoronaOverføringFått(LocalDate.now(), LocalDate.now(), LocalDate.now(), Duration.ofMillis(2000), lagPerson(), List.of(lagKilde()));
    }

    private Kilde lagKilde() {
        return new Kilde("12345", "type");
    }


    private Person lagPerson() {
        return new Person("12345678910", "Type", LocalDate.now());
    }
}
