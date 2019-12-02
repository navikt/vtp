package no.nav.oppgave;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static no.nav.oppgave.Oppgavestatuskategori.AAPEN;

public enum Oppgavestatus {
    OPPRETTET,
    AAPNET,
    UNDER_BEHANDLING,
    FERDIGSTILT,
    FEILREGISTRERT;

    public static Long getIdFor(Oppgavestatus status) {
        switch (status) {
            case OPPRETTET: return 1L;
            case AAPNET: return 2L;
            case UNDER_BEHANDLING: return 3L;
            case FERDIGSTILT: return 4L;
            case FEILREGISTRERT: return 5L;
            default: return null;
        }
    }

    public static List<Long> getIdsFor(Oppgavestatuskategori statuskategori) {
        if (Objects.equals(AAPEN, statuskategori)) {
            return Arrays.asList(getIdFor(OPPRETTET), getIdFor(AAPNET), getIdFor(UNDER_BEHANDLING));
        } else {
            return Arrays.asList(getIdFor(FERDIGSTILT), getIdFor(FEILREGISTRERT));
        }
    }

    public static List<Oppgavestatus> avsluttet() {
        return Arrays.asList(FERDIGSTILT, FEILREGISTRERT);
    }

    public static List<Oppgavestatus> aapen() {
        return Arrays.asList(OPPRETTET, AAPNET, UNDER_BEHANDLING);
    }
}
