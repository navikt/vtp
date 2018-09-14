package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "lukketPeriodeDtoType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UttaksperiodeDto.class),
        @JsonSubTypes.Type(value = UtsettelsesperiodeDto.class),
        @JsonSubTypes.Type(value = OverfoeringsperiodeDto.class),
        @JsonSubTypes.Type(value = OppholdsperiodeDto.class),
        @JsonSubTypes.Type(value = GraderingDto.class)

})


public abstract class LukketPeriodeDto {
   @NotNull
   protected LocalDate fom;
   @NotNull
   protected LocalDate tom;

   protected List vedlegg;

   public LocalDate getFom() {
      return this.fom;
   }

   public void setFom(LocalDate value) {
      this.fom = value;
   }

   public LocalDate getTom() {
      return this.tom;
   }

   public void setTom(LocalDate value) {
      this.tom = value;
   }

   public List getVedlegg() {
      if (this.vedlegg == null) {
         this.vedlegg = new ArrayList();
      }

      return this.vedlegg;
   }
}
