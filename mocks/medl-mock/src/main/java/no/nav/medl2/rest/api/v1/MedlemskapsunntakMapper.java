package no.nav.medl2.rest.api.v1;

import java.util.List;

import no.nav.vtp.person.Person;
import no.nav.vtp.person.personopplysninger.Medlemskap;

public class MedlemskapsunntakMapper {

    private MedlemskapsunntakMapper() {
        /* This utility class should not be instantiated */
    }

    public static List<Medlemskapsunntak> tilMedlemskapsunntak(Person person) {
        var medlemskapsperioder = person.personopplysninger().medlemskap();
        if (medlemskapsperioder == null) {
            return List.of();
        }
        return medlemskapsperioder.stream()
                .map(MedlemskapsunntakMapper::tilMedlemsperiode)
                .toList();
    }

    private static Medlemskapsunntak tilMedlemsperiode(Medlemskap medlemskap) {
        return new Medlemskapsunntak(
                genererId(medlemskap),
                medlemskap.fom(),
                medlemskap.tom(),
                tilDekningstype(medlemskap),
                null,
                "ENDL",
                medlemskap.land().getAlpha3(),
                null,
                true,
                new Medlemskapsunntak.Sporingsinformasjon(medlemskap.fom(), "ANNEN"),
                null);
    }

    private static String tilDekningstype(Medlemskap medlemskap) {
        return switch (medlemskap.trygdedekning()) {
            case IHT_AVTALE -> "IHT_Avtale";
            case FULL -> "Full";
        };
    }

    private static long genererId(Medlemskap medlemskap) {
        var key = "" + medlemskap.fom() + medlemskap.tom() + medlemskap.land();
        return 100000000L + Math.abs(key.hashCode() % 100_000_000L);
    }

}
