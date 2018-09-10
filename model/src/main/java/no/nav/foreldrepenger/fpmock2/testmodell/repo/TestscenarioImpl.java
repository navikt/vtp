package no.nav.foreldrepenger.fpmock2.testmodell.repo;

import no.nav.foreldrepenger.fpmock2.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelse;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.fpmock2.testmodell.util.VariabelContainer;
import no.nav.foreldrepenger.fpmock2.testmodell.virksomhet.ScenarioVirksomheter;

public class TestscenarioImpl implements Testscenario {

    private final String templateNavn;

    /** identity cache for dette scenario. Medfører at identer kan genereres dynamisk basert på lokal id referanse i scenarioet. */
    private final LokalIdentIndeks identer;

    private AdresseIndeks adresseIndeks;

    private Personopplysninger personopplysninger;
    
    private InntektYtelse inntektYtelse = new InntektYtelse();

    private ScenarioVirksomheter scenarioVirksomheter;

    /** Unik testscenario id. */
    private String id;
    
    private VariabelContainer vars = new VariabelContainer();

    @SuppressWarnings("unused")
    private TestscenarioBuilderRepository scenarioIndeks;

    public TestscenarioImpl(String templateNavn, String id, TestscenarioBuilderRepository scenarioIndeks) {
        this.templateNavn = templateNavn;
        this.id = id;
        this.scenarioIndeks = scenarioIndeks;
        
        this.scenarioVirksomheter = new ScenarioVirksomheter(this.templateNavn, scenarioIndeks.getBasisdata().getVirksomhetIndeks());
        
        this.identer = scenarioIndeks.getIdenter(getId());
    }

    @Override
    public String getTemplateNavn() {
        return templateNavn;
    }
    
    @Override
    public String getId() {
        return id;
    }

    public LokalIdentIndeks getIdenter() {
        return identer;
    }

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

    public ScenarioVirksomheter getVirksomheter() {
        return scenarioVirksomheter;
    }
    
    @Override
    public VariabelContainer getVariabelContainer() {
        return vars;
    }
}
