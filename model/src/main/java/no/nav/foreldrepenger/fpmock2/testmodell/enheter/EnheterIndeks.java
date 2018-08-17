package no.nav.foreldrepenger.fpmock2.testmodell.enheter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EnheterIndeks {

    private Map<String, Norg2Modell> byDiskresjonskode = new HashMap<>();
    
    public void leggTil(Collection<Norg2Modell> enheter) {
        enheter.forEach(m -> byDiskresjonskode.putIfAbsent(m.getDiskresjonskode(), m));
    }

    public Norg2Modell finnByDiskresjonskode(String diskresjonskode) {
       return byDiskresjonskode.get(diskresjonskode);
    }

    public Collection<Norg2Modell> getAlleEnheter() {
        return Collections.unmodifiableCollection(byDiskresjonskode.values());
    }
}
