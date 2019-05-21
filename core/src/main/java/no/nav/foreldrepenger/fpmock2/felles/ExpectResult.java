package no.nav.foreldrepenger.fpmock2.felles;

public class ExpectResult {

    protected boolean isHit;
    protected String data;
    
    
    public ExpectResult(boolean isHit, String data) {
        super();
        this.isHit = isHit;
        this.data = data;
    }


    public boolean isHit() {
        return isHit;
    }


    public String getData() {
        return data;
    }
    
    
}
