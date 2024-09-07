package net.projectleaf.plugin;

import net.projectleaf.plugin.event.Listener;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager {

    private static PluginManager instance;

    private Map<File, PluginDescription> files_found;
    private Map<LeafPlugin, PluginDescription> initialized;
    private Map<LeafPlugin, List<Listener>> listeners;

    private PluginManager() {
        this.files_found = new HashMap<>();
        this.initialized = new HashMap<>();
        this.listeners = new HashMap<>();
    }

    public static PluginManager getInstance() {
        if(instance == null) {
            instance = new PluginManager();
        }
        return instance;
    }

    /**
     * Registers a Listener from a Plugin
     * @param plugin {@link LeafPlugin} A Plugin instance
     * @param listener {@link Listener} A Listener instance
     * @return {@link PluginManager} Instance
     */
    public PluginManager registerListener(LeafPlugin plugin, Listener listener) {
        List<Listener> lis;
        if(listeners.containsKey(plugin)) {
            lis = listeners.get(plugin);
        } else {
            lis = new ArrayList<>();
        }

        lis.add(listener);

        listeners.put(plugin, lis);

        return this;
    }

    /**
     * Unregisters a Listener from a Plugin
     * @param plugin {@link LeafPlugin} A Plugin instance
     * @param listener {@link Listener} A Listener instance
     * @return {@link PluginManager} Instance
     */
    public PluginManager unregisterListener(LeafPlugin plugin, Listener listener) {
        List<Listener> lis;
        if(listeners.containsKey(plugin)) {
            lis = listeners.get(plugin);
        } else {
            lis = new ArrayList<>();
        }

        if(lis.contains(listener)) {
            lis.remove(listener);
        }

        listeners.put(plugin, lis);

        return this;
    }

    /**
     * Reads the /plugins folder and loads correct plugins
     * @return {@link PluginManager} Instance
     */
    public PluginManager findPlugins() {
        FileUtils.listFiles(new File("plugins/"), new String[] { "jar" }, false).forEach(f -> {
            try {
                JarFile jar = new JarFile(f);
                JarEntry desc = jar.getJarEntry("description.json");
                if (desc == null) {
                    System.out.println("Cannot enable " + f.getName() + " -> No description.json");
                } else {
                    JSONParser parser = new JSONParser();
                    InputStream in = jar.getInputStream(desc);
                    JSONObject pluginDescription = (JSONObject) parser.parse(new InputStreamReader(in));

                    try {
                        PluginDescription description = new PluginDescription(pluginDescription);
                        files_found.put(f, description);
                    } catch (Exception ex) {
                        System.out.println("Unable to Initialize File '" + f.getName() + "' \n-> " + ex.getMessage());
                    }
                }
                jar.close();
            } catch (Exception e) {
            }
        });
        System.out.println("Found " + files_found.size() + " possible plugin(s)");
        return this;
    }

    /**
     * initializes found plugins
     * @return {@link PluginManager} Instance
     */
    public PluginManager initPlugins() {
        files_found.keySet().forEach(file -> {
            PluginDescription description = files_found.get(file);
            String main = description.getMain();
            try {
                URLClassLoader classLoader = new URLClassLoader(new URL[] { file.toURI().toURL() });
                Class<? extends LeafPlugin> moduleClass = (Class<? extends LeafPlugin>) classLoader.loadClass(main);
                LeafPlugin module = (LeafPlugin) moduleClass.newInstance();
                module.description = description;
                module.name = description.getName();
                module.author = description.getAuthor();

                module.onEnable();
                initialized.put(module, description);
                classLoader.close();
            } catch (ClassNotFoundException | InstantiationException
                    | IllegalAccessException | IOException e) {
                e.printStackTrace();
            }
        });
        return this;
    }

    public EventManager getEventManager() {
        return EventManager.getInstance();
    }

    protected Map<LeafPlugin, List<Listener>> getListeners() {
        return listeners;
    }
}
