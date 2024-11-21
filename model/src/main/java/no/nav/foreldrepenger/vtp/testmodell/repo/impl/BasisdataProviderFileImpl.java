package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.NavAnsatt;
import no.nav.foreldrepenger.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.vtp.testmodell.enheter.Norg2Modell;
import no.nav.foreldrepenger.vtp.testmodell.identer.FiktiveFnr;
import no.nav.foreldrepenger.vtp.testmodell.identer.IdentGenerator;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.BasisdataProvider;
import no.nav.foreldrepenger.vtp.testmodell.util.JacksonObjectMapperTestscenario;
import no.nav.foreldrepenger.vtp.testmodell.virksomhet.VirksomhetIndeks;
import no.nav.foreldrepenger.vtp.testmodell.virksomhet.VirksomhetModell;

public class BasisdataProviderFileImpl implements BasisdataProvider {
    private static final String BASEDATA_RESOURCE_FOLDER_PATH = "/basedata/";
    private static final String ADRESSE_MALER = BASEDATA_RESOURCE_FOLDER_PATH + "adresse-maler.json";
    private static final String ENHETER = BASEDATA_RESOURCE_FOLDER_PATH + "enheter.json";
    private static final String VIRKSOMHETER = BASEDATA_RESOURCE_FOLDER_PATH + "virksomheter.json";
    private static final String ANSATTE = BASEDATA_RESOURCE_FOLDER_PATH + "nav-ansatte.json";
    private static final String ORGANISASJON = BASEDATA_RESOURCE_FOLDER_PATH + "organisasjon.json";

    private final VirksomhetIndeks virksomhetIndeks = new VirksomhetIndeks();
    private final EnheterIndeks enheterIndeks = new EnheterIndeks();
    private final AdresseIndeks adresseIndeks = new AdresseIndeks();
    private final AnsatteIndeks ansatteIndeks = new AnsatteIndeks();
    private final OrganisasjonIndeks organisasjonIndeks = new OrganisasjonIndeks();
    private final IdentGenerator identGenerator = new FiktiveFnr();

    private static final ObjectMapper mapper = JacksonObjectMapperTestscenario.getObjectMapper();
    private static BasisdataProviderFileImpl instance;

    private BasisdataProviderFileImpl() {
        loadAdresser();
        loadEnheter();
        loadVirksomheter();
        loadAnsatte();
        loadOrganisasjoner();
    }

    public static synchronized BasisdataProviderFileImpl getInstance() {
        if(instance == null){
            instance = new BasisdataProviderFileImpl();
        }
        return instance;
    }


    @Override
    public VirksomhetIndeks getVirksomhetIndeks() {
        return virksomhetIndeks;
    }

    @Override
    public EnheterIndeks getEnheterIndeks() {
        return enheterIndeks;
    }

    @Override
    public AdresseIndeks getAdresseIndeks() {
        return adresseIndeks;
    }

    @Override
    public AnsatteIndeks getAnsatteIndeks() {
        return ansatteIndeks;
    }

    @Override
    public IdentGenerator getIdentGenerator() {
        return identGenerator;
    }

    private void loadAdresser() {
        try (var is = getClass().getResourceAsStream(ADRESSE_MALER)) {
            var adresser = Arrays.asList(mapper.readValue(is, AdresseModell[].class));
            adresser.forEach(adresseIndeks::leggTil);
        } catch (IOException e) {
            throwIllegaleStateExecption(ADRESSE_MALER, e);
        }
    }

    private void loadEnheter() {
        try (var is = getClass().getResourceAsStream(ENHETER)) {
            var adresser = Arrays.asList(mapper.readValue(is, Norg2Modell[].class));
            enheterIndeks.leggTil(adresser);
        } catch (IOException e) {
            throwIllegaleStateExecption(ENHETER, e);
        }
    }

    private void loadVirksomheter() {
        try (var is = getClass().getResourceAsStream(VIRKSOMHETER)) {
            var virksomheter = Arrays.asList(mapper.readValue(is, VirksomhetModell[].class));
            virksomhetIndeks.leggTil(virksomheter);
        } catch (IOException e) {
            throwIllegaleStateExecption(VIRKSOMHETER, e);
        }
    }

    private void loadAnsatte() {
        try (var is = getClass().getResourceAsStream(ANSATTE)) {
            var ansatte = Arrays.asList(mapper.readValue(is, NavAnsatt[].class));
            ansatte.sort(Comparator.comparing(NavAnsatt::ident));
            ansatteIndeks.leggTil(ansatte);
        } catch (IOException e) {
            throwIllegaleStateExecption(ANSATTE, e);
        }
    }

    private void loadOrganisasjoner() {
        try (var is = getClass().getResourceAsStream(ORGANISASJON)) {
            var organisasjoner = Arrays.asList(mapper.readValue(is, OrganisasjonModell[].class));
            organisasjonIndeks.leggTil(organisasjoner);
        } catch (IOException e) {
            throwIllegaleStateExecption(ORGANISASJON, e);
        }
    }

    private void throwIllegaleStateExecption(String filnavn, IOException e) {
        throw new IllegalStateException("Noe gikk galt ved lesing av " + filnavn, e);
    }

}
