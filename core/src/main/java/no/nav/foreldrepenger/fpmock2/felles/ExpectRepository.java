package no.nav.foreldrepenger.fpmock2.felles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpectRepository {
    
    public enum Mock {
        ARBEIDSFORHOLD, ARENA, DOKUMENTPRODUKSJON,
        GSAK, INFOTRYGDFEED, INNFOTRYGD, INNTEKT,
        JOARK, KODEVERK, LDAP, MEDL, NORG2, ORGANISASJON,
        PERSONFEED, SIGRUN, TPS,
    }
    
    public static Map<Mock, Map<String, List<TokenEntry>>> repos = initRepos();
    
    public static void hit(Mock mock, String webMethod) {
        List<TokenEntry> tokens = getTokenList(mock, webMethod);
        
        for (TokenEntry tokenEntry : tokens) {
            tokenEntry.hit();
        }
    }

    public static void registerToken(Mock mock, String webMethod, String token) {
        List<TokenEntry> tokens = getTokenList(mock, webMethod);
        TokenEntry tokenEntry = new TokenEntry(token);
        tokens.add(tokenEntry);
    }
    
    public static boolean isHit(Mock mock, String webMethod, String token) {
        List<TokenEntry> tokens = getTokenList(mock, webMethod);
        TokenEntry foundToken = null;
        
        for (TokenEntry tokenEntry : tokens) {
            if(tokenEntry.token.equals(token)) {
                foundToken = tokenEntry;
            }
        }
        
        tokens.remove(foundToken);
        
        return foundToken != null ? foundToken.isHit : false;
    }
    
    public static Map<Mock, Map<String, List<TokenEntry>>> initRepos() {
        Map<Mock, Map<String, List<TokenEntry>>> map = new HashMap<>();
        
        for (Mock mock : Mock.values()) {
            map.put(mock, new HashMap<String, List<TokenEntry>>());
        }
        
        return map;
    }
    
    private static List<TokenEntry> getTokenList(Mock mock, String webMethod){
        if(repos.get(mock).get(webMethod) == null) {
            repos.get(mock).put(webMethod, new ArrayList<>());
        }
        
        return repos.get(mock).get(webMethod);
    }
    
    private static class TokenEntry{
        
        public String token;
        public boolean isHit = false;
        
        public TokenEntry(String token) {
            super();
            this.token = token;
        }
        
        public void hit() {
            isHit = true;
        }
    }
}
