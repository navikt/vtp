package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import javax.validation.constraints.NotNull;

public class BrukerDto {
   @NotNull
   protected String aktoerId;

   @NotNull
   protected BrukerrollerDto soeknadsrolle;
   protected FullmektigDto fullmektig;

   public String getAktoerId() {
      return this.aktoerId;
   }

   public void setAktoerId(String value) {
      this.aktoerId = value;
   }

   public BrukerrollerDto getSoeknadsrolle() {
      return this.soeknadsrolle;
   }

   public void setSoeknadsrolle(BrukerrollerDto value) {
      this.soeknadsrolle = value;
   }

   public FullmektigDto getFullmektig() {
      return this.fullmektig;
   }

   public void setFullmektig(FullmektigDto value) {
      this.fullmektig = value;
   }
}
