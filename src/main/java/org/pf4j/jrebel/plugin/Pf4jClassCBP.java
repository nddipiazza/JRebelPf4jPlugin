package org.pf4j.jrebel.plugin;

import org.zeroturnaround.bundled.javassist.ClassPool;
import org.zeroturnaround.bundled.javassist.CtClass;
import org.zeroturnaround.bundled.javassist.CtMethod;
import org.zeroturnaround.javarebel.integration.support.JavassistClassBytecodeProcessor;

public class Pf4jClassCBP extends JavassistClassBytecodeProcessor {
  @Override
  public void process(ClassPool cp, ClassLoader cl, CtClass ctClass) throws Exception {
    // Add a reload listener for the Boot so that all class changes will register a boot listener.
    if (ctClass.getName().equals("org.pf4j.demo.Boot")) {
      ctClass.getDeclaredConstructor(new CtClass[] {}).insertAfter("org.pf4j.jrebel.plugin.BootReloadHelper" +
          ".addClassChangeListener(this);");
    }
  }
}
