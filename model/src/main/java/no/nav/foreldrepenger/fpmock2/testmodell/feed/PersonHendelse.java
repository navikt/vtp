package no.nav.foreldrepenger.fpmock2.testmodell.feed;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSerialize(using = HendelseSerializer.class)
public abstract class PersonHendelse {

    protected Long sequence;
    protected Metadata metadata;

    public PersonHendelse(){
        this.metadata = new Metadata();
    }

    public Long getSequence() {
        return sequence;
    }

    public abstract String getType();

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    private class Metadata{
        private String guid;
        private LocalDateTime createdDate;

        public Metadata() {
            this.guid = UUID.randomUUID().toString();
            this.createdDate = LocalDateTime.now();
        }

    }

}
