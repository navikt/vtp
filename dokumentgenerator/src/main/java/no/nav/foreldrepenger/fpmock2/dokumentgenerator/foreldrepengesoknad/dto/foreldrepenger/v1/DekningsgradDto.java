package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


public class DekningsgradDto {
   protected List perioder;
   @NotNull
   protected DekningsgraderDto dekningsgrad;

   public List getPerioder() {
      if (this.perioder == null) {
         this.perioder = new ArrayList();
      }

      return this.perioder;
   }

   public DekningsgraderDto getDekningsgrad() {
      return this.dekningsgrad;
   }

   public void setDekningsgrad(DekningsgraderDto value) {
      this.dekningsgrad = value;
   }
}
