package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.vtp.testmodell.enheter.Norg2Modell;
import no.nav.foreldrepenger.vtp.testmodell.identer.FiktiveFnr;
import no.nav.foreldrepenger.vtp.testmodell.identer.IdentGenerator;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.BasisdataProvider;
import no.nav.foreldrepenger.vtp.testmodell.util.JacksonWrapperTestscenario;
import no.nav.foreldrepenger.vtp.testmodell.virksomhet.VirksomhetIndeks;
import no.nav.foreldrepenger.vtp.testmodell.virksomhet.VirksomhetModell;

public class BasisdataProviderFileImpl implements BasisdataProvider {

    private final VirksomhetIndeks virksomhetIndeks = new VirksomhetIndeks();
    private final EnheterIndeks enheterIndeks = new EnheterIndeks();
    private final AdresseIndeks adresseIndeks = new AdresseIndeks();
    private final OrganisasjonIndeks organisasjonIndeks = new OrganisasjonIndeks();
    private final IdentGenerator identGenerator = new FiktiveFnr();

    private static final ObjectMapper mapper = JacksonWrapperTestscenario.getObjectMapper();
    private static BasisdataProviderFileImpl instance;

    private BasisdataProviderFileImpl() throws IOException{
        loadAdresser();
        loadEnheter();
        loadVirksomheter();
        loadOrganisasjoner();
    }

    public static synchronized BasisdataProviderFileImpl getInstance() throws IOException{
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
    public IdentGenerator getIdentGenerator() {
        return identGenerator;
    }

    private void loadAdresser() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/basedata/adresse-maler.json")) {
            List<AdresseModell> adresser = Arrays.asList(mapper.readValue(is, AdresseModell[].class));
            adresser.forEach(adresseIndeks::leggTil);
        }
    }

    private void loadEnheter() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/basedata/enheter.json")) {
            List<Norg2Modell> adresser =Arrays.asList(mapper.readValue(is, Norg2Modell[].class));
            enheterIndeks.leggTil(adresser);
        }
    }

    private void loadVirksomheter() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/basedata/virksomheter.json")) {
            List<VirksomhetModell> virksomheter = Arrays.asList(mapper.readValue(is, VirksomhetModell[].class));
            virksomhetIndeks.leggTil(virksomheter);
        }
    }

    private void loadOrganisasjoner() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/basedata/organisasjon.json")) {
            List<OrganisasjonModell> organisasjoner = Arrays.asList(mapper.readValue(is, OrganisasjonModell[].class));
            organisasjonIndeks.leggTil(organisasjoner);
        }
    }

}
