# Distributed KV Store Java

This is not a full-featured distributed KV store. 

Rather its a simple demo application to depict replication of data between two Java processes running within different VM.

There are many way to achieve this in Java, as listed below.

1. Java object Serialization over network using Sockets
2. Java RPC
3. Java HTTP
4. Java WebSockets (In case if you any server to act as publisher/receiver)

## How To Build
---

Steps:

1. create a local clone of this repository (with git clone <https://github.com/pramod-rathod-avalara/dist-store-project.git)>
2. Go to project's folder (with `cd dist-store-project/Store`)
3. Build the artifacts (with `mvn clean package`)

After above steps a folder dist-store-project/Store/target is created and all goodies are in that folder.

## How To Run
---

Steps:

1. Build the project.
2. Run the listener process.
3. Run the publisher process.

### Step 1: Build the project

1. `cd dist-store-project/Store`
2. `mvn package`

### Step 2: Running the listener process

1. `cd dist-store-project/Store/target`
2. `java -cp Store-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.mydkvstore.httpservice.StoreHttpServiceLauncher --port 9339`

With the last command the server (Jetty) started on port `9339`. The port of the application will be used as to register it as a listener.

### Step 3: Running the publisher process

1. `cd dist-store-project/Store/target`
2. `java -cp Store-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.mydkvstore.httpservice.StoreHttpServiceLauncher --port 8338 --listener 9339`

With the last command the server (Jetty) started on port `8338`. The port of the application will be used as to serve 2 REST endpoints to set and get value from the store. With the listener argument, port 9339 will be registered as a listener that will listen for any changes on the store.
