# Roqui Client SRI

## Software
* Java 21 (Open JDK)
* Gradle 8.11.1

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
gradle wrapper --gradle-version 8.11.1
```

## Build
```
gradle build
```

## Publish in local maven repository
```
mvn install:install-file -Dfile=./app/build/libs/RoquiClientSri-1.0.1.jar -DgroupId=dev.joguenco.client -DartifactId=RoquiClientSri -Dversion=1.0.1 -Dpackaging=jar
```


