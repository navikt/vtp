package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.papirsøknad;

import java.util.ArrayList;
import java.util.List;

public class FordelingDto {

    public List<PermisjonPeriodeDto> permisjonsPerioder = new ArrayList<>();

    public boolean sokerHarAleneomsorg = false;

    public boolean denAndreForelderenHarRettPaForeldrepenger = true;
}
