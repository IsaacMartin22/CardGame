package io.github.starterproject.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.system.JNI;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;
import org.lwjgl.system.Pointer;
import org.lwjgl.system.windows.User32;
import org.lwjgl.system.windows.WindowsLibrary;

import static org.lwjgl.system.MemoryUtil.memAddress;

final class WindowsTaskbar {
    private static final int ICON_SMALL = 0;
    private static final int ICON_BIG = 1;
    private static final int GCLP_HICON = -14;
    private static final int GCLP_HICONSM = -34;

    private WindowsTaskbar() {
    }

    static void initialize() {
        if (Platform.get() != Platform.WINDOWS) return;

        try (WindowsLibrary shell32 = new WindowsLibrary("shell32.dll");
             MemoryStack stack = MemoryStack.stackPush()) {
            long function = shell32.getFunctionAddress("SetCurrentProcessExplicitAppUserModelID");
            if (function == 0) {
                throw new IllegalStateException("Windows taskbar identity API is unavailable.");
            }
            int result = JNI.invokePI(
                memAddress(stack.UTF16("io.github.starterproject.SleighTheSpire")), function);
            if (result < 0) {
                throw new IllegalStateException(
                    "Could not set Windows taskbar identity: 0x" + Integer.toHexString(result));
            }
        }
    }

    static void setWindowIcons(Lwjgl3Window window) {
        if (Platform.get() != Platform.WINDOWS) return;

        long handle = GLFWNativeWin32.glfwGetWin32Window(window.getWindowHandle());
        try (WindowsLibrary user32 = new WindowsLibrary("user32.dll")) {
            // Use the pointer-sized return value; LWJGL's User32.SendMessage binding returns a boolean.
            long sendMessage = user32.getFunctionAddress("SendMessageW");
            if (sendMessage == 0) {
                throw new IllegalStateException("Windows window-icon API is unavailable.");
            }
            long setClassLong = user32.getFunctionAddress(
                Pointer.BITS64 ? "SetClassLongPtrW" : "SetClassLongW");
            long getClassLong = user32.getFunctionAddress(
                Pointer.BITS64 ? "GetClassLongPtrW" : "GetClassLongW");
            if (setClassLong == 0 || getClassLong == 0) {
                throw new IllegalStateException("Windows class-icon API is unavailable.");
            }
            setClassIcon(handle, ICON_SMALL, GCLP_HICONSM, sendMessage,
                setClassLong, getClassLong);
            setClassIcon(handle, ICON_BIG, GCLP_HICON, sendMessage,
                setClassLong, getClassLong);
        }
    }

    private static void setClassIcon(long window, int size, int classIndex,
                                     long sendMessage, long setClassLong,
                                     long getClassLong) {
        long icon = JNI.invokePPPP(window, User32.WM_GETICON, (long) size, 0L, sendMessage);
        if (icon == 0) {
            throw new IllegalStateException("The game window has no icon for the Windows taskbar.");
        }
        // GLFW owns these handles; share them with the class without destroying them here.
        JNI.invokePPP(window, classIndex, icon, setClassLong);
        // Verify the stored handle instead of relying on last-error state across JNI calls.
        if (JNI.invokePP(window, classIndex, getClassLong) != icon) {
            throw new IllegalStateException("Windows did not retain the game's class icon.");
        }
        JNI.invokePPPP(window, User32.WM_SETICON, (long) size, icon, sendMessage);
    }
}
