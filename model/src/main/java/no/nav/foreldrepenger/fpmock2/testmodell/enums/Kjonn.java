package no.nav.foreldrepenger.fpmock2.testmodell.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Kjonn {
    MANN ("M"),
    KVINNE ("K");

    private String tpsName;

    Kjonn(String tpsName){
        this.tpsName = tpsName;
    }

    private static final List<Kjonn> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Kjonn randomKjonn() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public String getTpsName(){
        return this.tpsName;
    }




}
