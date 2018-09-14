package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.mapper;

import no.nav.vedtak.felles.integrasjon.felles.ws.DateUtil;
import org.modelmapper.ModelMapper;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;

public class LocalDateToXmlGregorianCalender extends DtoMapper<LocalDate, XMLGregorianCalendar> {

    public LocalDateToXmlGregorianCalender(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    protected XMLGregorianCalendar convert(LocalDate localDate) {
        try {
            return DateUtil.convertToXMLGregorianCalendar(localDate);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
