package no.nav.foreldrepenger.fpmock2.testmodell;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import org.junit.Test;

import no.nav.foreldrepenger.fpmock2.testmodell.medlemskap.MedlemskapperiodeModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.BarnModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.BrukerModell.Kjønn;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Landkode;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.PersonModell.Diskresjonskoder;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.SivilstandModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.StatsborgerskapModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.SøkerModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.StringTestscenarioTemplate;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioFraTemplateMapper;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioTilTemplateMapper;
import no.nav.foreldrepenger.fpmock2.testmodell.util.JsonMapper;

public class PersonopplysningerTest {

    @Test
    public void skal_skrive_scenario_til_personopplysninger_json() throws Exception {
        TestscenarioRepositoryImpl testScenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());

        TestscenarioTilTemplateMapper mapper = new TestscenarioTilTemplateMapper();

        TestscenarioImpl scenario = new TestscenarioImpl("test", "test-1", testScenarioRepository);
        JsonMapper jsonMapper =  new JsonMapper(scenario.getVariabelContainer());
        String lokalIdent = "#id1#";
        SøkerModell søker = new SøkerModell(lokalIdent, "Donald", LocalDate.now().minusYears(20), Kjønn.M);
        Personopplysninger personopplysninger = new Personopplysninger(søker);
        scenario.setPersonopplysninger(personopplysninger);
        søker.setStatsborgerskap(new StatsborgerskapModell(Landkode.NOR));
        søker.setSivilstand(new SivilstandModell("GIFT"));
        søker.setDiskresjonskode(Diskresjonskoder.UDEF);
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
        assertThat(søker2.getEtternavn()).isEqualTo("Donald");

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
