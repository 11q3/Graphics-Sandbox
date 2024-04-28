package org.elevenqtwo.util;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferAllocator {
    public static FloatBuffer allocateBuffer(float[] data) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static IntBuffer allocateBuffer(int[] data) {
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data).flip();
        return buffer;
    }
}
