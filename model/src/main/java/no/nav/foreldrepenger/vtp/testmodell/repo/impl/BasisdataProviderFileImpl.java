package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import no.nav.foreldrepenger.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.vtp.testmodell.enheter.Norg2Modell;
import no.nav.foreldrepenger.vtp.testmodell.identer.FiktiveFnr;
import no.nav.foreldrepenger.vtp.testmodell.identer.IdentGenerator;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonAdresseModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.BasisdataProvider;
import no.nav.foreldrepenger.vtp.testmodell.util.JsonMapper;
import no.nav.foreldrepenger.vtp.testmodell.virksomhet.VirksomhetIndeks;
import no.nav.foreldrepenger.vtp.testmodell.virksomhet.VirksomhetModell;

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
        loadOrganisasjonsAdresser();
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

    private void loadOrganisasjonsAdresser() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("basedata/organisasjon.json")) {
            TypeReference<List<OrganisasjonAdresseModell>> typeAdrRef = new TypeReference<List<OrganisasjonAdresseModell>>() {
            };
            List<OrganisasjonAdresseModell> organisasjonAdresseModells = jsonMapper.lagObjectMapper().readValue(inputStream, typeAdrRef);
            organisasjonIndeks.leggTilAdresse(organisasjonAdresseModells);
        }
    }
}
