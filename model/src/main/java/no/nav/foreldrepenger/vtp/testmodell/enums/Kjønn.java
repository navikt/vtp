package no.nav.foreldrepenger.vtp.testmodell.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Kjønn {
    MANN,
    KVINNE;

    private static final List<Kjønn> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Kjønn randomKjonn() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
