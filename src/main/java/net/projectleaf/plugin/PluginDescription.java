package net.projectleaf.plugin;

import org.json.simple.JSONObject;

public class PluginDescription {

    private String author, name, main, version;

    private JSONObject input;

    public PluginDescription(JSONObject descriptor) throws Exception {
        this.input = descriptor;
        if(!validate()) {
            throw new Exception("Descriptor validation failed");
        } else {
            author = getInput().get("author").toString();
            name = getInput().get("name").toString();
            main = getInput().get("main").toString();
            version = getInput().get("version").toString();
        }
    }

    /**
     *
     * @return if the description.json is valid
     */
    public boolean validate() {
        return getInput().containsKey("author") && getInput().containsKey("name") && getInput().containsKey("main") && getInput().containsKey("version");
    }

    public JSONObject getInput() {
        return input;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public String getMain() {
        return main;
    }

    public String getVersion() {
        return version;
    }
}
