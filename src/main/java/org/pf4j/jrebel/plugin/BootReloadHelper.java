package org.pf4j.jrebel.plugin;

import org.pf4j.demo.Boot;
import org.zeroturnaround.javarebel.ReloaderFactory;
import org.zeroturnaround.javarebel.integration.generic.ClassEventListenerAdapter;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class BootReloadHelper {

  public static void addClassChangeListener(final Boot boot) throws Exception {
    // Class event listeners are called when a class is reloaded by JRebel.
    // You can order the reload listeners using the priority parameter in the constructor.
    System.out.println("Class Change Listener Has Been Called");
    ReloaderFactory.getInstance().addClassReloadListener(new ClassEventListenerAdapter(0) {
      @Override
      public void onClassEvent(int eventType, Class<?> klass) throws Exception {
        System.out.println("Need to reload plugin now for class " + klass.getName() + " Plugin dir = " + boot
            .getPluginManager().getPluginsRoot().toString());

        String pluginShortName = null;
        String pluginName = null;
        if (klass.getName().startsWith("org.pf4j.demo.welcome")) {
          pluginShortName = "plugin1";
          pluginName = "welcome-plugin";
        } else if (klass.getName().startsWith("org.pf4j.demo.hello")) {
          pluginShortName = "plugin2";
          pluginName = "hello-plugin";
        }
        if (pluginShortName != null && pluginName != null) {
          String pf4jBootHome = FileSystems.getDefault().getPath(".").toAbsolutePath().toString();
          String gradlew = pf4jBootHome + "/gradlew";
          String pluginPath = pf4jBootHome + "/plugins/" + pluginShortName + "/build/libs/plugin-" + pluginName +
              "-0.0.1.zip";
          Process gradleProcess = new ProcessBuilder().command(gradlew, ":plugins:" + pluginShortName + ":clean",
              ":plugins:" + pluginShortName + ":assemblePlugin", "-x", "test").start();
          inheritIO(gradleProcess.getInputStream(), System.out);
          inheritIO(gradleProcess.getErrorStream(), System.err);
          try {
            if (!gradleProcess.waitFor(5, TimeUnit.MINUTES)) {
              System.out.println("Giving up waiting for plugin to build. Taking too long. Will not automatically " +
                  "redeploy.");
              gradleProcess.destroyForcibly();
              return;
            }
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
          if (gradleProcess.exitValue() != 0) {
            System.out.println("Will not auto-deploy plugin. Exit code != 0.");
            return;
          }
          boot.getPluginManager().stopPlugin(pluginName);
          boot.getPluginManager().unloadPlugin(pluginName);
          boot.getPluginManager().loadPlugin(Paths.get(pluginPath));
          boot.getPluginManager().startPlugin(pluginName);
        }
      }
    });
  }

  private static void inheritIO(final InputStream src, final PrintStream dest) {
    new Thread(() -> {
      Scanner sc = new Scanner(src);
      while (sc.hasNextLine()) {
        dest.println(sc.nextLine());
      }
    }).start();
  }
}
