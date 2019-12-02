package no.nav.oppgave.infrastruktur.validering.rest;

import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

public class ErrorResponse {
    private final String uuid;
    private final String feilmelding;

    public ErrorResponse(String uuid, String feilmelding) {
        this.uuid = uuid;
        this.feilmelding = feilmelding;
    }

    public String getFeilmelding() {
        return feilmelding;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
            .append("uuid", uuid)
            .append("feilmelding", feilmelding)
            .toString();
    }
}
