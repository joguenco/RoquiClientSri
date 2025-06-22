package dev.joguenco;

import dev.joguenco.client.sri.Check;
import dev.joguenco.client.sri.Send;
import java.io.IOException;

public class Main {
    public String getGreeting() {
        return "RoQui Client SRI";
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new Main().getGreeting());

        ClassLoader classLoader = Main.class.getClassLoader();

        /*
        var pathXmlFile = classLoader
                .getResource("0606202301100245687700110010030000000081234567811.xml").getPath();

        final var statusSend = Send.execute(pathXmlFile);
        System.out.println(statusSend.getEstado());
        */
        final var url = "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl";
        final var statusCheck = Check.execute(url,"0606202301100245687700110010030000000081234567811");
        System.out.println(statusCheck.getEstadoAutorizacion().getDescripcion());
    }
}
