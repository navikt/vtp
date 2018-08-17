package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PersonIndeks {

    private Map<String, BrukerModell> byIdent = new HashMap<>();
    private Map<String, BrukerModell> byAktørIdent = new HashMap<>();
    private Map<String, Personopplysninger> byIdentPersonopplysninger = new HashMap<>();
    
    public PersonIndeks() {
    }

    public void indekserPersonopplysningerByIdent(Personopplysninger pers) {
        if (pers.getSøker() != null) {
            byIdentPersonopplysninger.putIfAbsent(pers.getSøker().getIdent(), pers);
        }

        if (pers.getAnnenPart() != null) {
            byIdentPersonopplysninger.putIfAbsent(pers.getAnnenPart().getIdent(), pers);
        }

        for (FamilierelasjonModell fr : pers.getFamilierelasjoner()) {
            byIdentPersonopplysninger.putIfAbsent(fr.getTil().getIdent(), pers);
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
            byAktørIdent.put(aktørIdent, bruker);
        }

        byIdent.putIfAbsent(ident, bruker);
        byAktørIdent.putIfAbsent(aktørIdent, bruker);
    }

    public void indekserFamilierelasjonBrukere(Collection<FamilierelasjonModell> familierelasjoner) {
        for (FamilierelasjonModell fr : familierelasjoner) {
            leggTil(fr.getTil());
        }
    }

    @SuppressWarnings("unchecked")
    public <V extends BrukerModell> V finnByIdent(String ident) {
        return (V) byIdent.get(ident);
    }

    @SuppressWarnings("unchecked")
    public <V extends BrukerModell> V finnByAktørIdent(String ident) {
        return (V) byAktørIdent.get(ident);
    }
    
    public Personopplysninger finnPersonopplysningerByIdent(String ident) {
        return byIdentPersonopplysninger.get(ident);
    }
    
    public Set<Personopplysninger> getAlleSøkere(){
        return byIdentPersonopplysninger.values().stream().filter(p -> p.getSøker()!=null).collect(Collectors.toSet());
    }
    
    public Set<Personopplysninger> getAlleAnnenPart(){
        return byIdentPersonopplysninger.values().stream().filter(p -> p.getAnnenPart()!=null).collect(Collectors.toSet());
    }
}
