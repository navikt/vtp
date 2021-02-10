package no.nav.tjeneste.virksomhet.sak.rs;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SakJson {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("tema")
    private String tema;
    @JsonProperty("applikasjon")
    private String applikasjon;
    @JsonProperty("aktoerId")
    private String aktoerId;
    @JsonProperty("fagsakNr")
    private String fagsakNr;

    @JsonCreator
    public SakJson(@JsonProperty("id") Long id,
                   @JsonProperty("tema") String tema,
                   @JsonProperty("applikasjon") String applikasjon,
                   @JsonProperty("aktoerId") String aktoerId,
                   @JsonProperty("fagsakNr") String fagsakNr) {
        this.id = id;
        this.tema = tema;
        this.applikasjon = applikasjon;
        this.aktoerId = aktoerId;
        this.fagsakNr = fagsakNr;
    }

    private SakJson() {}

    public Long getId() {
        return id;
    }

    public String getTema() {
        return tema;
    }

    public String getApplikasjon() {
        return applikasjon;
    }

    public String getAktoerId() {
        return aktoerId;
    }

    public String getFagsakNr() {
        return fagsakNr;
    }

    @Override
    public String toString() {
        return "SakJson{" +
                "id=" + id +
                ", tema='" + tema + '\'' +
                ", applikasjon='" + applikasjon + '\'' +
                ", fagsakNr='" + fagsakNr + '\'' +
                '}';
    }

    public static SakJson.Builder getBuilder() {
        return new SakJson.Builder();
    }

    public static class Builder {
        SakJson sak;

        Builder() {
            sak = new SakJson();
        }

        public SakJson.Builder medId(Long id) {
            this.sak.id = id;
            return this;
        }

        public SakJson.Builder medTema(String tema) {
            this.sak.tema = tema;
            return this;
        }

        public SakJson.Builder medApplikasjon(String applikasjon) {
            this.sak.applikasjon = applikasjon;
            return this;
        }

        public SakJson.Builder medAktoerId(String aktoerId) {
            this.sak.aktoerId = aktoerId;
            return this;
        }

        public SakJson.Builder medFagsakNr(String fagsakNr) {
            this.sak.fagsakNr = fagsakNr;
            return this;
        }

        public SakJson build() {
            return this.sak;
        }
    }

    }
