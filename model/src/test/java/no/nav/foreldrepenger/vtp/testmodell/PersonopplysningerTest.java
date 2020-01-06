package no.nav.foreldrepenger.vtp.testmodell;

import no.nav.foreldrepenger.vtp.testmodell.medlemskap.MedlemskapperiodeModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.*;
import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.*;
import no.nav.foreldrepenger.vtp.testmodell.util.JsonMapper;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonopplysningerTest {
    // TODO(EW) - Finne ut hvor disse testene ører hjemme?

    @Test
    public void skal_skrive_scenario_til_personopplysninger_json() throws Exception {
        TestscenarioRepositoryImpl testScenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());

        TestscenarioTilTemplateMapper mapper = new TestscenarioTilTemplateMapper();

        TestscenarioImpl scenario = new TestscenarioImpl("test", "test-1", testScenarioRepository);
        JsonMapper jsonMapper =  new JsonMapper(scenario.getVariabelContainer());
        String lokalIdent = "#id1#";
        SøkerModell søker = new SøkerModell(lokalIdent, "Donald", LocalDate.now().minusYears(20), BrukerModell.Kjønn.M);
        Personopplysninger personopplysninger = new Personopplysninger(søker);
        scenario.setPersonopplysninger(personopplysninger);
        søker.setStatsborgerskap(new StatsborgerskapModell(Landkode.NOR));
        søker.setSivilstand(new SivilstandModell("GIFT"));
        søker.setDiskresjonskode(PersonModell.Diskresjonskoder.UDEF);
        søker.setSpråk("nb-NO");
        MedlemskapperiodeModell medlemskapperiode = new MedlemskapperiodeModell();
        søker.leggTil(medlemskapperiode);

        BarnModell barn = new BarnModell("#id2#", "Ole", LocalDate.now());
        personopplysninger.leggTilBarn(barn);

        // Act - writeout
        String json = skrivPersonopplysninger(scenario, mapper, jsonMapper);
        System.out.println("TestscenarioImpl:" + scenario + "\n--------------");
        System.out.println(json);
        System.out.println("--------------");

        // Act - readback

        TestscenarioFraTemplateMapper readMapper = new TestscenarioFraTemplateMapper(testScenarioRepository);
        Testscenario scenario2 = readMapper.lagTestscenario(new StringTestscenarioTemplate("my-template", json, null, null));

        // Assert
        SøkerModell søker2 = scenario2.getPersonopplysninger().getSøker();
        assertThat(søker2).isNotNull();
        assertThat(søker2.getEtternavn()).isNotEmpty();

        SøkerModell søkerFraIndeks = testScenarioRepository.getPersonIndeks().finnByIdent(søker2.getIdent());
        assertThat(søkerFraIndeks).isEqualTo(søker2);
    }

    private String skrivPersonopplysninger(Testscenario scenario, TestscenarioTilTemplateMapper mapper, JsonMapper jsonMapper) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream buf = new BufferedOutputStream(baos);
        mapper.skrivPersonopplysninger(jsonMapper.canonicalMapper(), buf, scenario);
        buf.flush();
        return baos.toString("UTF8");
    }
}
