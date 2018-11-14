package no.nav.foreldrepenger.autotest.klienter.fpsak.prosesstask.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProsessTaskStatusDto {

    protected String prosessTaskStatusName;
}
