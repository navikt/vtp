package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.endringssoeknad.v1;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.YtelseDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1.FordelingDto;

public class EndringssoeknadDto extends YtelseDto {
   protected String saksnummer;
   protected FordelingDto fordeling;

   public String getSaksnummer() {
      return this.saksnummer;
   }

   public void setSaksnummer(String value) {
      this.saksnummer = value;
   }

   public FordelingDto getFordeling() {
      return this.fordeling;
   }

   public void setFordeling(FordelingDto value) {
      this.fordeling = value;
   }
}
