package no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HistorikkInnslag {
    /** Historikkinnslag type. */
    public enum Type {
        REVURD_OPPR,
        BEH_VENT,
        BREV_BESTILT,
        BREV_SENT,
        VEDLEGG_MOTTATT,
        VEDTAK_FATTET,
        SPOLT_TILBAKE,
        NYE_REGOPPLYSNINGER,
        UENDRET_UTFALL;

        public String getKode() {
            return name();
        }
    }
    public static final Type REVURD_OPPR = Type.REVURD_OPPR;
    public static final Type BEH_VENT = Type.BEH_VENT;
    public static final Type BREV_BESTILT = Type.BREV_BESTILT;
    public static final Type BREV_SENDT = Type.BREV_SENT;
    public static final Type VEDLEGG_MOTTATT = Type.VEDLEGG_MOTTATT;
    public static final Type VEDTAK_FATTET = Type.VEDTAK_FATTET;
    public static final Type BEHANDLINGEN_ER_FLYTTET = Type.SPOLT_TILBAKE;
    public static final Type NYE_REGOPPLYSNINGER = Type.NYE_REGOPPLYSNINGER;
    public static final Type UENDRET_UTFALL = Type.UENDRET_UTFALL;
    
    
    protected HistorikkTekst tekst;
    protected List<HistorikkinnslagDel> historikkinnslagDeler;
    
    protected int behandlingsid;
    protected Kode type;
    protected Kode aktoer;
    protected Kode kjoenn;
    
    public String getTypeKode() {
        return type.kode;
    }
}
