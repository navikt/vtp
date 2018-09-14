package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import javax.validation.constraints.NotNull;

public class VergeDto extends FullmektigDto {
   @NotNull
   protected VergetypeDto vergetype;
   protected boolean tvungenForvaltning;

   public VergetypeDto getVergetype() {
      return this.vergetype;
   }

   public void setVergetype(VergetypeDto value) {
      this.vergetype = value;
   }

   public boolean isTvungenForvaltning() {
      return this.tvungenForvaltning;
   }

   public void setTvungenForvaltning(boolean value) {
      this.tvungenForvaltning = value;
   }
}
