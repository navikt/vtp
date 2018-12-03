package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.opptjening;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpptjeningAktivitet {

    protected Kode aktivitetType;
    protected LocalDate originalFom;
    protected LocalDate originalTom;
    protected LocalDate opptjeningFom;
    protected LocalDate opptjeningTom;
    protected boolean erGodkjent;
    protected String begrunnelse;
    protected boolean erManueltOpprettet;

    public LocalDate getOpptjeningFom() {
        return opptjeningFom;
    }

    public LocalDate getOpptjeningTom() {
        return opptjeningTom;
    }

    public void setOriginalFom(LocalDate fom) {
        originalFom = fom;
    }

    public void setOriginalTom(LocalDate tom) {
        originalTom = tom;
    }

    public boolean isErManueltOpprettet() {
        return erManueltOpprettet;
    }

    public void setErManueltOpprettet(boolean erManueltOpprettet) {
        this.erManueltOpprettet = erManueltOpprettet;
    }

    public void vurder(boolean erGodkjent, String begrunnelse, boolean erManueltOpprettet) {
        this.erGodkjent = erGodkjent;
        this.begrunnelse = begrunnelse;
        this.setErManueltOpprettet(erManueltOpprettet);
    }

}
