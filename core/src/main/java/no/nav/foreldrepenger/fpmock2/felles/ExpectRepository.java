package no.nav.foreldrepenger.fpmock2.felles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpectRepository {
    
    public enum Mock {
        ARBEIDSFORHOLD, ARENA, DOKUMENTPRODUKSJON,
        GSAK, INFOTRYGDFEED, INNFOTRYGD, INNTEKT,
        JOARK, KODEVERK, LDAP, MEDL, NORG2, ORGANISASJON,
        PERSONFEED, SIGRUN, TPS,
    }
    
    private static Map<String, List<TokenEntry>> repos = new HashMap<>();
    
    private static final Logger LOG = LoggerFactory.getLogger(ExpectRepository.class);
    
    public static void hit(Mock mock, String webMethod, ExpectPredicate predicate) {
        List<TokenEntry> tokens = getTokenList(mock, webMethod);
        
        for (TokenEntry tokenEntry : tokens) {
            if(predicate == null || predicate.entrySet().containsAll(tokenEntry.predicate.entrySet())) {
                tokenEntry.hit();
            }
        }
    }

    public static void registerToken(Mock mock, String webMethod, String token, ExpectPredicate predicate) {
        List<TokenEntry> tokens = getTokenList(mock, webMethod);
        TokenEntry tokenEntry = new TokenEntry(token, predicate);
        tokens.add(tokenEntry);
        cleanupTokens(tokens);
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
    
    private static void cleanupTokens(List<TokenEntry> tokens) {
        for(int i = tokens.size() - 1; i >= 0; i--) {
            TokenEntry token = tokens.get(i);
            if(token.hasExpired()) {
                tokens.remove(i);
            }
        }
    }
    
    private static List<TokenEntry> getTokenList(Mock mock, String webMethod){
        String key = mock.toString() + "_" + webMethod;
        if(null == repos.get(key)) {
            repos.put(key, new ArrayList<>());
        }
        return repos.get(key);
    }
    
    private static class TokenEntry{
        
        public String token;
        public Map<String, String> predicate;
        public LocalDateTime expires = LocalDateTime.now().plusMinutes(5);
        public boolean isHit = false;
        
        public TokenEntry(String token, ExpectPredicate predicate) {
            super();
            this.token = token;
            this.predicate = predicate;
        }
        
        public void hit() {
            isHit = true;
        }
        
        public boolean hasExpired() {
            return LocalDateTime.now().isAfter(expires);
        }
    }
}
