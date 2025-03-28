package no.nav.foreldrepenger.vtp.server.api.scenario.mapper;

import no.nav.foreldrepenger.vtp.kontrakter.v2.PermisjonDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.Permisjonstype;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjon;

import java.util.List;

import static no.nav.foreldrepenger.fpwsproxy.UtilKlasse.safeStream;

class PermisjonMapper {

    private PermisjonMapper() {
        // sonar
    }

    public static List<Permisjon> tilPermisjoner(List<PermisjonDto> permisjoner) {
        return safeStream(permisjoner)
                .map(PermisjonMapper::tilPermisjon)
                .toList();
    }

    private static Permisjon tilPermisjon(PermisjonDto permisjon) {
        return new Permisjon(
                permisjon.stillingsprosent(),
                permisjon.fomGyldighetsperiode(),
                permisjon.tomGyldighetsperiode(),
                tilPermisjontype(permisjon.permisjonstype())
        );
    }

    private static no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjonstype tilPermisjontype(
            Permisjonstype permisjonstype) {
        return switch (permisjonstype) {
            case PERMISJON -> no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjonstype.PERMISJON;
            case PERMISJON_MED_FORELDREPENGER -> no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjonstype.PERMISJON_MED_FORELDREPENGER;
            case PERMISJON_VED_MILITÆRTJENESTE -> no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjonstype.PERMISJON_VED_MILITÆRTJENESTE;
            case PERMITTERING -> no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjonstype.PERMITTERING;
            case UTDANNINGSPERMISJON -> no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjonstype.UTDANNINGSPERMISJON;
            case UTDANNINGSPERMISJON_IKKE_LOVFESTET -> no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjonstype.UTDANNINGSPERMISJON_IKKE_LOVFESTET;
            case UTDANNINGSPERMISJON_LOVFESTET -> no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjonstype.UTDANNINGSPERMISJON_LOVFESTET;
            case VELFERDSPERMISJON -> no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjonstype.VELFERDSPERMISJON;
            case ANNEN_PERMISJON_IKKE_LOVFESTET -> no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjonstype.ANNEN_PERMISJON_IKKE_LOVFESTET;
            case ANNEN_PERMISJON_LOVFESTET -> no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjonstype.ANNEN_PERMISJON_LOVFESTET;
        };
    }
}
