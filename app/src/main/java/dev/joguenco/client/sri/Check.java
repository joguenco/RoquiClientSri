package dev.joguenco.client.sri;

import autorizacion.ws.sri.gob.ec.Autorizacion;
import autorizacion.ws.sri.gob.ec.AutorizacionComprobantesOffline;
import autorizacion.ws.sri.gob.ec.AutorizacionComprobantesOfflineService;
import autorizacion.ws.sri.gob.ec.RespuestaComprobante;
import dev.joguenco.definition.AutorizacionEstado;
import dev.joguenco.definition.Estado;
import dev.joguenco.util.CheckUtil;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;

public class Check {

    public static AutorizacionEstado execute(final String accessKey) throws MalformedURLException {
        String wsdlLocation = "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl";

        AutorizacionComprobantesOfflineService ss = new AutorizacionComprobantesOfflineService(new URL(wsdlLocation), new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesOfflineService"));
        AutorizacionComprobantesOffline port = ss.getAutorizacionComprobantesOfflinePort();

        RespuestaComprobante response = port.autorizacionComprobante(accessKey);

        AutorizacionEstado autorizacionEstado = new AutorizacionEstado(new Autorizacion(), Estado.NO_AUTORIZADO);
        if (!response.getAutorizaciones().getAutorizacion().isEmpty()) {

            CheckUtil authorization = new CheckUtil(response, accessKey);

            autorizacionEstado = authorization.getAuthorizationStatus();

            String tempDir = System.getProperty("java.io.tmpdir");
            authorization.getAuthorizationResponse(autorizacionEstado, tempDir);
        }

        return autorizacionEstado;
    }
}
