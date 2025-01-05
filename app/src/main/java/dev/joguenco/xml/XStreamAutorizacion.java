package dev.joguenco.xml;

//import autorizacion.ws.sri.gob.ec.Autorizacion;
import dev.joguenco.copy.Autorizacion;
import autorizacion.ws.sri.gob.ec.Mensaje;
import autorizacion.ws.sri.gob.ec.RespuestaComprobante;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Writer;

public class XStreamAutorizacion {

    public static XStream getRespuestaXStream() {
        XStream xstream = new XStream(new XppDriver() {
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    protected void writeText(QuickWriter writer, String text) {
                        writer.write(text);
                    }
                };
            }
        });
        xstream.alias("respuesta", RespuestaComprobante.class);
        xstream.alias("autorizacion", Autorizacion.class);
//        xstream.alias("fechaAutorizacion", XMLGregorianCalendar.class);
        xstream.alias("fechaAutorizacion", String.class);
        xstream.alias("mensaje", Mensaje.class);
        xstream.registerConverter(new RespuestaDateConverter());

        return xstream;
    }
}