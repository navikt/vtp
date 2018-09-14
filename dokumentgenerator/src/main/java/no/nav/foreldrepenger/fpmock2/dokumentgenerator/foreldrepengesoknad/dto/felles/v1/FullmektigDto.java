package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "fullmektigDtoType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = VergeDto.class)
})

public class FullmektigDto {
   protected String aktoerId;
   protected String navn;
   protected boolean kontaktperson;
   protected boolean stoenadmottaker;
   protected PeriodeDto periode;
   protected String beskrivelse;

   public String getAktoerId() {
      return this.aktoerId;
   }

   public void setAktoerId(String value) {
      this.aktoerId = value;
   }

   public String getNavn() {
      return this.navn;
   }

   public void setNavn(String value) {
      this.navn = value;
   }

   public boolean isKontaktperson() {
      return this.kontaktperson;
   }

   public void setKontaktperson(boolean value) {
      this.kontaktperson = value;
   }

   public boolean isStoenadmottaker() {
      return this.stoenadmottaker;
   }

   public void setStoenadmottaker(boolean value) {
      this.stoenadmottaker = value;
   }

   public PeriodeDto getPeriode() {
      return this.periode;
   }

   public void setPeriode(PeriodeDto value) {
      this.periode = value;
   }

   public String getBeskrivelse() {
      return this.beskrivelse;
   }

   public void setBeskrivelse(String value) {
      this.beskrivelse = value;
   }
}
