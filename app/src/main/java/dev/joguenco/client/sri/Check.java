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

    /**
     * Queries the SRI web service for the authorization status of an electronic document.
     *
     * @param wsdlLocation the WSDL location of the SRI web service
     * 1. Test: "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl"
     * 2. Production: "https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl"
     * @param accessKey the access key of the electronic document to check
     * @return the authorization status of the document
     * @throws MalformedURLException if the WSDL URL is malformed
     */
    public static AutorizacionEstado execute(final String wsdlLocation, final String accessKey) throws MalformedURLException {

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
