package no.nav.foreldrepenger.fpmock2.server.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class EndUserAuthenticateTemplate {
    static class Name {
        private String name; // NOSONAR
        private String value;

        Name() {
            // må ha default ctor
        }
        
        Name(String name, String value) {
            super();
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    static class Callback {
        private String type;
        private List<Name> input;
        private List<Name> output;

        Callback() {
            // må ha default ctor
        }
        
        Callback(String type, Name output, Name input){
            this.type=type;
            this.input = Arrays.asList(input);
            this.output=Arrays.asList(output);
        }
        
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Name> getInput() {
            return input;
        }

        public void setInput(List<Name> input) {
            this.input = input;
        }

        public List<Name> getOutput() {
            return output;
        }

        public void setOutput(List<Name> output) {
            this.output = output;
        }

        public void setVerdi(int index, String verdi) {
            input.get(index).setValue(verdi);
        }
    }

    private String authId;
    private String template;
    private List<Callback> callbacks = new ArrayList<>();
    private String header;
    private String stage;
    
    public EndUserAuthenticateTemplate() {
        // må ha default ctor
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<Callback> getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(List<Callback> callbacks) {
        this.callbacks = callbacks;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public void setPassord(String passord) {
        setVerdi(passord, "Password");
    }

    public void setBrukernavn(String brukernavn) {
        setVerdi(brukernavn, "Name");
    }

    private void setVerdi(String brukernavn, String name) {
        for (Callback callback : callbacks) {
            if (callback.getType().startsWith(name)) {
                callback.setVerdi(0, brukernavn);
            }
        }
    }
}
