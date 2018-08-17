package no.nav.foreldrepenger.fpmock2.testmodell;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseType;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.BarnModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.BrukerModell.Kjønn;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.FamilierelasjonModell.Rolle;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.GateadresseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Identer;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.PersonModell.Diskresjonskoder;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.SivilstandModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.StatsborgerskapModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.SøkerModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.UstrukturertAdresseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.util.JsonMapper;

public class ScenariosTest {

    private static final String TEST_SCENARIO_NAVN = "100-mor-og-far-bosatt-i-norge";

    @Test
    public void skal_laste_scenarios() throws Exception {
        boolean avsjekketEttScenario = false;
        Collection<Scenario> scenarios = Scenarios.scenarios();
        for (Scenario sc : scenarios) {
            sjekkAdresseIndeks(sc);
            sjekkIdenterErInjisert(sc);

            Personopplysninger pers = sc.getPersonopplysninger();
            assertThat(pers).isNotNull();
            SøkerModell søker = pers.getSøker();
            assertThat(pers.getFamilierelasjoner()).isNotEmpty();
            assertThat(søker.getGeografiskTilknytning()).isNotNull();

            if (sc.getNavn().equals(TEST_SCENARIO_NAVN)) {
                avsjekketEttScenario = avsjekkSpesifiktScenario(pers, søker);
            }
        }

        assertThat(avsjekketEttScenario).as("fant aldri scenario").isTrue();
    }

    private boolean avsjekkSpesifiktScenario(Personopplysninger pers, SøkerModell søker) {
        boolean avsjekketEttScenario;
        avsjekketEttScenario = true;
        
        assertThat(søker.getIdent()).isEqualTo("12341234123");

        FamilierelasjonModell familierelasjon = pers.getFamilierelasjoner(Rolle.BARN).findFirst().get();
        assertThat(familierelasjon.getTil()).isNotNull();
        assertThat(familierelasjon.getTil()).isInstanceOf(BarnModell.class);
        // sjekk variable er satt inn
        assertThat(((BarnModell) familierelasjon.getTil()).getFornavn()).isEqualTo("Dole");

        assertThat(søker.getAdresser()).hasSize(1);
        // hentet fra adressekatalog ikke fra personopplysninger.json
        Optional<AdresseModell> bostedsAdresseOpt = søker.getAdresse(AdresseType.BOSTEDSADRESSE);
        assertThat(bostedsAdresseOpt).isPresent();
        GateadresseModell gateadresse = (GateadresseModell) bostedsAdresseOpt.get();
        assertThat(gateadresse.getGatenavn()).isEqualTo("Fjordlandet");
        assertThat(gateadresse.getFom()).isEqualTo(LocalDate.now().minusYears(1));
        return avsjekketEttScenario;
    }

    private void sjekkIdenterErInjisert(Scenario sc) {
        sc.getIdenter().getAlleIdenter().entrySet().forEach(e -> {
            System.out.println(e);
        });
        System.out.println("--------------");
    }

    private void sjekkAdresseIndeks(Scenario sc) {
        assertThat(sc.getAdresseIndeks()).isNotNull();
        AdresseModell bostedsadresse = sc.getAdresseIndeks().finn(AdresseType.BOSTEDSADRESSE, "NOR");
        assertThat(bostedsadresse).isNotNull();
        assertThat(sc.getAdresseIndeks().finn(AdresseType.MIDLERTIDIG_POSTADRESSE, "NOR")).isNotNull();
        assertThat(sc.getAdresseIndeks().finn(AdresseType.MIDLERTIDIG_POSTADRESSE, "USA")).isNotNull();
        assertThat(sc.getAdresseIndeks().finn(AdresseType.POSTADRESSE, "NOR")).isNotNull();
    }

