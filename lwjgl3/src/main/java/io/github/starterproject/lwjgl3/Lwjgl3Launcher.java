package io.github.starterproject.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import io.github.starterproject.game.TheGameClass;

import javax.imageio.ImageIO;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.net.URL;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        WindowsTaskbar.initialize();
        TrayIcon trayIcon = createTrayIcon();
        try {
            createApplication();
        } finally {
            if (trayIcon != null) SystemTray.getSystemTray().remove(trayIcon);
        }
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new TheGameClass(), getDefaultConfiguration());
    }

    private static TrayIcon createTrayIcon() {
        if (!SystemTray.isSupported()) {
            System.err.println("System tray is not supported on this desktop.");
            return null;
        }

        URL logo = Lwjgl3Launcher.class.getResource("/logo.png");
        if (logo == null) {
            System.err.println("Could not find logo.png for the system tray.");
            return null;
        }

        try {
            Image image = ImageIO.read(logo);
            if (image == null) {
                System.err.println("Could not decode logo.png for the system tray.");
                return null;
            }
            TrayIcon icon = new TrayIcon(image, "Sleigh the Spire");
            icon.setImageAutoSize(true);
            SystemTray.getSystemTray().add(icon);
            return icon;
        } catch (IOException | AWTException e) {
            System.err.println("Could not create the system tray icon: " + e.getMessage());
            return null;
        }
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Sleigh the Spire");
        //// Vsync limits the frames per second to what your hardware can display, and helps eliminate
        //// screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
        configuration.useVsync(true);
        //// Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
        //// refresh rates. The Vsync setting above should limit the actual FPS to match the monitor.
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        //// useful for testing performance, but can also be very stressful to some hardware.
        //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.

        //configuration.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        configuration.setWindowedMode(1280, 720);

        //// You can change these files; they are in lwjgl3/src/main/resources/ .
        //// They can also be loaded from the root of assets/ .
        configuration.setWindowIcon("logo.png");
        configuration.setWindowListener(new Lwjgl3WindowAdapter() {
            @Override
            public void created(Lwjgl3Window window) {
                WindowsTaskbar.setWindowIcons(window);
            }
        });

        //// This could improve compatibility with Windows machines with buggy OpenGL drivers, Macs
        //// with Apple Silicon that have to emulate compatibility with OpenGL anyway, and more.
        //// This uses the dependency `com.badlogicgames.gdx:gdx-lwjgl3-angle` to function.
        //// You would need to add this line to lwjgl3/build.gradle , below the dependency on `gdx-backend-lwjgl3`:
        ////     implementation "com.badlogicgames.gdx:gdx-lwjgl3-angle:$gdxVersion"
        //// You can choose to add the following line and the mentioned dependency if you want; they
        //// are not intended for games that use GL30 (which is compatibility with OpenGL ES 3.0).
        //// Know that it might not work well in some cases.
//        configuration.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.ANGLE_GLES20, 0, 0);

        return configuration;
    }
}
