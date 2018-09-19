package no.nav.foreldrepenger.fpmock2.testmodell.organisasjon;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrganisasjonDetaljerModell {

    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @JsonProperty("registreringsDato")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate registreringsDato;

    @JsonProperty("datoSistEndret")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate datoSistEndret;

    public OrganisasjonDetaljerModell() {
    }

    public LocalDate getRegistreringsDato() {
        return registreringsDato;
    }

    public void setRegistreringsDato(LocalDate registreringsDato) {
        this.registreringsDato = registreringsDato;
    }

    public LocalDate getDatoSistEndret() {
        return datoSistEndret;
    }

    public void setDatoSistEndret(LocalDate datoSistEndret) {
        this.datoSistEndret = datoSistEndret;
    }
}
