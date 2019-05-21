package no.nav.foreldrepenger.fpmock2.testmodell;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseType;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.GateadresseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Landkode;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.UstrukturertAdresseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplate;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.util.JsonMapper;

public class AdresserTest {

    @Test
    public void sjekk_scenarios() throws Exception {
        TestscenarioRepositoryImpl testScenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        TestscenarioTemplateRepositoryImpl templateRepository = TestscenarioTemplateRepositoryImpl.getInstance();
        templateRepository.load();
        for (TestscenarioTemplate testScenarioTemplate : templateRepository.getTemplates()) {
            TestscenarioImpl testScenario = testScenarioRepository.opprettTestscenario(testScenarioTemplate);
            sjekkAdresseIndeks(testScenario);
        }
    }

    @Test
    public void skriv_ut_adresser() throws Exception {
        GateadresseModell gateadresse = new GateadresseModell();
        gateadresse.setGatenavn("Fjordlandet");
        gateadresse.setGatenummer(15);
        gateadresse.setHusbokstav("B");
        gateadresse.setHusnummer(10);
        gateadresse.setPostnummer("2500");
        gateadresse.setLand(Landkode.NOR);
        gateadresse.setAdresseType(AdresseType.BOSTEDSADRESSE);

        UstrukturertAdresseModell a = new UstrukturertAdresseModell();
        a.setAdresseLinje1("Allé skal med 12");
        a.setAdresseLinje2("GateadresseModell 2");
        a.setAdresseLinje3("Rislunch");
        a.setAdresseLinje3("2500 Tynset");
        a.setLand(Landkode.NOR);
        a.setAdresseType(AdresseType.POSTADRESSE);

        UstrukturertAdresseModell a1 = new UstrukturertAdresseModell();
        a1.setAdresseLinje1("Vinsjan ved kaia");
        a1.setAdresseLinje2("1336");
        a1.setLand(Landkode.NOR);
        a1.setAdresseType(AdresseType.MIDLERTIDIG_POSTADRESSE);

        UstrukturertAdresseModell a2 = new UstrukturertAdresseModell();
        a2.setAdresseLinje1("President Avenue");
        a2.setAdresseLinje2("Central district");
        a2.setAdresseLinje3("Washington DC");
        a2.setAdresseLinje3("USA");
        a2.setLand(Landkode.USA);
        a2.setAdresseType(AdresseType.MIDLERTIDIG_POSTADRESSE);

        List<Object> adresser = Arrays.asList(gateadresse, a, a1, a2);

        StringWriter sw = new StringWriter();
        TypeReference<List<AdresseModell>> typeAdresseListe = new TypeReference<List<AdresseModell>>() {
        };
        new JsonMapper().lagObjectMapper().writerWithDefaultPrettyPrinter().forType(typeAdresseListe).writeValue(sw, adresser);

        String json = sw.toString();
        System.out.println(json);

        List<AdresseModell> adresser2 = new JsonMapper().lagObjectMapper().readValue(json, typeAdresseListe);

        assertThat(adresser2).hasSize(adresser.size());
    }

    private void sjekkAdresseIndeks(TestscenarioImpl sc) {
        assertThat(sc.getAdresseIndeks()).isNotNull();
        AdresseModell bostedsadresse = sc.getAdresseIndeks().finn(AdresseType.BOSTEDSADRESSE, Landkode.NOR);
        assertThat(bostedsadresse).isNotNull();
        assertThat(sc.getAdresseIndeks().finn(AdresseType.MIDLERTIDIG_POSTADRESSE, Landkode.NOR)).isNotNull();
        assertThat(sc.getAdresseIndeks().finn(AdresseType.MIDLERTIDIG_POSTADRESSE, Landkode.USA)).isNotNull();
        assertThat(sc.getAdresseIndeks().finn(AdresseType.POSTADRESSE, Landkode.NOR)).isNotNull();
    }

}
