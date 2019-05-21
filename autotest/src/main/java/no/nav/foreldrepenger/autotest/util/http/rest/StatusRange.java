package no.nav.foreldrepenger.autotest.util.http.rest;

public class StatusRange {

    //STATUS SUCCESS
    public static final StatusRange STATUS_SUCCESS = new StatusRange(200, 299);
    
    //STATUS REDIRECT
    public static final StatusRange STATUS_REDIRECT = new StatusRange(300, 399);
    
    //STATUS CLIENT ERROR
    public static final StatusRange STATUS_CLIENT_ERROR = new StatusRange(400, 499);
    
    //STATUS SERVER ERROR
    public static final StatusRange STATUS_SERVER_ERROR = new StatusRange(500, 599);
    
    //STATUS COMBINE
    public static final StatusRange STATUS_NO_SERVER_ERROR = new StatusRange(200, 499);
    public static final StatusRange STATUS_200 = new StatusRange(200, 200);
    
    
    public int min;
    public int max;
    
    public StatusRange(int min, int max) {
        super();
        this.min = min;
        this.max = max;
    }
    
    public boolean inRange(int number) {
        return (number >= min && number <= max);
    }
    
}
