package no.nav.foreldrepenger.vtp.testmodell.enheter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnheterIndeks {

    private Map<String, Norg2Modell> byDiskresjonskode = new ConcurrentHashMap<>();

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
