package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.opptjening;

import java.time.LocalDate;

public class OpptjeningAktivitet {

    public LocalDate originalFom;
    public LocalDate originalTom;
    
    public LocalDate opptjeningFom;
    public LocalDate opptjeningTom;
    
    public boolean erGodkjent;
    public boolean erManueltOpprettet;
    
    public String begrunnelse;
    
}
