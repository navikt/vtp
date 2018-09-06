package no.nav.foreldrepenger.fpmock2.testmodell.repo;

import no.nav.foreldrepenger.fpmock2.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelse;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.fpmock2.testmodell.virksomhet.ScenarioVirksomheter;

public class TestscenarioImpl implements Testscenario {

    private final String templateNavn;

    /** identity cache for dette scenario. Medfører at identer kan genereres dynamisk basert på lokal id referanse i scenarioet. */
    private final LokalIdentIndeks identer;

    private AdresseIndeks adresseIndeks;

    private Personopplysninger personopplysninger;
    
    private InntektYtelse inntektYtelse = new InntektYtelse();

    private ScenarioVirksomheter scenarioVirksomheter;

    private String unikScenarioId;

    @SuppressWarnings("unused")
    private TestscenarioRepository scenarioIndeks;

    public TestscenarioImpl(String templateNavn, String unikScenarioId, TestscenarioRepository scenarioIndeks) {
        this.templateNavn = templateNavn;
        this.unikScenarioId = unikScenarioId;
        this.scenarioIndeks = scenarioIndeks;
        
        this.scenarioVirksomheter = new ScenarioVirksomheter(this.templateNavn, scenarioIndeks.getBasisdata().getVirksomhetIndeks());
        
        this.identer = scenarioIndeks.getIdenter(getUnikScenarioId());
        this.inntektYtelse.setIdenter(this.identer);
    }

    @Override
    public String getTemplateNavn() {
        return templateNavn;
    }
    
    @Override
    public String getUnikScenarioId() {
        return unikScenarioId;
    }

    @Override
    public LokalIdentIndeks getIdenter() {
        return identer;
    }

    @Override
    public AdresseIndeks getAdresseIndeks() {
        return adresseIndeks;
    }

    public void setAdresseIndeks(AdresseIndeks adresseIndeks) {
        this.adresseIndeks = adresseIndeks;
    }

    public void setPersonopplysninger(Personopplysninger personopplysninger) {
        this.personopplysninger = personopplysninger;
        personopplysninger.setIdenter(identer);
    }

    @Override
    public Personopplysninger getPersonopplysninger() {
        return this.personopplysninger;
    }

    @Override
    public InntektYtelse getInntektYtelse() {
        return this.inntektYtelse;
    }
    
    public void leggTil(InntektYtelseModell iyModell) {
        this.inntektYtelse.leggTil(iyModell);
    }

    @Override
    public ScenarioVirksomheter getVirksomheter() {
        return scenarioVirksomheter;
    }
}
