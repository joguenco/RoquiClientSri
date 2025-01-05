package dev.joguenco.util;

import dev.joguenco.copy.Autorizacion;
import autorizacion.ws.sri.gob.ec.Mensaje;
import autorizacion.ws.sri.gob.ec.RespuestaComprobante;
import com.thoughtworks.xstream.XStream;
import dev.joguenco.definition.AutorizacionEstado;
import dev.joguenco.definition.Estado;
import dev.joguenco.xml.XStreamAutorizacion;

import java.io.*;

public class CheckUtil {

    private final RespuestaComprobante response;
    private final String fileName;

    public CheckUtil(RespuestaComprobante response, String fileName) {
        this.response = response;
        this.fileName = fileName + ".xml";
    }

    public void getAuthorizationResponse(AutorizacionEstado autorizacionEstado, String destinationDirectory) {

        byte[] responseXml = getAuthorizationResponseXml(copyAuthorization(autorizacionEstado.getAutorizacion()));
        if (Estado.AUTORIZADO.equals(autorizacionEstado.getEstadoAutorizacion())) {
            FileLocalUtils.saveFile(responseXml, this.fileName, destinationDirectory);
        } else {
            if (Estado.NO_AUTORIZADO.equals(autorizacionEstado.getEstadoAutorizacion())) {
                FileLocalUtils.saveFile(responseXml, this.fileName, destinationDirectory);
                System.out.println("Error al validar el comprobante estado " + autorizacionEstado.getEstadoAutorizacion().getDescripcion() + autorizacionEstado.getMensaje());
            }
            if (Estado.PROCESADO.equals(autorizacionEstado.getEstadoAutorizacion())) {
                System.out.println("Error al validar el comprobante estado : " + autorizacionEstado.getEstadoAutorizacion().getDescripcion());
            }
        }
    }

    /**
     * Copy method for remove XMLGregorianCalendar
     * and convert in String
     */
    private Autorizacion copyAuthorization(autorizacion.ws.sri.gob.ec.Autorizacion autorizacion) {
        Autorizacion copy = new Autorizacion();

        copy.setEstado(autorizacion.getEstado());
        copy.setNumeroAutorizacion(autorizacion.getNumeroAutorizacion());
        copy.setAmbiente(autorizacion.getAmbiente());
        copy.setComprobante(autorizacion.getComprobante());
        copy.setMensajes(autorizacion.getMensajes());
        copy.setFechaAutorizacion(autorizacion.getFechaAutorizacion().toString());

        return copy;
    }

    private byte[] getAuthorizationResponseXml(Autorizacion autorizacion) {
        try {
            XStream xstream = XStreamAutorizacion.getRespuestaXStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(outputStream, "UTF-8");

            setXmlCDATA(autorizacion);
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.append('\n');
            xstream.toXML(autorizacion, writer);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setXmlCDATA(Autorizacion autorizacion) {
        autorizacion.setComprobante("<![CDATA[" + autorizacion.getComprobante() + "]]>");
    }

    public AutorizacionEstado getAuthorizationStatus() {
        for (autorizacion.ws.sri.gob.ec.Autorizacion autorizacion : this.response.getAutorizaciones().getAutorizacion()) {
            Estado status = Estado.getEstadoAutorizacion(autorizacion.getEstado());
            if (Estado.AUTORIZADO.equals(status)) {
                return new AutorizacionEstado(autorizacion, Estado.AUTORIZADO);
            }
            if (Estado.PROCESADO.equals(status)) {
                return new AutorizacionEstado(autorizacion, Estado.AUTORIZADO);
            }
        }
        autorizacion.ws.sri.gob.ec.Autorizacion autorizacion = (autorizacion.ws.sri.gob.ec.Autorizacion) this.response.getAutorizaciones().getAutorizacion().get(0);

        return new AutorizacionEstado(autorizacion, Estado.NO_AUTORIZADO, getAuthorizationMessage(autorizacion));
    }

    public static String getAuthorizationMessage(autorizacion.ws.sri.gob.ec.Autorizacion autorizacion) {
        StringBuilder mensaje = new StringBuilder();
        for (Mensaje m : autorizacion.getMensajes().getMensaje()) {
            if (m.getInformacionAdicional() != null) {
                mensaje.append(String.format("\n%s:%s", new Object[]{m.getMensaje(), m.getInformacionAdicional()}));
            } else {
                mensaje.append(String.format("\n%s", new Object[]{m.getMensaje()}));
            }
        }
        return mensaje.toString();
    }
}

