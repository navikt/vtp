package no.nav.foreldrepenger.vtp.autotest.journalpost;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;


public class JournalforingResultatDtoTest {

    @Test
    public void leggerInnJournalpostIdOgSjekkerOmDenBlirSerialisertKorrekt() throws JsonProcessingException {
        JournalforingResultatDto journalforingResultatDto = new JournalforingResultatDto();
        journalforingResultatDto.setJournalpostId("11000200");

        String result = new ObjectMapper().writeValueAsString(journalforingResultatDto);

        assertThat(result, containsString("journalpostId"));
        assertThat(result, containsString("11000200"));
    }
}

