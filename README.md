 # JRebel Plugin for PF4J
 
 The following plugin will allow hot-deploy of PF4J plugins using JRebel.
 
 Without this plugin, JRebel will fail to reload PF4J changes. 
 
 The plugin will do the following when a PF4J plugin is modified:
 
 1) Rebuild the plugin, and will not proceed to next step if it fails or times out.
 1) Stop the plugin.
 1) Unload the plugin.
 1) Load the plugin anew.
 1) Start the plugin.

# How to use

I assume you have your IDE with the JRebel IDE plugin installed. 

 1) In order to show this working, we need to slightly modify the demo_gradle project to be a little more realistic. So `git clone -b JrebelTest git@github.com:nddipiazza/pf4j.git`
 1) `cd pf4j/demo_gradle`
 1) `./gradlew build -x test; mvn install:install-file -Dfile=/home/ndipiazza/lucidworks/pf4j/demo_gradle/app/build/libs/app-plugin-demo-uberjar.jar -DgroupId=org.pf4j -DartifactId=demo_gradle -Dversion=2.3.0 -Dpackaging=jar;`
 1) git clone [this|https://github.com/nddipiazza/JRebelPf4jPlugin] repository
 1) Import your *pf4j/demo_gradle* into your IDE
 1) Go to JRebel view in IDE and enable JRebel for the project.
 1) `cd JRebelPf4jPlugin`
 1) `mvn clean package`
 1) Now run the example: `java -jar -Dpf4j.pluginsDir=build/plugins -agentpath:/home/ndipiazza/Downloads/jrebel/lib/libjrebel64.so -Drebel.plugins=/home/ndipiazza/lucidworks/jrebel-pf4j-plugin/target/jrebel-plugin-2.3.0.jar app/build/libs/app-plugin-demo-uberjar.jar`
 
 