    @Test
    public void skriv_ut_adresser() throws Exception {
        GateadresseModell gateadresse = new GateadresseModell();
        gateadresse.setGatenavn("Fjordlandet");
        gateadresse.setGatenummer(15);
        gateadresse.setHusbokstav("B");
        gateadresse.setHusnummer(10);
        gateadresse.setPostnummer("2500");
        gateadresse.setLandkode("NOR");
        gateadresse.setAdresseType(AdresseType.BOSTEDSADRESSE);

        UstrukturertAdresseModell a = new UstrukturertAdresseModell();
        a.setAdresseLinje1("Allé skal med 12");
        a.setAdresseLinje2("GateadresseModell 2");
        a.setAdresseLinje3("Rislunch");
        a.setAdresseLinje3("2500 Tynset");
        a.setLandkode("NOR");
        a.setAdresseType(AdresseType.POSTADRESSE);

        UstrukturertAdresseModell a1 = new UstrukturertAdresseModell();
        a1.setAdresseLinje1("Vinsjan ved kaia");
        a1.setAdresseLinje2("1336");
        a1.setLandkode("NOR");
        a1.setAdresseType(AdresseType.MIDLERTIDIG_POSTADRESSE);

        UstrukturertAdresseModell a2 = new UstrukturertAdresseModell();
        a2.setAdresseLinje1("President Avenue");
        a2.setAdresseLinje2("Central district");
        a2.setAdresseLinje3("Washington DC");
        a2.setAdresseLinje3("USA");
        a2.setLandkode("USA");
        a2.setAdresseType(AdresseType.MIDLERTIDIG_POSTADRESSE);

        List<Object> adresser = Arrays.asList(gateadresse, a, a1, a2);

        StringWriter sw = new StringWriter();
        TypeReference<List<AdresseModell>> typeAdresseListe = new TypeReference<List<AdresseModell>>() {
        };
        new JsonMapper().getObjectMapper().writerWithDefaultPrettyPrinter().forType(typeAdresseListe).writeValue(sw, adresser);

        String json = sw.toString();
        System.out.println(json);

        List<AdresseModell> adresser2 = new JsonMapper().getObjectMapper().readValue(json, typeAdresseListe);

        assertThat(adresser2).hasSize(adresser.size());
    }

    @Test
    public void skal_skrive_scenario_til_json_fil() throws Exception {
        ScenarioMapper mapper = new ScenarioMapper();
        Identer identer = mapper.getIndeks().getIdenter("test");

        Scenario scenario = new Scenario("test", identer);
        String lokalIdent = "#id1#";
        SøkerModell søker = new SøkerModell(lokalIdent, "Donald", LocalDate.now().minusYears(20), Kjønn.M);
        Personopplysninger personopplysninger = new Personopplysninger(søker);
        scenario.setPersonopplysninger(personopplysninger);
        søker.setStatsborgerskap(new StatsborgerskapModell("NOR"));
        søker.setSivilstand(new SivilstandModell("GIFT"));
        søker.setDiskresjonskode(Diskresjonskoder.UDEF);
        søker.setSpråk("nb-NO");

        BarnModell barn = new BarnModell("#id2#", "Ole", LocalDate.now());
        personopplysninger.leggTilBarn(barn);

        // Act - writeout
        String json = writeOut(scenario, mapper);
        System.out.println("Scenario:" + scenario.getNavn() + "\n--------------");
        System.out.println(json);
        System.out.println("--------------");

        // Act - readback
        Scenario scenario2 = new Scenario("test", identer);

        mapper.load(scenario2, new StringReader(json));

        // Assert
        SøkerModell søker2 = scenario2.getPersonopplysninger().getSøker();
        assertThat(søker2).isNotNull();
        assertThat(søker2.getEtternavn()).isEqualTo("Donald");
        assertThat(søker2.getIdent()).isEqualTo(søker.getIdent()).isNotEqualTo(lokalIdent);

        SøkerModell søkerFraIndeks = mapper.getIndeks().getPersonIndeks().finnByIdent(søker2.getIdent());
        assertThat(søkerFraIndeks).isEqualTo(søker2);
    }

    private String writeOut(Scenario scenario, ScenarioMapper mapper) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream buf = new BufferedOutputStream(baos);
        mapper.writeTo(buf, scenario);
        buf.flush();
        return baos.toString("UTF8");
    }
}
