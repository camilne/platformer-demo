package camilne.engine;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IOUtilTest {

    @Test
    void canLoadResourceToByteBuffer() throws Exception {
        var buffer = IOUtil.ioResourceToByteBuffer("resource.txt", 1024);

        assertEquals(1, buffer.limit());

        buffer = IOUtil.ioResourceToByteBuffer("resource.txt", 16);

        assertEquals(1, buffer.limit());
    }

    @Test
    void canLoadResourceWithSmallBuffer() throws IOException {
        var buffer = IOUtil.ioResourceToByteBuffer("buffer.txt", 4);

        assertEquals(10, buffer.limit());
    }

    @Test
    void doesThrowExceptionWhenResourceDoesNotExist() {
        assertThrows(IOException.class, () -> IOUtil.ioResourceToByteBuffer("does_not_exist", 1024));
    }

}
