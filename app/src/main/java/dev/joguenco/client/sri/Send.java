package dev.joguenco.client.sri;

import dev.joguenco.util.SendWebServices;
import dev.joguenco.util.FileLocalUtils;
import recepcion.ws.sri.gob.ec.RespuestaSolicitud;

import java.io.File;
import java.io.IOException;

public class Send {

    public static RespuestaSolicitud execute(final String fileName) throws IOException {
        RespuestaSolicitud response = new RespuestaSolicitud();
        File file = new File(fileName);

        byte[] fileByte = FileLocalUtils.fileToByte(file);
        response = SendWebServices.getResponse(file);

        FileLocalUtils.validateResponseSend(response, fileByte, file.getName(), "/tmp", "/tmp");

        return response;
    }
}
