package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.papirsoknad;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.papirsøknad.AnnenForelderDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.papirsøknad.DekningsgradDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.papirsøknad.EgenVirksomhetDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.papirsøknad.FordelingDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.papirsøknad.FrilansDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5040")
public class PapirSoknadForeldrepengerBekreftelse extends AksjonspunktBekreftelse {

    protected String tema = "FODSL";

    protected String soknadstype = "FP";

    protected String soker = "MOR";

    protected boolean erBarnetFodt = true;

    protected Integer antallBarn = 1;

    protected List<LocalDate> foedselsDato =  Collections.singletonList(LocalDate.now().minusDays(1));

    protected LocalDate mottattDato = LocalDate.now().minusDays(10);

    protected boolean ufullstendigSoeknad;

    protected DekningsgradDto dekningsgrad = DekningsgradDto.HUNDRE;

    protected EgenVirksomhetDto egenVirksomhet = new EgenVirksomhetDto();

    protected FordelingDto tidsromPermisjon = new FordelingDto();

    protected FrilansDto frilans = new FrilansDto();

    protected AnnenForelderDto annenForelder = new AnnenForelderDto();

    protected boolean annenForelderInformert = true;

    public PapirSoknadForeldrepengerBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }

    public void morSøkerFødsel(FordelingDto fordeling, LocalDate fødselsdato, LocalDate mottattDato) {
        this.foedselsDato = Collections.singletonList(fødselsdato);
        this.mottattDato = mottattDato.minusWeeks(3);
        this.tidsromPermisjon = fordeling;
    }
}
