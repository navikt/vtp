package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InntektYtelseIndeks {

    private Map<String, InntektYtelseModell> byIdent = new HashMap<>();
    
    public Optional<InntektYtelseModell> getModellForIdent(String ident) {
        return Optional.ofNullable(byIdent.get(ident));
    }

    public void leggTil(String ident, InntektYtelseModell iy) {
        byIdent.put(ident, iy);
    }

}
