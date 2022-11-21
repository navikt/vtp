package no.nav.foreldrepenger.fpwsproxy;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class UtilKlasse {

    private UtilKlasse() {
    }

    public static <T> Stream<T> safeStream(List<T> list) {
        return ((List) Optional.ofNullable(list).orElseGet(List::of)).stream();
    }
}
