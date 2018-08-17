package no.nav.foreldrepenger.fpmock2.testmodell;

import java.util.Collection;

public class Scenarios {

    private static Scenarios instance;
    
    private ScenarioMapper loader;
    
    public Scenarios() {
        // TODO Auto-generated constructor stub
    }
    static synchronized Scenarios getInstance() {
        if(instance==null) {
            instance=new Scenarios();
            instance.loadScenarios();
        }
        return instance;
    }
    
    public static Repository getRepository() {
        return new Repository(getInstance().loader);
    }
    
    
    public static Collection<Scenario> scenarios(){
        return getInstance().loader.scenarios();
    }
    
    private void loadScenarios() {
        instance.loader = new ScenarioMapper();
        instance.loader.load();
    }
}
