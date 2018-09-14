package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import java.util.ArrayList;
import java.util.List;

public class MedlemskapDto {
   protected List<OppholdNorgeDto> oppholdNorge;
   protected Boolean iNorgeVedFoedselstidspunkt;
   protected Boolean boddINorgeSiste12Mnd;
   protected Boolean borINorgeNeste12Mnd;
   protected List<OppholdUtlandetDto> oppholdUtlandet;

   public List<OppholdNorgeDto> getOppholdNorge() {
      if (this.oppholdNorge == null) {
         this.oppholdNorge = new ArrayList<OppholdNorgeDto>();
      }

      return this.oppholdNorge;
   }

   public List<OppholdUtlandetDto> getOppholdUtlandet() {
      if (this.oppholdUtlandet == null) {
         this.oppholdUtlandet = new ArrayList<OppholdUtlandetDto>();
      }

      return this.oppholdUtlandet;
   }

   public void setOppholdNorge(List<OppholdNorgeDto> oppholdNorge) {
      this.oppholdNorge = oppholdNorge;
   }

   public Boolean getiNorgeVedFoedselstidspunkt() {
      return iNorgeVedFoedselstidspunkt;
   }

   public void setiNorgeVedFoedselstidspunkt(Boolean iNorgeVedFoedselstidspunkt) {
      this.iNorgeVedFoedselstidspunkt = iNorgeVedFoedselstidspunkt;
   }

   public Boolean getBoddINorgeSiste12Mnd() {
      return boddINorgeSiste12Mnd;
   }

   public void setBoddINorgeSiste12Mnd(Boolean boddINorgeSiste12Mnd) {
      this.boddINorgeSiste12Mnd = boddINorgeSiste12Mnd;
   }

   public Boolean getBorINorgeNeste12Mnd() {
      return borINorgeNeste12Mnd;
   }

   public void setBorINorgeNeste12Mnd(Boolean borINorgeNeste12Mnd) {
      this.borINorgeNeste12Mnd = borINorgeNeste12Mnd;
   }

   public void setOppholdUtlandet(List<OppholdUtlandetDto> oppholdUtlandet) {
      this.oppholdUtlandet = oppholdUtlandet;
   }
}
