package no.nav.medl2.rest.api.v1;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import no.nav.foreldrepenger.vtp.testmodell.medlemskap.LovvalgType;
import no.nav.vtp.person.Person;
import no.nav.vtp.person.personopplysninger.Medlemskap;

public class MedlemskapsunntakMapper {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(100000000);

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
                ID_GENERATOR.getAndIncrement(),
                medlemskap.fom(),
                medlemskap.tom(),
                tilDekningstype(medlemskap),
                null,
                LovvalgType.ENDL.name(),
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

}
