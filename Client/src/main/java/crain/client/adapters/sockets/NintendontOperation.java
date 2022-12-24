package crain.client.adapters.sockets;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.ArrayUtils;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class NintendontOperation {

    public static byte[] readNOperation(Integer consoleAddress, Byte byteCount) {
        var header = new byte[]{0x00, 0x01, 0x01, 0x01};
        var address = new byte[6];
        address[0] = (byte) (0xFF & (consoleAddress >>> 24));
        address[1] = (byte) (0xFF & (consoleAddress >>> 16));
        address[2] = (byte) (0xFF & (consoleAddress >>> 8));
        address[3] = (byte) (0xFF &  consoleAddress);
        address[4] = (byte) 0x80;
        address[5] = byteCount;
        return ArrayUtils.addAll(header, address);
    }
    public static byte[] readByteOperation(Integer consoleAddress) {
        return readNOperation(consoleAddress, (byte) 1);
    }

    public static byte[] writeByteOperation(Integer consoleAddress, Byte bite) {
        var header = new byte[]{0x00, 0x01, 0x01, 0x01};
        var address = new byte[7];
        address[0] = (byte) (0xFF & (consoleAddress >>> 24));
        address[1] = (byte) (0xFF & (consoleAddress >>> 16));
        address[2] = (byte) (0xFF & (consoleAddress >>> 8));
        address[3] = (byte) (0xFF &  consoleAddress);
        address[4] = (byte) 0x40;
        address[5] = (byte) 0x01;
        address[6] = bite;
        return ArrayUtils.addAll(header, address);
    }



    public static byte[] readShortOperation(Integer consoleAddress) {
        return readNOperation(consoleAddress, (byte) 2);
    }

    public static byte[] writeShortOperation(Integer consoleAddress, Short value) {
        var header = new byte[]{0x00, 0x01, 0x01, 0x01};
        var address = new byte[8];
        address[0] = (byte) (0xFF & (consoleAddress >>> 24));
        address[1] = (byte) (0xFF & (consoleAddress >>> 16));
        address[2] = (byte) (0xFF & (consoleAddress >>> 8));
        address[3] = (byte) (0xFF &  consoleAddress);
        address[4] = (byte) 0x40;
        address[5] = (byte) 0x02;
        address[6] = (byte) (0xFF & (value >>> 8));
        address[7] = (byte) (0xFF &  value);
        return ArrayUtils.addAll(header, address);
    }

    public static byte[] readIntegerOperation(Integer consoleAddress) {
        if (consoleAddress % 4 != 0) {
            throw new IllegalArgumentException("Unaligned Integer Request");
        }
        var header = new byte[]{0x00, 0x01, 0x01, 0x01};
        var address = new byte[5];
        address[0] = (byte) (0xFF & (consoleAddress >>> 24));
        address[1] = (byte) (0xFF & (consoleAddress >>> 16));
        address[2] = (byte) (0xFF & (consoleAddress >>> 8));
        address[3] = (byte) (0xFF &  consoleAddress);
        address[4] = (byte) 0xA0;
        return ArrayUtils.addAll(header, address);
    }

    public static byte[] writeIntegerOperation(Integer consoleAddress, Integer value) {
        if (consoleAddress % 4 != 0) {
            throw new IllegalArgumentException("Unaligned Integer Request");
        }
        var header = new byte[]{0x00, 0x01, 0x01, 0x01};
        var address = new byte[9];
        address[0] = (byte) (0xFF & (consoleAddress >>> 24));
        address[1] = (byte) (0xFF & (consoleAddress >>> 16));
        address[2] = (byte) (0xFF & (consoleAddress >>> 8));
        address[3] = (byte) (0xFF &  consoleAddress);
        address[4] = (byte) 0x60;
        address[5] = (byte) (0xFF & (value >>> 24));
        address[6] = (byte) (0xFF & (value >>> 16));
        address[7] = (byte) (0xFF & (value >>> 8));
        address[8] = (byte) (0xFF &  value);
        return ArrayUtils.addAll(header, address);
    }

    public static byte[] getApiDetailsPacket() {
        return new byte[]{0x01, 0x00, 0x00, 0x01};
    }
}
