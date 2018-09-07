package no.nav.foreldrepenger.fpmock2.testmodell.identer;

import java.time.LocalDate;
import java.util.Random;

import no.nav.foreldrepenger.fpmock2.testmodell.enums.IdentType;
import no.nav.foreldrepenger.fpmock2.testmodell.enums.Kjonn;
import no.nav.foreldrepenger.fpmock2.testmodell.util.TestdataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FoedselsnummerGenerator {
    private final static Logger LOG = LoggerFactory.getLogger(FoedselsnummerGenerator.class);


    private Kjonn kjonn;
    private IdentType identType;
    private LocalDate fodselsdato;

    private FoedselsnummerGenerator(FodselsnummerGeneratorBuilder fgb){
        if(fgb.kjonn != null) {
            this.kjonn = fgb.kjonn;
        } else {
            this.kjonn = Kjonn.randomKjonn();
        }
        if(fgb.identType != null) {
            this.identType = fgb.identType;
        } else {
            this.identType = IdentType.FNR;
        }
        if(fgb.fodselsdato != null){
            this.fodselsdato = fgb.fodselsdato;
        } else {
            this.fodselsdato = TestdataUtil.generateRandomPlausibleBirtdayParent();
        }
    }

    private static int getDigit(String text, int index) {
        return Integer.parseInt(text.substring(index,index + 1));
    }

    private static boolean betweenExclusive(int x, int min, int max) {
        return x>min && x<max;
    }


    private String generate(){
        //LOG.info("Vil generere FNR for " + this.kjonn + " født: " + this.fodselsdato + " av type: " + this.identType);

        Random random = new Random();

        String day = String.format("%02d",this.fodselsdato.getDayOfMonth());
        String month = String.format("%02d",this.fodselsdato.getMonthValue());
        String year = Integer.toString(this.fodselsdato.getYear()).substring(2);

        int birthNumber = 999;

        if(this.kjonn == Kjonn.KVINNE) {
            birthNumber = 100+ random.nextInt(900/2) *2;
        } else if(this.kjonn == Kjonn.MANN) {
            birthNumber = 100+ random.nextInt(900/2) *2 + 1;
        } else {
            birthNumber = 999;
        }

        int fullYear = this.fodselsdato.getYear();
        if(betweenExclusive(fullYear,1854,1899)){
            if (!betweenExclusive(birthNumber,500,749)) return generate();
        } else if(betweenExclusive(fullYear,1900,1999)){
            if (!betweenExclusive(birthNumber,0,499)) return generate();
        } else if(betweenExclusive(fullYear,1940,1999)){
            if (!betweenExclusive(birthNumber,900,999)) return generate();
        } else if(betweenExclusive(fullYear,2000, 2039)){
            if (!betweenExclusive(birthNumber,500,999)) return generate();
        } else {
            LOG.info("Kunne ikke identifisere fødselsnummerserie");
        }

        String withoutControlDigits = day+month+year+birthNumber;

        int d1 = getDigit(withoutControlDigits, 0);
        int d2 = getDigit(withoutControlDigits, 1);
        int m1 = getDigit(withoutControlDigits, 2);
        int m2 = getDigit(withoutControlDigits, 3);
        int y1 = getDigit(withoutControlDigits, 4);
        int y2 = getDigit(withoutControlDigits, 5);
        int i1 = getDigit(withoutControlDigits, 6);
        int i2 = getDigit(withoutControlDigits, 7);
        int i3 = getDigit(withoutControlDigits, 8);

        int control1 =  11 - ((3 * d1 + 7 * d2 + 6 * m1 + 1 * m2 + 8 * y1 + 9 * y2 + 4 * i1 + 5 * i2 + 2 * i3) % 11);
        if (control1 == 11) {
            control1 = 0;
        }
        int control2 = 11 - ((5 * d1 + 4 * d2 + 3 * m1 + 2 * m2 + 7 * y1 + 6 * y2 + 5 * i1 + 4 * i2 + 3 * i3 + 2 * control1) % 11);
        if (control2 == 11) {
            control2 = 0;
        }
        if (control1 == 10 || control2 == 10) {
            //Invalid number. Get a new one
            return generate();
        }
        return withoutControlDigits + control1 + control2;
    }



    public static class FodselsnummerGeneratorBuilder {

        private Kjonn kjonn;
        private IdentType identType;
        private LocalDate fodselsdato;

        public void FodselsnummerGenerator(){}

        public FodselsnummerGeneratorBuilder kjonn(Kjonn k){
            this.kjonn = k;
            return this;
        }

        public FodselsnummerGeneratorBuilder identType(IdentType i){
            this.identType = i;
            return this;
        }

        public FodselsnummerGeneratorBuilder fodselsdato(LocalDate lt){
            this.fodselsdato = lt;
            return this;
        }

        public String buildAndGenerate() {
            return new FoedselsnummerGenerator(this).generate();
        }

    }

}
