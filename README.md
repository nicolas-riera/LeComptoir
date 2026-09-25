# LeComptoir

## Requirements

- Java 25 (JDK) + Maven
- Node.js (to run the TypeScript client via `npx`)

## Run the server (Java)

```
./run-server
```

Compiles and starts the Java server (`com.andrenicolas.App`) on `http://127.0.0.1:8080`.

## Run the client (TypeScript)

```
./run-client
```

Runs the TypeScript demo client (`demo/demo.ts`), which consumes the server's API.

> The server must be running before you start the client.
