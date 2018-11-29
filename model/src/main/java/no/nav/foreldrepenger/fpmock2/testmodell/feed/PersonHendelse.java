package no.nav.foreldrepenger.fpmock2.testmodell.feed;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class PersonHendelse {

    protected Long sekvensnummer;
    protected Metadata metadata;

    public PersonHendelse(){
        this.metadata = new Metadata();
    }

    public Long getSekvensnummer() {
        return sekvensnummer;
    }

    public abstract String getType();

    public void setSekvensnummer(Long sekvensnummer) {
        this.sekvensnummer = sekvensnummer;
    }

    private class Metadata{
        private String guid;
        private LocalDateTime createdDate;

        public Metadata() {
            this.guid = UUID.randomUUID().toString();
        }

    }

}
