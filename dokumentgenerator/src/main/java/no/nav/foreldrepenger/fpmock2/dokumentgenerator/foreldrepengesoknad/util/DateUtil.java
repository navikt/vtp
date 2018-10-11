package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class DateUtil {
    private DateUtil() {
    }

    public static XMLGregorianCalendar convertToXMLGregorianCalendar(LocalDateTime localDateTime) throws DatatypeConfigurationException {
        if (localDateTime == null) {
            return null;
        } else {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        }
    }

    public static XMLGregorianCalendar convertToXMLGregorianCalendar(LocalDate localDate) throws DatatypeConfigurationException {
        if (localDate == null) {
            return null;
        } else {
            GregorianCalendar gregorianCalendar = GregorianCalendar.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        }
    }

    public static XMLGregorianCalendar convertToXMLGregorianCalendarRemoveTimezone(LocalDate localDate) throws DatatypeConfigurationException {
        return localDate == null ? null : DatatypeFactory.newInstance().newXMLGregorianCalendar(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), -2147483648, -2147483648, -2147483648, -2147483648, -2147483648);
    }

    public static LocalDateTime convertToLocalDateTime(XMLGregorianCalendar xmlGregorianCalendar) {
        return xmlGregorianCalendar == null ? null : LocalDateTime.ofInstant(xmlGregorianCalendar.toGregorianCalendar().getTime().toInstant(), ZoneId.systemDefault());
    }

    public static LocalDate convertToLocalDate(XMLGregorianCalendar xmlGregorianCalendar) {
        return xmlGregorianCalendar == null ? null : xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime().toLocalDate();
    }
}
