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
        System.out.println("Need to reload plugin now for class " + klass.getName() + " Plugin dir = " + boot.getPluginManager().getPluginsRoot().toString());

        if (klass.getName().equals("org.pf4j.demo.hello.HelloPlugin")) {
          String pf4jBootHome = FileSystems.getDefault().getPath(".").toAbsolutePath().toString();
          String gradlew = pf4jBootHome + "/gradlew";
          String pluginPath = pf4jBootHome + "/plugins/plugin2/build/libs/plugin-hello-plugin-0.0.1.zip";
          Process gradleProcess = new ProcessBuilder().command(gradlew, ":plugins:plugin2:clean", ":plugins:plugin2:assemblePlugin", "-x", "test").start();
          inheritIO(gradleProcess.getInputStream(), System.out);
          inheritIO(gradleProcess.getErrorStream(), System.err);
          try {
            if (!gradleProcess.waitFor(5, TimeUnit.MINUTES)) {
              System.out.println("Giving up waiting for plugin to build. Taking too long. Will not automatically redeploy.");
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
          boot.getPluginManager().stopPlugin("hello-plugin");
          boot.getPluginManager().unloadPlugin("hello-plugin");
          boot.getPluginManager().loadPlugin(Paths.get(pluginPath));
          boot.getPluginManager().startPlugin("hello-plugin");
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
