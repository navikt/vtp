package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import no.nav.foreldrepenger.fpmock2.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.enheter.Norg2Modell;
import no.nav.foreldrepenger.fpmock2.testmodell.identer.FiktiveFnr;
import no.nav.foreldrepenger.fpmock2.testmodell.identer.IdentGenerator;
import no.nav.foreldrepenger.fpmock2.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.BasisdataProvider;
import no.nav.foreldrepenger.fpmock2.testmodell.util.JsonMapper;
import no.nav.foreldrepenger.fpmock2.testmodell.virksomhet.VirksomhetIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.virksomhet.VirksomhetModell;

public class BasisdataProviderFileImpl implements BasisdataProvider {

    private final VirksomhetIndeks virksomhetIndeks = new VirksomhetIndeks();
    private final EnheterIndeks enheterIndeks = new EnheterIndeks();
    private final AdresseIndeks adresseIndeks = new AdresseIndeks();
    private final OrganisasjonIndeks organisasjonIndeks = new OrganisasjonIndeks();
    private final IdentGenerator identGenerator = new FiktiveFnr();

    private final JsonMapper jsonMapper = new JsonMapper();

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
            TypeReference<List<AdresseModell>> typeRef = new TypeReference<List<AdresseModell>>() {
            };
            List<AdresseModell> adresser = jsonMapper.lagObjectMapper().readValue(is, typeRef);
            adresser.forEach(a -> adresseIndeks.leggTil(a));
        }
    }

    private void loadEnheter() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/basedata/enheter.json")) {
            TypeReference<List<Norg2Modell>> typeRef = new TypeReference<List<Norg2Modell>>() {
            };
            List<Norg2Modell> adresser = jsonMapper.lagObjectMapper().readValue(is, typeRef);
            enheterIndeks.leggTil(adresser);
        }
    }

    private void loadVirksomheter() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/basedata/virksomheter.json")) {
            TypeReference<List<VirksomhetModell>> typeRef = new TypeReference<List<VirksomhetModell>>() {
            };
            List<VirksomhetModell> virksomheter = jsonMapper.lagObjectMapper().readValue(is, typeRef);
            virksomhetIndeks.leggTil(virksomheter);
        }
    }

    private void loadOrganisasjoner() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/basedata/organisasjon.json")) {
            TypeReference<List<OrganisasjonModell>> typeRef = new TypeReference<List<OrganisasjonModell>>() {
            };
            List<OrganisasjonModell> organisasjoner = jsonMapper.lagObjectMapper().readValue(is, typeRef);
            organisasjonIndeks.leggTil(organisasjoner);
        }
    }
}
