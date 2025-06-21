# RoQui Client SRI

## Software
* Java 21 (Open JDK)
* Gradle 8.14.2

### Tools
* https://www.soapui.org/
* https://cxf.apache.org/

### Command to generate source code for web service client
```
wsdl2java -p autorizacion.ws.sri.gob.ec -d ./webclient -client -impl -ant -exsh false -dns true -dex true -verbose https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl
```

### FIX in autorizacion.ws.sri.gob.ec.RespuestaComprobante class
* In RespuestaComprobante class added @XmlRootElement annotation for return authorizations
* In RespuestaSolicitud class added @XmlRootElement annotation for return response
* Remove or comment @XmlElement(namespace ...)  from all files

## Upgrade Gradle
```
gradle wrapper --gradle-version 8.14.2
```

## Build
```
gradle build
```

## Publish in local maven repository
```
mvn install:install-file -Dfile=./app/build/libs/RoQuiClientSri-1.0.1.jar -DgroupId=dev.joguenco.client -DartifactId=RoQuiClientSri -Dversion=1.0.1 -Dpackaging=jar
```


