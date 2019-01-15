package no.nav.foreldrepenger.fpmock2.felles;

import java.util.HashMap;

public class ExpectPredicate extends HashMap<String, String>{
    
    public ExpectPredicate(String key, String value) {
        this.put(key, value);
    }
    
    public ExpectPredicate() {
        
    }
    
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

}
