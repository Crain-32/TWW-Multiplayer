package client.adapters;

import client.adapters.sockets.NintendontOperation;
import client.adapters.sockets.NintendontSocket;
import client.exceptions.GameHandlerDisconnectException;
import client.game.interfaces.MemoryAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Lazy
@Slf4j
@Component
public class NintendontAdapter implements MemoryAdapter {

    private NintendontSocket nintendontSocket;
    private final ConsoleConnectionConfig consoleInfo;

    public NintendontAdapter(ConsoleConnectionConfig consoleConnectionConfig) {
        this.consoleInfo = consoleConnectionConfig;
    }

    @Override
    public void connect() throws GameHandlerDisconnectException {
        this.nintendontSocket = new NintendontSocket(consoleInfo.getExternalIpAddress());
    }

    @Override
    public Boolean isConnected() {
        if (nintendontSocket == null) {
            nintendontSocket = new NintendontSocket(consoleInfo.getExternalIpAddress(), consoleInfo.getExternalPort());
        }
        return true;
    }

    @Override
    public Boolean disconnect() {
        this.nintendontSocket.closeSocket();
        // If the socket fails to close, we have bigger problems.
        return true;
    }

    @Override
    public Boolean writeByte(Integer consoleAddress, Byte byteVal) {
        try {
            nintendontSocket.writeToSocket(NintendontOperation.writeByteOperation(consoleAddress, byteVal));
            return nintendontSocket.readFromSocket(1)[0] == 0x01;
        } catch (IOException e) {
            log.debug("Failed to Read from Socket: ", e);
            return false;
        }
    }

    @Override
    public Byte readByte(Integer consoleAddress) {
        try {
            nintendontSocket.writeToSocket(NintendontOperation.readByteOperation(consoleAddress));
            return nintendontSocket.readFromSocket(2)[1];
        } catch (IOException e) {
            log.debug("Failed to Read from Socket: ", e);
            return 0x00;
        }
    }

    @Override
    public Boolean writeShort(Integer consoleAddress, Short shortVal) {
        try {
            nintendontSocket.writeToSocket(NintendontOperation.writeShortOperation(consoleAddress, shortVal));
            return nintendontSocket.readFromSocket(1)[0] == 0x01;
        } catch (IOException e) {
            log.debug("Failed to Read from Socket: ", e);
            return false;
        }
    }

    @Override
    public Short readShort(Integer consoleAddress) {
        try {
            nintendontSocket.writeToSocket(NintendontOperation.readShortOperation(consoleAddress));
            var confirmationByte = nintendontSocket.readFromSocket(1)[0] == 0x01;
            if (!confirmationByte) {
                throw new GameHandlerDisconnectException("An Error Likely Occurred when talking to the Console");
            }
            String val = DatatypeConverter.printHexBinary(nintendontSocket.readFromSocket(2));
            return Short.parseShort(val, 16);
        } catch (IOException e) {
            log.debug("Failed to Read from Socket: ", e);
            return 0x00;
        }
    }

    @Override
    public Boolean writeInteger(Integer consoleAddress, Integer integerVal) {
        try {
            nintendontSocket.writeToSocket(NintendontOperation.writeIntegerOperation(consoleAddress, integerVal));
            return nintendontSocket.readFromSocket(1)[0] == 0x01;
        } catch (IOException e) {
            log.debug("Failed to Read from Socket: ", e);
            return false;
        }
    }

    @Override
    public Integer readInteger(Integer consoleAddress) {
        try {
            nintendontSocket.writeToSocket(NintendontOperation.readIntegerOperation(consoleAddress));
            var confirmationByte = nintendontSocket.readFromSocket(1)[0] == 0x01;
            if (!confirmationByte) {
                throw new GameHandlerDisconnectException("An Error Likely Occurred when talking to the Console");
            }
            String val = DatatypeConverter.printHexBinary(nintendontSocket.readFromSocket(4));
            return Integer.parseInt(val, 16);
        } catch (IOException e) {
            log.debug("Failed to Read from Socket: ", e);
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
                 if (!confirmationByte) {
                     throw new GameHandlerDisconnectException("An Error Likely Occurred when talking to the Console");
                 }
                 var socketBytes = nintendontSocket.readFromSocket(4);

                 return new String(socketBytes, StandardCharsets.UTF_8);
             }
             nintendontSocket.writeToSocket(NintendontOperation.readNOperation(consoleAddress,(byte) (0xFF & stringLength)));
             var confirmationByte = nintendontSocket.readFromSocket(1)[0] == 0x01;
             if (!confirmationByte) {
                 throw new GameHandlerDisconnectException("An Error Likely Occurred when talking to the Console");
             }
             var socketBytes = nintendontSocket.readFromSocket(stringLength);

             return new String(socketBytes, StandardCharsets.UTF_8);
         } catch (IOException e) {
             log.debug("Failed to Read from Socket: ", e);
             return "";
         }
    }
}
