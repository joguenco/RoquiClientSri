package dev.joguenco.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class RespuestaDateConverter implements Converter {

    public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        XMLGregorianCalendar i = (XMLGregorianCalendar) source;
        writer.setValue(dateTimeFormat.format(i.toGregorianCalendar().getTime()));
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Date date;
        try {
            date = dateTimeFormat.parse(reader.getValue());
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);

            XMLGregorianCalendar item;
            item = DatatypeFactory
                    .newInstance()
                    .newXMLGregorianCalendar(cal);

            return item;
        } catch (ParseException | DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(XMLGregorianCalendar.class);
    }
}