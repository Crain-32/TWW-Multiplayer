package crain.client.adapters;

import crain.client.adapters.sockets.NintendontOperation;
import crain.client.adapters.sockets.NintendontSocket;
import crain.client.communication.CommunicationState;
import crain.client.exceptions.memory.MissingGameAdapterException;
import crain.client.game.interfaces.MemoryAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Lazy
@Slf4j
@Component
public class NintendontAdapter implements MemoryAdapter {

    private NintendontSocket nintendontSocket;
    private final ConsoleConnectionConfig consoleInfo;
    private final ApplicationEventPublisher publisher;

    public NintendontAdapter(ConsoleConnectionConfig consoleConnectionConfig, ApplicationEventPublisher publisher) {
        this.consoleInfo = consoleConnectionConfig;
        this.publisher = publisher;
    }

    @Override
    public void connect() throws MissingGameAdapterException {
        try {
            this.nintendontSocket = new NintendontSocket(consoleInfo.getExternalIpAddress());
            publisher.publishEvent(new CommunicationState.ToggleGameConnectionEvent(true));
        } catch (IOException e) {
            log.error("Failed to connect to Nintendont: {}", e.getMessage(), e);
            publisher.publishEvent(new CommunicationState.ToggleGameConnectionEvent(false));
        }
    }

    @Override
    public Boolean isConnected() {
        if (nintendontSocket == null) {
            try {
                nintendontSocket = new NintendontSocket(consoleInfo.getExternalIpAddress(), consoleInfo.getExternalPort());
            } catch (IOException e) {
                log.error("Failed to connect to Nintendont: {}", e.getMessage(), e);

            }
        }
        publisher.publishEvent(new CommunicationState.ToggleGameConnectionEvent(nintendontSocket != null));
        return nintendontSocket != null;
    }

    @Override
    public Boolean disconnect() {
        try {
            this.nintendontSocket.closeSocket();
            this.nintendontSocket = null;
        } catch (IOException ignore) {

        }
        publisher.publishEvent(new CommunicationState.ToggleGameConnectionEvent(false));
        // If the socket fails to close, we have bigger problems.
        return true;
    }

    @Override
    public Boolean writeByte(Integer consoleAddress, Byte byteVal) {
        try {
            nintendontSocket.writeToSocket(NintendontOperation.writeByteOperation(consoleAddress, byteVal));
            return nintendontSocket.readFromSocket(1)[0] == 0x01;
        } catch (IOException e) {
            log.error("Failed to Read from Socket", e);
            return false;
        }
    }

    @Override
    public Byte readByte(Integer consoleAddress) {
        try {
            nintendontSocket.writeToSocket(NintendontOperation.readByteOperation(consoleAddress));
            return nintendontSocket.readFromSocket(2)[1];
        } catch (IOException e) {
            log.error("Failed to Read from Socket", e);
            return 0x00;
        }
    }

    @Override
    public Boolean writeShort(Integer consoleAddress, Short shortVal) {
        try {
            nintendontSocket.writeToSocket(NintendontOperation.writeShortOperation(consoleAddress, shortVal));
            return nintendontSocket.readFromSocket(1)[0] == 0x01;
        } catch (IOException e) {
            log.error("Failed to Read Confirmation Byte from Socket", e);
            return false;
        }
    }

    @Override
    public Short readShort(Integer consoleAddress) {
        try {
            String val;
            nintendontSocket.writeToSocket(NintendontOperation.readShortOperation(consoleAddress));
            var confirmationByte = nintendontSocket.readFromSocket(1)[0] == 0x01;
            if (confirmationByte) {
                val = toHexString(nintendontSocket.readFromSocket(2));
            } else {
                log.error("An Error Likely Occurred when talking to the Console, Missing Confirmation Byte");
                val = "00";
            }
            return Short.parseShort(val, 16);
        } catch (IOException e) {
            log.error("Failed to Read from Socket", e);
            return 0x00;
        }
    }

    @Override
    public Boolean writeInteger(Integer consoleAddress, Integer integerVal) {
        try {
            nintendontSocket.writeToSocket(NintendontOperation.writeIntegerOperation(consoleAddress, integerVal));
            return nintendontSocket.readFromSocket(1)[0] == 0x01;
        } catch (IOException e) {
            log.error("Failed to Read from Socket", e);
            return false;
        }
    }

    @Override
    public Integer readInteger(Integer consoleAddress) {
        try {
            String val;
            nintendontSocket.writeToSocket(NintendontOperation.readIntegerOperation(consoleAddress));
            var confirmationByte = nintendontSocket.readFromSocket(1)[0] == 0x01;
            if (confirmationByte) {
                val = toHexString(nintendontSocket.readFromSocket(4));
            } else {
                log.error("An Error Likely Occurred when talking to the Console, Missing Confirmation Byte");
                val = "00";
            }
            return Integer.parseInt(val, 16);
        } catch (IOException e) {
            log.error("Failed to Read from Socket", e);
            return 0x00;
        }
    }

    @Override
    public Boolean writeString(Integer consoleAddress, String stringVal) {
        return false;
    }

    @Override
    public String readString(Integer consoleAddress, Integer stringLength) {
        try {
            if (stringLength == 4) {
                nintendontSocket.writeToSocket(NintendontOperation.readIntegerOperation(consoleAddress));
                var confirmationByte = nintendontSocket.readFromSocket(1)[0] == 0x01;
                byte[] socketBytes;
                if (confirmationByte) {
                    socketBytes = nintendontSocket.readFromSocket(4);
                } else {
                    log.error("An Error Likely Occurred when talking to the Console, Missing Confirmation Byte");
                    socketBytes = new byte[0];
                }
                return new String(socketBytes, StandardCharsets.UTF_8);
            }
            nintendontSocket.writeToSocket(NintendontOperation.readNOperation(consoleAddress, (byte) (0xFF & stringLength)));
            var confirmationByte = nintendontSocket.readFromSocket(1)[0] == 0x01;
            if (!confirmationByte) {
                log.error("Confirmation Byte was false when reading from Console Address: {}", consoleAddress);
                throw new IOException("An Error Likely Occurred when talking to the Console");
            }
            var socketBytes = nintendontSocket.readFromSocket(stringLength);
            return new String(socketBytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Failed to Read from Socket", e);
            return "";
        }
    }

    // See https://www.baeldung.com/java-byte-arrays-hex-strings
    private String toHexString(byte[] byteArr) {
        char[] hexChars = new char[byteArr.length * 2];
        for (int index = 0; index < byteArr.length; index++) {
            hexChars[index] = Character.forDigit((byteArr[index] >> 4) & 0xF, 16);
            hexChars[index] = Character.forDigit(byteArr[index] & 0xF, 16);
        }
        return new String(hexChars);
    }
}
