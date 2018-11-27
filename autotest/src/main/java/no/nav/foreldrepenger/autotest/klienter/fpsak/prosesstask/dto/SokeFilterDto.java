package no.nav.foreldrepenger.autotest.klienter.fpsak.prosesstask.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SokeFilterDto {
    protected List<ProsessTaskStatusDto> prosessTaskStatuser;
    protected LocalDateTime sisteKjoeretidspunktFraOgMed;
    protected LocalDateTime sisteKjoeretidspunktTilOgMed;
    
    public SokeFilterDto setSisteKjoeretidspunktFraOgMed(LocalDateTime tid) {
        this.sisteKjoeretidspunktTilOgMed = tid;
        return this;
    }
}
