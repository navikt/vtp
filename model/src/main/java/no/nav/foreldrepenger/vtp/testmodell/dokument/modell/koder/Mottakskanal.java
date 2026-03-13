package no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder;

/*
    reelt en kopi av no.nav.saf.Kanal
*/

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Mottakskanal {
    ALTINN,
    EESSI,
    EIA,
    EKST_OPPS,
    LOKAL_UTSKRIFT,
    NAV_NO,
    SENTRAL_UTSKRIFT,
    SDP,
    SKAN_NETS,
    SKAN_PEN,
    SKAN_IM,
    TRYGDERETTEN,
    HELSENETTET,
    INGEN_DISTRIBUSJON,
    NAV_NO_UINNLOGGET,
    INNSENDT_NAV_ANSATT,
    NAV_NO_CHAT,
    NAV_NO_UTEN_VARSLING,
    DPVT,
    DPO,
    E_POST,
    ALTINN_INNBOKS,
    HR_SYSTEM_API,
    @JsonEnumDefaultValue UKJENT
}
