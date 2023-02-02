package no.nav.foreldrepenger.vtp.testmodell.identer;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.vtp.testmodell.enums.IdentType;
import no.nav.foreldrepenger.vtp.testmodell.enums.Kjønn;
import no.nav.foreldrepenger.vtp.testmodell.util.TestdataUtil;

public class FoedselsnummerGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(FoedselsnummerGenerator.class);
    private static final Integer NAV_SYNTETISK_IDENT_OFFSET_MND = 40;
    private static final Integer DNR_OFFSETT_DAYS = 40;
    private static final Random random = new Random();

    private final Kjønn kjonn;
    private final IdentType identType;
    private final LocalDate fodselsdato;

    private FoedselsnummerGenerator(Builder fgb){
        this.kjonn = Objects.requireNonNullElseGet(fgb.kjonn, Kjønn::randomKjonn);
        this.identType = Objects.requireNonNullElse(fgb.identType, IdentType.FNR);
        this.fodselsdato = Objects.requireNonNullElseGet(fgb.fodselsdato, TestdataUtil::generateRandomPlausibleBirtdayParent);
    }

    private static int getDigit(String text, int index) {
        return Integer.parseInt(text.substring(index,index + 1));
    }

    private static boolean betweenExclusive(int x, int min, int max) {
        return x>min && x<max;
    }


    private String generate(){
        var day = String.format("%02d",this.fodselsdato.getDayOfMonth());
        var month = String.format("%02d",this.fodselsdato.getMonthValue() + NAV_SYNTETISK_IDENT_OFFSET_MND);
        var year = Integer.toString(this.fodselsdato.getYear()).substring(2);

        int birthNumber;
        if (this.kjonn == Kjønn.KVINNE) {
            birthNumber = 100+ random.nextInt(900/2) *2;
        } else if(this.kjonn == Kjønn.MANN) {
            birthNumber = 100+ random.nextInt(900/2) *2 + 1;
        } else {
            birthNumber = 999;
        }

        if (this.identType == IdentType.DNR){
            day = String.valueOf(Integer.parseInt(day) + DNR_OFFSETT_DAYS);
        }

        int fullYear = this.fodselsdato.getYear();
        if (betweenExclusive(fullYear,1854,1899)){
            if (!betweenExclusive(birthNumber,500,749)) return generate();
        } else if (betweenExclusive(fullYear,1900,1999)){
            if (!betweenExclusive(birthNumber,0,499)) return generate();
        } else if (betweenExclusive(fullYear,1940,1999)){
            if (!betweenExclusive(birthNumber,900,999)) return generate();
        } else if (betweenExclusive(fullYear,2000, 2039)){
            if (!betweenExclusive(birthNumber,500,999)) return generate();
        } else {
            LOG.info("Kunne ikke identifisere fødselsnummerserie");
        }

        String withoutControlDigits = day+month+year+birthNumber;

        var d1 = getDigit(withoutControlDigits, 0);
        var d2 = getDigit(withoutControlDigits, 1);
        var m1 = getDigit(withoutControlDigits, 2);
        var m2 = getDigit(withoutControlDigits, 3);
        var y1 = getDigit(withoutControlDigits, 4);
        var y2 = getDigit(withoutControlDigits, 5);
        var i1 = getDigit(withoutControlDigits, 6);
        var i2 = getDigit(withoutControlDigits, 7);
        var i3 = getDigit(withoutControlDigits, 8);

        var control1 =  11 - ((3 * d1 + 7 * d2 + 6 * m1 + 1 * m2 + 8 * y1 + 9 * y2 + 4 * i1 + 5 * i2 + 2 * i3) % 11);
        if (control1 == 11) {
            control1 = 0;
        }
        var control2 = 11 - ((5 * d1 + 4 * d2 + 3 * m1 + 2 * m2 + 7 * y1 + 6 * y2 + 5 * i1 + 4 * i2 + 3 * i3 + 2 * control1) % 11);
        if (control2 == 11) {
            control2 = 0;
        }
        if (control1 == 10 || control2 == 10) {
            //Invalid number. Get a new one
            return generate();
        }
        return withoutControlDigits + control1 + control2;
    }



    public static class Builder {
        private Kjønn kjonn;
        private IdentType identType;
        private LocalDate fodselsdato;

        public void Builder(){}

        public Builder kjonn(Kjønn k){
            this.kjonn = k;
            return this;
        }

        public Builder identType(IdentType i){
            this.identType = i;
            return this;
        }

        public Builder fodselsdato(LocalDate lt){
            this.fodselsdato = lt;
            return this;
        }

        public String buildAndGenerate() {
            return new FoedselsnummerGenerator(this).generate();
        }

    }

}
