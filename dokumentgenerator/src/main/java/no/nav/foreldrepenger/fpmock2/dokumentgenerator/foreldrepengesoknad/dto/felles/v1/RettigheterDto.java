package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;


public class RettigheterDto {
   protected Boolean harAnnenForelderRett;
   protected Boolean harOmsorgForBarnetIPeriodene;
   protected Boolean harAleneomsorgForBarnet;

   public Boolean isHarAnnenForelderRett() {
      return this.harAnnenForelderRett;
   }

   public void setHarAnnenForelderRett(Boolean value) {
      this.harAnnenForelderRett = value;
   }

   public Boolean isHarOmsorgForBarnetIPeriodene() {
      return this.harOmsorgForBarnetIPeriodene;
   }

   public void setHarOmsorgForBarnetIPeriodene(Boolean value) {
      this.harOmsorgForBarnetIPeriodene = value;
   }

   public Boolean isHarAleneomsorgForBarnet() {
      return this.harAleneomsorgForBarnet;
   }

   public void setHarAleneomsorgForBarnet(Boolean value) {
      this.harAleneomsorgForBarnet = value;
   }
}
