package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PersonIndeks {

    private Map<String, BrukerModell> byIdent = new HashMap<>();
    private Map<String, Personopplysninger> byIdentPersonopplysninger = new HashMap<>();


    public PersonIndeks() {
    }

    public synchronized void indekserPersonopplysningerByIdent(Personopplysninger pers) {
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

    public synchronized void leggTil(SøkerModell kopierFra, BrukerModell leggTilBruker) {
        leggTilAdressePåBrukerHvisIkkeSatt(kopierFra, leggTilBruker);
        leggTil(leggTilBruker);
    }

    public synchronized void leggTil(BrukerModell bruker) {
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

    public synchronized void indekserFamilierelasjonBrukere(SøkerModell søkerModell, Collection<FamilierelasjonModell> familierelasjoner) {
        for (FamilierelasjonModell fr : familierelasjoner) {
            leggTilAdressePåBrukerHvisIkkeSatt(søkerModell, fr.getTil());
            leggTil(fr.getTil());
        }
    }

    private void leggTilAdressePåBrukerHvisIkkeSatt(SøkerModell fraSøker, BrukerModell tilBruker) {
        var personModell = (PersonModell) tilBruker;
        if (personModell.getAdresser().isEmpty()) {
            personModell.setAdresser(fraSøker.getAdresser());
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized  <V extends BrukerModell> V finnByIdent(String ident) {
        if(byIdent.containsKey(ident)) {
            return (V) byIdent.get(ident);
        } else {
            throw new IllegalArgumentException("Finner ikke bruker med ident: "+ ident);
        }
    }

    public synchronized  <V extends BrukerModell> V finnByAktørIdent(String ident) {
        if(byIdent.containsKey(ident)){
            return (V) byIdent.get(ident);
        } else {
            throw new IllegalArgumentException("Finner ikke bruker med AktørId: "+ident);
        }

    }

    public synchronized Personopplysninger finnPersonopplysningerByIdent(String ident) {
        return byIdentPersonopplysninger.get(ident);
    }

    public synchronized Personopplysninger finnPersonopplysningerByAktørIdent(String ident) {
        return byIdentPersonopplysninger.get(ident);
    }

    public synchronized Set<Personopplysninger> getAlleSøkere(){
        return byIdentPersonopplysninger.values().stream().filter(p -> p.getSøker()!=null).collect(Collectors.toSet());
    }

    public synchronized Set<Personopplysninger> getAlleAnnenPart(){
        return byIdentPersonopplysninger.values().stream().filter(p -> p.getAnnenPart()!=null).collect(Collectors.toSet());
    }
}
