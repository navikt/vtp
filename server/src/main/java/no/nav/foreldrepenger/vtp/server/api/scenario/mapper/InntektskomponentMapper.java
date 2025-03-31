package no.nav.foreldrepenger.vtp.server.api.scenario.mapper;

import no.nav.foreldrepenger.vtp.kontrakter.v2.*;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.*;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektYtelseType;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonArbeidsgiver;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static no.nav.foreldrepenger.fpwsproxy.UtilKlasse.safeStream;

class InntektskomponentMapper {
    private InntektskomponentMapper() {
        // sonar
    }

    private static final String INNTEKTPERIODE_BESKRIVELSE = "fastloenn";

    public static InntektskomponentModell tilInntektkomponenten(InntektkomponentDto inntektskomponent, Map<UUID, BrukerModell> allePersoner) {
        if (inntektskomponent == null) {
            return new InntektskomponentModell();
        }
        return new InntektskomponentModell(tilInntektsperioder(inntektskomponent.inntektsperioder(), allePersoner), List.of());
    }

    private static List<Inntektsperiode> tilInntektsperioder(List<InntektsperiodeDto> inntektsperioder, Map<UUID, BrukerModell> allePersoner) {
        return safeStream(inntektsperioder)
                .map(i -> tilInntektsperiode(i, allePersoner))
                .toList();
    }

    private static Inntektsperiode tilInntektsperiode(InntektsperiodeDto i, Map<UUID, BrukerModell> allePersoner) {
        var inntektYtelseType = i.inntektYtelseType() != null ? InntektYtelseType.valueOf(i.inntektYtelseType().name()) : null;
        return new Inntektsperiode(i.fom(), i.tom(), null, i.beløp(), tilOrgnummer(i.arbeidsgiver()), tilInntektType(i),
                tilInntektFordel(i.inntektFordel()), INNTEKTPERIODE_BESKRIVELSE, inntektYtelseType, null, null,
                null,
                tilPrivatArbeidgiver(i.arbeidsgiver(), allePersoner));
    }

    public static String tilOrgnummer(Arbeidsgiver arbeidsgiver) {
        if (arbeidsgiver instanceof OrganisasjonDto org) {
            return org.orgnummer().value();
        }
        return null;
    }

    public static PersonArbeidsgiver tilPrivatArbeidgiver(Arbeidsgiver arbeidsgiver, Map<UUID, BrukerModell> allePersoner) {
        if (arbeidsgiver instanceof PrivatArbeidsgiver privatArbeidsgiver) {
            return (PersonArbeidsgiver) allePersoner.get(privatArbeidsgiver.uuid());
        }
        return null;
    }

    private static InntektFordel tilInntektFordel(InntektsperiodeDto.InntektFordelDto inntektFordelDto) {
        return switch (inntektFordelDto) {
            case KONTANTYTELSE -> InntektFordel.KONTANTYTELSE;
            case UTGIFTSGODTGJØRELSE -> InntektFordel.UTGIFTSGODTGJØRELSE;
            case NATURALYTELSE -> InntektFordel.NATURALYTELSE;
        };
    }

    private static InntektType tilInntektType(InntektsperiodeDto inntektsperiodeDto) {
        if (inntektsperiodeDto.inntektType() != null) {
            return switch (inntektsperiodeDto.inntektType()) {
                case LØNNSINNTEKT -> InntektType.LØNNSINNTEKT;
                case NÆRINGSINNTEKT -> InntektType.NÆRINGSINNTEKT;
                case PENSJON_ELLER_TRYGD -> InntektType.PENSJON_ELLER_TRYGD;
                case YTELSE_FRA_OFFENTLIGE -> InntektType.YTELSE_FRA_OFFENTLIGE;
            };
        } else {
            return InntektType.LØNNSINNTEKT;
        }
    }
}
