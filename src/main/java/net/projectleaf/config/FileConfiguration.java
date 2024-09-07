package net.projectleaf.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileConfiguration {

    private String filename;

    private Map<String, String> map;

    public FileConfiguration(String filename) {
        this.filename = filename;
    }

    /**
     *
     * @return {@link String} the Filename of the configuration
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Copies the default lines
     * @param defaults {@link String[]} Array of default options for the config
     */
    public void copyDefaults(String[] defaults) {
        File f = new File(getFilename());
        if (!f.exists()) {
            try {
                f.createNewFile();
                FileWriter fw = new FileWriter(f);
                for (int i = 0; i < defaults.length; i++) {
                    fw.write(defaults[i] + "\n");
                }
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @return {@link Map<String, String>} returns a map of all keys and values
     */
    public Map<String, String> read() {
        Map<String, String> keyset = new HashMap<>();
        File f = new File(getFilename());
        if (f.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String n = "";
                while ((n = reader.readLine()) != null) {
                    if (!n.startsWith("#")) {
                        String key = n.split("=")[0];
                        String value = n.substring(key.length() + 1);
                        keyset.put(key, value);
                    }
                }
                reader.close();
                return keyset;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                throw new Exception("Cannot read Configuration file '" + getFilename() + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     *
     * @param key {@link String} Configuration key
     * @return if the map contains the key
     */
    public boolean contains(String key) {
        if(this.map == null) {
            this.map = read();
        }
        return this.map.containsKey(key);
    }

    /**
     *
     * @param key {@link String} Configuration key
     * @return String value if key is present
     */
    public String getString(String key) {
        if (this.map == null) {
            this.map = read();
        }
        return this.map.get(key);
    }

    /**
     *
     * @param key {@link String} Configuration key
     * @return Integer value if key is present
     */
    public int getInt(String key) {
        if (this.map == null) {
            this.map = read();
        }
        return Integer.parseInt(this.map.get(key));
    }

    /**
     *
     * @param key {@link String} Configuration key
     * @return boolean value if key is present
     */
    public boolean getBoolean(String key) {
        if (this.map == null) {
            this.map = read();
        }
        return Boolean.parseBoolean(this.map.get(key));
    }

}
