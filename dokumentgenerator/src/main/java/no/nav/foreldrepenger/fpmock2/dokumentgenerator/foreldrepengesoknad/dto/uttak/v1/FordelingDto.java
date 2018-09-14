package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.LukketPeriodeDto;

import java.util.ArrayList;
import java.util.List;


public class FordelingDto {
   protected Boolean annenForelderErInformert;
   protected List<LukketPeriodeDto> perioder;
   protected OverfoeringsaarsakerDto oenskerKvoteOverfoert;

   public Boolean isAnnenForelderErInformert() {
      return this.annenForelderErInformert;
   }

   public void setAnnenForelderErInformert(Boolean value) {
      this.annenForelderErInformert = value;
   }

   public List<LukketPeriodeDto> getPerioder() {
      if (this.perioder == null) {
         this.perioder = new ArrayList<>();
      }

      return this.perioder;
   }

   public OverfoeringsaarsakerDto getOenskerKvoteOverfoert() {
      return this.oenskerKvoteOverfoert;
   }

   public void setOenskerKvoteOverfoert(OverfoeringsaarsakerDto value) {
      this.oenskerKvoteOverfoert = value;
   }

   public void setPerioder(List<LukketPeriodeDto> perioder) {
      this.perioder = perioder;
   }
}


