package dev.joguenco.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.IOException;
import java.io.StringWriter;

public class ObjectToXML {

    public static byte[] convertToXml(Object comprobante) {
        try {
            StringWriter xmlComprobante = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(new Class[]{comprobante.getClass()});
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
            marshaller.marshal(comprobante, xmlComprobante);
            xmlComprobante.close();
            return xmlComprobante.toString().getBytes("UTF-8");
        } catch (IOException | JAXBException | ClassCastException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Se produjo un error al convetir el archivo al formato XML");
            StringWriter xmlError = new StringWriter();
            xmlError.write("Se produjo un error al convetir el archivo al formato XML");
            return xmlError.toString().getBytes();
        }
    }
}

