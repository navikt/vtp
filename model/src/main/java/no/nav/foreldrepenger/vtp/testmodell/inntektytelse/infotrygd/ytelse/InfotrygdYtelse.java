package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.ytelse;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.InfotrygdBehandlingstema;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.InfotrygdSakResultat;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.InfotrygdSakStatus;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.InfotrygdSakType;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.InfotrygdTema;

/**
 * @see https://confluence.adeo.no/pages/viewpage.action?pageId=220537850 for ytterligere dokumentasjon av denne strukturen.
 */
public record InfotrygdYtelse(String sakId,
                              LocalDateTime registrert,
                              LocalDateTime vedtatt,
                              LocalDateTime iverksatt,
                              LocalDateTime endret,
                              LocalDateTime opphør,
                              String saksbehandlerId,
                              InfotrygdTema tema,
                              InfotrygdBehandlingstema behandlingstema,
                              @JsonProperty("type") InfotrygdSakType sakType,
                              @JsonProperty("status") InfotrygdSakStatus sakStatus,
                              InfotrygdSakResultat resultat) {
}
