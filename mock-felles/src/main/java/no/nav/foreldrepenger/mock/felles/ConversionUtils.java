package no.nav.foreldrepenger.mock.felles;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConversionUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ConversionUtils.class);

    public static XMLGregorianCalendar convertToXMLGregorianCalendar(LocalDateTime localDateTime) {
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

}
