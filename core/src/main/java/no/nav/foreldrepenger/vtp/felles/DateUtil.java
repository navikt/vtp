package no.nav.foreldrepenger.vtp.felles;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    private DateUtil() {
    }

    public static XMLGregorianCalendar convertToXMLGregorianCalendar(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        } else {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
            return datatypeFactoryInstance().newXMLGregorianCalendar(gregorianCalendar);
        }
    }

    public static XMLGregorianCalendar convertToXMLGregorianCalendar(LocalDate localDate) {
        if (localDate == null) {
            return null;
        } else {
            GregorianCalendar gregorianCalendar = GregorianCalendar.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            return datatypeFactoryInstance().newXMLGregorianCalendar(gregorianCalendar);
        }
    }

    private static DatatypeFactory datatypeFactoryInstance() {
        try {
            return DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

}
