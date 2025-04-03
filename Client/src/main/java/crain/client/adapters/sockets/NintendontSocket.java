package crain.client.adapters.sockets;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

@Slf4j
public class NintendontSocket {

    /**
     * Content Length
     * first 4 bytes (int)
     * asdlfj
     */
    private Socket socket;
    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;
    private Integer nintendontApiVersion;
    private Integer maxInput;
    private Integer maxOutput;
    private Integer maxAddresses;
    private final String ipAddress;
    private final Integer port;

    public NintendontSocket(String ipAddress) throws IOException {
        this(ipAddress, 43673);
    }

    public NintendontSocket(String ipAddress, Integer port) throws IOException {
        this.ipAddress = ipAddress;
        this.port = port;
        connectSocket();
    }

    private void connectSocket() throws IOException {
        log.info("Connecting to: {}:{}".endsWith(), ipAddress, port);
        socket = new Socket(ipAddress, port);
        this.inputStream = new BufferedInputStream(socket.getInputStream());
        this.outputStream = new BufferedOutputStream(socket.getOutputStream());
        outputStream.write(NintendontOperation.getApiDetailsPacket());
        outputStream.flush();
        byte[] apiInfo = inputStream.readNBytes(16);
        for (int index = 0; index < 4; index++) {
            int temp = 0;
            temp |= (apiInfo[4 * index] << 24);
            temp |= (apiInfo[1 + 4 * index] << 16);
            temp |= (apiInfo[2 + 4 * index] << 8);
            temp |= (apiInfo[3 + 4 * index]);
            switch (index) {
                case 0 -> nintendontApiVersion = temp;
                case 1 -> maxInput = temp;
                case 2 -> maxOutput = temp;
                case 3 -> maxAddresses = temp;
                default -> throw new IllegalStateException("Too much information provided in Nintendont API call");
            }
        }
        log.debug("Connected to Nintendont Version: {}", nintendontApiVersion);
    }

    public void closeSocket() throws IOException {
        log.trace("Closing Nintendont Socket");
        try {
            this.inputStream.close();
            this.outputStream.close();
            this.socket.close();
        } catch (IOException e) {
            log.error("Failed to Close Nintendont Socket", e);
            throw e;
        }
    }

    public Boolean writeToSocket(NintendontOperation operation) {
        return true;
    }

    public void writeToSocket(byte[] toWrite) {
        try {
            outputStream.write(toWrite);
            outputStream.flush();
        } catch (IOException e) {
            log.error("Failed to Write to Data Stream.", e);
        }
    }

    public byte[] readFromSocket(Integer amount) throws IOException {
        return inputStream.readNBytes(amount);
    }
}
