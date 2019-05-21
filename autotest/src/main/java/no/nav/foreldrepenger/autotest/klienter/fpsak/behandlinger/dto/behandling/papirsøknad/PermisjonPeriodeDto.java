package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.papirsøknad;

import java.time.LocalDate;

public class PermisjonPeriodeDto {

    public LocalDate periodeFom;

    public LocalDate periodeTom;

    public String periodeType;

    public PermisjonPeriodeDto(String periodeType, LocalDate fom, LocalDate tom) {
        this.periodeType = periodeType;
        this.periodeFom = fom;
        this.periodeTom = tom;
    }
}
