package no.nav.foreldrepenger.vtp.testmodell.enheter;

import java.util.*;

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

    public Optional<Norg2Modell> finnByEnhetId(String enhetId) {
        return byDiskresjonskode.values()
                .stream()
                .filter(enhet -> Objects.equals(enhetId, enhet.getEnhetId()))
                .findFirst();
    }
}
