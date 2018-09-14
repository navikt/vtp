package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.engangsstoenad.v1;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.AnnenForelderDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.MedlemskapDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.SoekersRelasjonTilBarnetDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.YtelseDto;
import javax.validation.constraints.NotNull;

public class EngangsstønadDto extends YtelseDto {
   @NotNull
   protected SoekersRelasjonTilBarnetDto soekersRelasjonTilBarnet;

   @NotNull
   protected MedlemskapDto medlemskap;
   protected AnnenForelderDto annenForelder;

   public SoekersRelasjonTilBarnetDto getSoekersRelasjonTilBarnet() {
      return this.soekersRelasjonTilBarnet;
   }

   public void setSoekersRelasjonTilBarnet(SoekersRelasjonTilBarnetDto value) {
      this.soekersRelasjonTilBarnet = value;
   }

   public MedlemskapDto getMedlemskap() {
      return this.medlemskap;
   }

   public void setMedlemskap(MedlemskapDto value) {
      this.medlemskap = value;
   }

   public AnnenForelderDto getAnnenForelder() {
      return this.annenForelder;
   }

   public void setAnnenForelder(AnnenForelderDto value) {
      this.annenForelder = value;
   }
}
