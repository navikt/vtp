package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InntektYtelseIndeks {

    private Map<String, InntektYtelseModell> byIdent = new ConcurrentHashMap<>();
    
    public Optional<InntektYtelseModell> getModellForIdent(String ident) {
        return Optional.ofNullable(byIdent.get(ident));
    }

    public void leggTil(String ident, InntektYtelseModell iy) {
        if(ident != null && iy != null){
            byIdent.put(ident, iy);
        }
    }

}
