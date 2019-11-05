package no.nav.foreldrepenger.vtp.autotest.personhendelse;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = PersonhendelseDeserializer.class)
public interface PersonhendelseDto {

    String getType();
}
