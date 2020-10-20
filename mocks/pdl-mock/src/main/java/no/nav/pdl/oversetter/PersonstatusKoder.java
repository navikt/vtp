package no.nav.pdl.oversetter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonstatusKoder {
    private static final Map<String, List<String>> PERSONSTATUSER;

    static {
        final Map<String, List<String>> personstatuser = new HashMap<>();
        personstatuser.put("BOSA", List.of("bosattEtterFolkeregisterloven", "bosatt"));
        personstatuser.put("DØD", List.of("doedIFolkeregisteret", "doed"));
        personstatuser.put("DØDD", List.of("doedIFolkeregisteret", "doed"));
        personstatuser.put("UTPE", List.of("opphoert", "opphoert"));
        personstatuser.put("ADNR", List.of("dNummer", "inaktiv"));
        personstatuser.put("FOSV", List.of("forsvunnet", "forsvunnet"));
        personstatuser.put("UTVA", List.of("ikkeBosatt", "utflyttet"));
        personstatuser.put("UREG", List.of("ikkeBosatt", "ikkeBosatt"));
        personstatuser.put("UTAN", List.of("ikkeBosatt", "ikkeBosatt"));
        personstatuser.put("FØDR", List.of("ikkeBosatt", "foedselsregistrert"));
        PERSONSTATUSER = Collections.unmodifiableMap(personstatuser);
    }

    public static List<String> hentPersonstatusPDL(String personstatusTPS) {
        return PERSONSTATUSER.get(personstatusTPS);
    }

}
