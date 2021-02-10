package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PersonIndeks {

    private Map<String, BrukerModell> byIdent = new ConcurrentHashMap<>();
    private Map<String, Personopplysninger> byIdentPersonopplysninger = new ConcurrentHashMap<>();

    public void indekserPersonopplysningerByIdent(Personopplysninger pers) {
        if (pers.getSøker() != null) {
            byIdentPersonopplysninger.putIfAbsent(pers.getSøker().getIdent(), pers);
            byIdentPersonopplysninger.putIfAbsent(pers.getSøker().getAktørIdent(), pers);
        }

        if (pers.getAnnenPart() != null) {
            byIdentPersonopplysninger.putIfAbsent(pers.getAnnenPart().getIdent(), pers);
            byIdentPersonopplysninger.putIfAbsent(pers.getAnnenPart().getAktørIdent(), pers);
        }

        for (FamilierelasjonModell fr : pers.getFamilierelasjoner()) {
            byIdentPersonopplysninger.putIfAbsent(fr.getTil().getIdent(), pers);
            byIdentPersonopplysninger.putIfAbsent(fr.getTil().getAktørIdent(), pers);
        }
    }

    public void leggTil(BrukerModell bruker) {
        if (bruker == null) {
            // quiet escape
            return;
        }
        String ident = bruker.getIdent();
        String aktørIdent = bruker.getAktørIdent();
        if (bruker instanceof BrukerIdent && ident == null) {
            // skip - er BrukerIdent, venter på full bruker
            return;
        }

        if (byIdent.get(ident) instanceof BrukerIdent) {
            // overskriv
            byIdent.put(ident, bruker);
            byIdent.put(aktørIdent, bruker);
        }

        byIdent.putIfAbsent(ident, bruker);
        byIdent.putIfAbsent(aktørIdent, bruker);
    }

    public void indekserFamilierelasjonBrukere(Collection<FamilierelasjonModell> familierelasjoner) {
        for (FamilierelasjonModell fr : familierelasjoner) {
            leggTil(fr.getTil());
        }
    }

    @SuppressWarnings("unchecked")
    public <V extends BrukerModell> V finnByIdent(String ident) {
        Objects.requireNonNull(ident,"ident");
        if(byIdent.containsKey(ident)) {
            return (V) byIdent.get(ident);
        } else {
            throw new IllegalArgumentException("Finner ikke bruker med ident: "+ ident);
        }
    }

    public <V extends BrukerModell> V finnByAktørIdent(String ident) {
        Objects.requireNonNull(ident,"ident");
        if(byIdent.containsKey(ident)){
            return (V) byIdent.get(ident);
        } else {
            throw new IllegalArgumentException("Finner ikke bruker med AktørId: "+ident);
        }
    }

    public Personopplysninger finnPersonopplysningerByIdent(String ident) {
        Objects.requireNonNull(ident,"ident");
        return byIdentPersonopplysninger.get(ident);
    }

    public Personopplysninger finnPersonopplysningerByAktørIdent(String ident) {
        Objects.requireNonNull(ident,"ident");
        return byIdentPersonopplysninger.get(ident);
    }

    public Set<Personopplysninger> getAlleSøkere(){
        return byIdentPersonopplysninger.values().stream().filter(p -> p.getSøker()!=null).collect(Collectors.toSet());
    }

    public Set<Personopplysninger> getAlleAnnenPart(){
        return byIdentPersonopplysninger.values().stream().filter(p -> p.getAnnenPart()!=null).collect(Collectors.toSet());
    }
}
