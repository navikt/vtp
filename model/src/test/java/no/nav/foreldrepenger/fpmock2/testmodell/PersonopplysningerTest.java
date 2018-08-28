package no.nav.foreldrepenger.fpmock2.testmodell;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
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

public class PersonopplysningerTest {
    
    @Test
    public void skal_skrive_scenario_til_personopplysninger_json() throws Exception {
        ScenarioMapper mapper = new ScenarioMapper();
        ScenarioIdenter identer = mapper.getIndeks().getIdenter("test");

        Scenario scenario = new Scenario("test", identer, mapper.getVirksomheter());
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
        String json = skrivPersonopplysninger(scenario, mapper);
        System.out.println("Scenario:" + scenario.getNavn() + "\n--------------");
        System.out.println(json);
        System.out.println("--------------");

        // Act - readback
        Scenario scenario2 = new Scenario("test", identer, mapper.getVirksomheter());

        mapper.load(scenario2, new StringReader(json));

        // Assert
        SøkerModell søker2 = scenario2.getPersonopplysninger().getSøker();
        assertThat(søker2).isNotNull();
        assertThat(søker2.getEtternavn()).isEqualTo("Donald");
        assertThat(søker2.getIdent()).isEqualTo(søker.getIdent()).isNotEqualTo(lokalIdent);

        SøkerModell søkerFraIndeks = mapper.getIndeks().getPersonIndeks().finnByIdent(søker2.getIdent());
        assertThat(søkerFraIndeks).isEqualTo(søker2);
    }


    private String skrivPersonopplysninger(Scenario scenario, ScenarioMapper mapper) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream buf = new BufferedOutputStream(baos);
        mapper.skrivPersonopplysninger(buf, scenario, true);
        buf.flush();
        return baos.toString("UTF8");
    }
}
