package dev.joguenco.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;
import recepcion.ws.sri.gob.ec.RespuestaSolicitud;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;

public class FileLocalUtils {
    public static void saveFile(byte[] file, String fileName, String directory) {
        try {
            File destinationDirectory = new File(directory);
            String path = destinationDirectory + File.separator + fileName;
            File destinationFile = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);
            fileOutputStream.write(file);
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] fileToByte(File file)
            throws IOException {
        String archivo = FileUtils.readFileToString(file, "UTF-8");
        return archivo.getBytes(Charset.forName("UTF-8"));
    }

    public static void validateResponseSend(RespuestaSolicitud respuestaSolicitudEnvio, byte[] archivoFirmado, String nombreArchivo, String directorioDestino, String directorioRechazado) {
        if (respuestaSolicitudEnvio.getEstado().equals("RECIBIDA")) {
            try {
                saveFile(archivoFirmado, nombreArchivo, directorioDestino);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } else if (respuestaSolicitudEnvio.getEstado().equals("DEVUELTA")) {
            byte[] archivoRespuesta = addReasonsRejects(respuestaSolicitudEnvio, archivoFirmado);
            createRejectedDirectories(archivoRespuesta, nombreArchivo, directorioRechazado);
            System.out.println("Error al enviar el comprobante estado :  Revisar la carpeta de rechazados " + respuestaSolicitudEnvio.getEstado());
        }
    }

    public static void createRejectedDirectories(byte[] file, String fileName, String directory) {
        try {

            File destinyDirectory = new File(directory);
            String pathRejectedDirectories = destinyDirectory + File.separator;
            File rejectedDirectories = new File(pathRejectedDirectories);
            if (!rejectedDirectories.exists()) {
                rejectedDirectories.mkdir();
            }
            String pathDestinyFile = pathRejectedDirectories + File.separator + fileName;
            File destinyFile = new File(pathDestinyFile);
            FileOutputStream fileOutputStream = new FileOutputStream(destinyFile);
            if (fileOutputStream != null) {
                fileOutputStream.write(file);
            }
            fileOutputStream.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error al mover el archivo al directorio: ");
        }
    }
    public static byte[] addReasonsRejects(RespuestaSolicitud respuestaRecepcion, byte[] receipt) {
        try {
            byte[] response = ObjectToXML.convertToXml(respuestaRecepcion);
            Document document = mergeFiles(receipt, response);
            return attachFile(document);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Se produjo un error al adjuntar los resultados de la respuesta al comprobante enviado");
        }
        return null;
    }

    public static byte[] attachFile(Document document) {
        try {
            DOMSource source = new DOMSource(document);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(byteArrayOutputStream);
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.transform(source, result);
            return byteArrayOutputStream.toByteArray();
        } catch (TransformerException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Se produjo un error al adjuntar los resultados de la respuesta al comprobante enviado");
        }
        return null;
    }

    public static Document mergeFiles(byte[] comprobante, byte[] respuesta) {
        return merge("*", new byte[][]{comprobante, respuesta});
    }

    private static Document merge(String exp, byte[]... archivosXML) {
        try {
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();
            XPathExpression expression = xpath.compile(exp);
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilderFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(archivosXML[0]);
            Document base = docBuilder.parse(byteArrayInputStream);
            Node results = (Node) expression.evaluate(base, XPathConstants.NODE);
            for (int i = 1; i < archivosXML.length; i++) {
                ByteArrayInputStream byteArrayInputStreamMerge = new ByteArrayInputStream(archivosXML[i]);
                Document merge = docBuilder.parse(byteArrayInputStreamMerge);
                Node nextResults = (Node) expression.evaluate(merge, XPathConstants.NODE);
                results.appendChild(base.importNode(nextResults, true));
            }
            return base;
        } catch (XPathExpressionException | SAXException | IOException | ParserConfigurationException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Se produjo un error al adjuntar los resultados de la respuesta al comprobante enviado");
        }
        return null;
    }
}
