package no.nav.foreldrepenger.fpmock2.felles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConversionUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ConversionUtils.class);

    public static XMLGregorianCalendar convertToXMLGregorianCalendar(LocalDateTime localDateTime) {
        if(localDateTime==null) {
            return null;
        }
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        XMLGregorianCalendar xmlGregorianCalendar = null;
        try {
            xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            LOG.error("", e);
        }
        return xmlGregorianCalendar;
    }

    public static XMLGregorianCalendar convertToXMLGregorianCalendar(LocalDate localDate) {

        if (localDate == null) {
            return null;
        } else {
            XMLGregorianCalendar xmlGregorianCalendar = null;
            GregorianCalendar gregorianCalendar = GregorianCalendar.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            try {
                xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
            }
            catch (DatatypeConfigurationException e) {
                LOG.error("", e);
            }
            return xmlGregorianCalendar;
        }
    }

    public static LocalDateTime convertToLocalDateTime(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        }
        return LocalDateTime.ofInstant(xmlGregorianCalendar.toGregorianCalendar().getTime().toInstant(), ZoneId.systemDefault());
    }

    public static LocalDate convertToLocalDate(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        }
        return xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime().toLocalDate();
    }
}
