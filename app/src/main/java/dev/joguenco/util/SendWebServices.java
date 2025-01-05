package dev.joguenco.util;

import jakarta.xml.ws.WebServiceException;
import recepcion.ws.sri.gob.ec.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.xml.namespace.QName;
import java.net.MalformedURLException;

public class SendWebServices {

    private static RecepcionComprobantesOfflineService service;

    public SendWebServices(String wsdlLocation)
            throws MalformedURLException, WebServiceException {
        try {
            URL url = new URL(wsdlLocation);
            QName qname = new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesOfflineService");
//            service = new RecepcionComprobantesOfflineService(url, qname);
            service = new RecepcionComprobantesOfflineService();
        } catch (WebServiceException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public RespuestaSolicitud send(File xmlFile) {
        RespuestaSolicitud response = null;
        try {
            RecepcionComprobantesOffline port = service.getRecepcionComprobantesOfflinePort();
            response = port.validarComprobante(FileLocalUtils.fileToByte(xmlFile));
        } catch (IOException | WebServiceException e) {
            System.out.println(e.getMessage());
            response = new RespuestaSolicitud();
            response.setEstado(e.getMessage());
            return response;
        }
        return response;
    }

    public static RespuestaSolicitud getResponse(File file) {
        RespuestaSolicitud response = new RespuestaSolicitud();
        SendWebServices clientWebService = null;
        try {
            clientWebService = new SendWebServices("https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl");
        } catch (MalformedURLException | WebServiceException ex) {
            System.out.println(ex.getMessage());
            response.setEstado(ex.getMessage());
            return response;
        }
        response = clientWebService.send(file);

        return response;
    }
}
