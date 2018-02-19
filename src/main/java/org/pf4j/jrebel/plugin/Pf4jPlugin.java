package org.pf4j.jrebel.plugin;

import org.zeroturnaround.javarebel.ClassResourceSource;
import org.zeroturnaround.javarebel.Integration;
import org.zeroturnaround.javarebel.IntegrationFactory;
import org.zeroturnaround.javarebel.Plugin;

public class Pf4jPlugin implements Plugin {

  @Override
  public void preinit() {
    Integration integration = IntegrationFactory.getInstance();
    ClassLoader cl = getClass().getClassLoader();

    integration.addIntegrationProcessor(cl,
        new String[] { "org.pf4j.demo.Boot" },
        new Pf4jClassCBP());
  }

  @Override
  public boolean checkDependencies(ClassLoader cl, ClassResourceSource crs) {
    return cl.toString().contains("pf4j") || (crs != null && crs.getClassResource("org.pf4j.demo.Boot") != null);
  }

  @Override
  public String getId() {
    return "pf4j";
  }

  @Override
  public String getName() {
    return "PF4J";
  }

  @Override
  public String getDescription() {
    return "PF4J Jrebel Plugin";
  }

  @Override
  public String getAuthor() {
    return "Nicholas DiPiazza";
  }

  @Override
  public String getWebsite() {
    return "https://github.com/pf4j/pf4j";
  }

  @Override
  public String getSupportedVersions() {
    return null;
  }

  @Override
  public String getTestedVersions() {
    return null;
  }
}
