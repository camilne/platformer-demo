package camilne.engine;

import org.lwjgl.BufferUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

public final class IOUtil {

    private IOUtil() {
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        var source = IOUtil.class.getClassLoader().getResourceAsStream(resource);
        if (source == null) {
            throw new FileNotFoundException("Resource does not exist: " + resource);
        }

        try (var rbc = Channels.newChannel(source)) {
            var buffer = BufferUtils.createByteBuffer(bufferSize);

            while (true) {
                var bytes = rbc.read(buffer);
                if (bytes == -1) {
                    break;
                }
                if (buffer.remaining() == 0) {
                    buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
                }
            }

            buffer.flip();
            return buffer.slice();
        }
    }
}
