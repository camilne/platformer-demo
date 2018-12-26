package camilne.engine;

import org.lwjgl.opengl.GLDebugMessageCallback;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLUtil {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";

    private static GLDebugMessageCallback cb;

    public static void checkError() {
        var error = glGetError();
        while (error != GL_NO_ERROR) {
            new RuntimeException("OpenGL error " + error).printStackTrace();
            error = glGetError();
        }
    }

    public static void init() {
        glEnable(GL_DEBUG_OUTPUT_SYNCHRONOUS);
        checkError();
        glDebugMessageControl(GL_DONT_CARE, GL_DONT_CARE, GL_DONT_CARE, (IntBuffer) null, true);
        // Disable NVIDIA buffer memory location spam
        glDebugMessageControl(GL_DEBUG_SOURCE_API, GL_DEBUG_TYPE_OTHER, GL_DONT_CARE, 131185, false);
        checkError();
        cb = GLDebugMessageCallback.create(GLUtil::glErrorCallback);
        glDebugMessageCallback(cb, NULL);
        checkError();
    }

    public static void destroy() {
        if (cb != null) {
            cb.free();
        }
    }

    private static void glErrorCallback(int source, int type, int id, int severity, int length, long message, long param) {
        System.out.println(ANSI_YELLOW + "OpenGL Error:" + ANSI_RESET);
        System.out.println("=============");

        System.out.println(" Object ID: " + ANSI_BLUE + id + ANSI_RESET);
        System.out.println(" Severity:  " + getSeverityColor(severity) + getSeverityString(severity) + ANSI_RESET);

        System.out.println(" Type:      " + getTypeString(type));
        System.out.println(" Source:    " + getSourceString(source));
        System.out.println(" Message:   " + GLDebugMessageCallback.getMessage(length, message));
        System.out.println();
    }

    private static String getSeverityColor(int severity) {
        switch (severity) {
            case GL_DEBUG_SEVERITY_HIGH:
                return ANSI_RED;
            case GL_DEBUG_SEVERITY_MEDIUM:
                return ANSI_YELLOW;
            case GL_DEBUG_SEVERITY_LOW:
                return ANSI_GREEN;
            default:
                return ANSI_RESET;
        }
    }

    private static String getSeverityString(int severity) {
        switch (severity) {
            case GL_DEBUG_SEVERITY_HIGH:
                return "High";
            case GL_DEBUG_SEVERITY_MEDIUM:
                return "Medium";
            case GL_DEBUG_SEVERITY_LOW:
                return "Low";
            default:
                return "Unknown";
        }
    }

    private static String getTypeString(int type) {
        switch (type) {
            case GL_DEBUG_TYPE_ERROR:
                return "Error";
            case GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR:
                return "Deprecated Behavior";
            case GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR:
                return "Undefined Behavior";
            case GL_DEBUG_TYPE_PORTABILITY:
                return "Portability";
            case GL_DEBUG_TYPE_PERFORMANCE:
                return "Performance";
            case GL_DEBUG_TYPE_OTHER:
                return "Other";
            default:
                return "Unknown";
        }
    }

    private static String getSourceString(int source) {
        switch (source) {
            case GL_DEBUG_SOURCE_API:
                return "API";
            case GL_DEBUG_SOURCE_WINDOW_SYSTEM:
                return "Window System";
            case GL_DEBUG_SOURCE_SHADER_COMPILER:
                return "Shader Compiler";
            case GL_DEBUG_SOURCE_THIRD_PARTY:
                return "Third Party";
            case GL_DEBUG_SOURCE_APPLICATION:
                return "Application";
            case GL_DEBUG_SOURCE_OTHER:
                return "Other";
            default:
                return "Unknown";
        }
    }

}
