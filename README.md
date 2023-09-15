# aims-demo-ml-provider
Demo Arrowhead service provider for detecting humans on a picture. 

### How it works?

This `aims-demo-ml-provider` periodically reads the location specific input folders, where the pictures are expected to being saved by the camera control application. Each location folder represents an area (one grid) from the frontend where at least one camera is located. Each picture has been found are going to being fed into the human detection ML software. Every postive results are being stored (in an in-memory database) and cloud be fetched by consuming the [`fetch-detections`](./documents/fetch-detections_SDD.md) service.   

### How to run?

**Edit the application.properties config file:**

- define the Arrowhead ServiceRegistry address and port
- define location list 
- define your input folder
- define your working folder
- define the processing tool path

**Certificates**

By default the project provides a `testcloud2` compatible certificate and truststore. For any other cloud you must generate a certificate and define its data and a proper truststore data in the applicatio.properties file.

```
server.ssl.key-store-type=PKCS12
server.ssl.key-store=file:path/to/aimsmlprovider.p12
server.ssl.key-store-password=yourclientpassword
server.ssl.key-alias=aimsmlprovider.<yourcloudname>.<youroperatorname>.arrowhead.eu
server.ssl.key-password=yourclientpassword
server.ssl.client-auth=need
server.ssl.trust-store-type=PKCS12
server.ssl.trust-store=file:path/to/your/cloud/truststore.p12
server.ssl.trust-store-password=yourtrustorepassword
```

**Run**

Note: Running Arrowhead ServiceRegistry is required.

`cd path/to/jar` 
`java -jar aims-demo-ml-provider-<version>.jar`
