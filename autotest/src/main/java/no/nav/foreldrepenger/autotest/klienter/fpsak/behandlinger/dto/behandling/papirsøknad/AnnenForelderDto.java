package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.papirsøknad;

public class AnnenForelderDto {

    public boolean kanIkkeOppgiAnnenForelder = true;

    public KanIkkeOppgiBegrunnelse kanIkkeOppgiBegrunnelse = new KanIkkeOppgiBegrunnelse();

    public boolean sokerHarAleneomsorg = false;

    public boolean denAndreForelderenHarRettPaForeldrepenger = true;

    public static class KanIkkeOppgiBegrunnelse {

        public String arsak = "UKJENT_FORELDER";
    }
}
