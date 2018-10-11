package no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.dto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum EksempelArbeidsgiver {
    STATOILSOKKEL("Statoil ASA avd Sokkelvirksomhet","973861778","973861778","07097521470","22552255"),
    COLORLINE("Color Line Crew AS","973135678","991779434","07097521470","99009900"),
    UNIVERSITETET_I_OSLO("Universitetet i Oslo","974716860","974716860","07097521470","88778877"),
    FORSVARET("Forsvaret","995428563","995428563","07097521470","22552255"),
    STATOILFORSK("Statoil Forskningssenter", "984042442", "984042442", "07097521470","22332233"),
    STORTINGET("Stortinget","874707112","874707112","07097521470","22330000");

    private String orgNavn;
    private String orgNummer;
    private String virkNummer;
    private String kontaktInfoFNR;
    private String kontaktInfoTLF;



    EksempelArbeidsgiver(String orgNavn,
                         String orgNummer,
                         String virkNummer,
                         String kontaktInfoFNR,
                         String kontaktInfoTLF){
        this.orgNavn = orgNavn;
        this.orgNummer = orgNummer;
        this.virkNummer = virkNummer;
        this.kontaktInfoFNR = kontaktInfoFNR;
        this.kontaktInfoTLF = kontaktInfoTLF;
    }

    public String getOrgNavn() {
        return orgNavn;
    }

    public String getOrgNummer() {
        return orgNummer;
    }

    public String getVirkNummer() {
        return virkNummer;
    }

    public String getKontaktInfoFNR() {
        return kontaktInfoFNR;
    }

    public String getKontaktInfoTLF() {
        return kontaktInfoTLF;
    }


    private static final List<EksempelArbeidsgiver> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static EksempelArbeidsgiver getRandomEksempelarbeidsgiver() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
