 # JRebel Plugin for PF4J
 
 The following plugin will allow JRebel to be used with PF4J.
 
 The class reloading of PF4J plugins will be ignored as is. 
 
 The plugin will do the following when a PF4J plugin is modified:
 
 1) Rebuild the plugin, and will not proceed to next step if it fails or times out.
 1) Stop the plugin.
 1) Unload the plugin.
 1) Load the plugin anew.
 1) Start the plugin.