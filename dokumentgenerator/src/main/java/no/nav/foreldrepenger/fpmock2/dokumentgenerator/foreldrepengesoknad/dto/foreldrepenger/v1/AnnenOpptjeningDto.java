package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1;



import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.PeriodeDto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


public class AnnenOpptjeningDto {
   @NotNull
   protected AnnenOpptjeningTyperDto type;
   protected PeriodeDto periode;

   protected List vedlegg;

   public AnnenOpptjeningTyperDto getType() {
      return this.type;
   }

   public void setType(AnnenOpptjeningTyperDto value) {
      this.type = value;
   }

   public PeriodeDto getPeriode() {
      return this.periode;
   }

   public void setPeriode(PeriodeDto value) {
      this.periode = value;
   }

   public List getVedlegg() {
      if (this.vedlegg == null) {
         this.vedlegg = new ArrayList();
      }

      return this.vedlegg;
   }
}
