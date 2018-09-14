package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "soekersRelasjonTilBarnetDtoType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AdopsjonDto.class),
        @JsonSubTypes.Type(value = FoedselDto.class),
        @JsonSubTypes.Type(value = OmsorgsovertakelseDto.class),
        @JsonSubTypes.Type(value = TerminDto.class)
})

public abstract class SoekersRelasjonTilBarnetDto {
   protected int antallBarn;

   protected List vedlegg;

   public int getAntallBarn() {
      return this.antallBarn;
   }

   public void setAntallBarn(int value) {
      this.antallBarn = value;
   }

   public List getVedlegg() {
      if (this.vedlegg == null) {
         this.vedlegg = new ArrayList();
      }

      return this.vedlegg;
   }
}
