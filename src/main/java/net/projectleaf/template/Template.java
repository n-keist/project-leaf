package net.projectleaf.template;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Template {

    private String name, version;
    private List<String> excluded_;
    private File source;


    /**
     *
     * @param source {@link File} The template directory
     * @param object {@link JSONObject} The template descriptor
     */
    public Template(File source, JSONObject object) {
        this.name = object.get("name").toString();
        this.version = object.get("minecraft-version").toString();
        JSONArray array = (JSONArray) object.get("excluded-files");
        List<String> list = new ArrayList<>();
        for(Object o : array) {
            list.add(o.toString());
        }
        this.excluded_ = list;
        this.source = source;
    }


    /**
     *
     * @return {@link String} the template name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return {@link String} Minecraft version of the template
     */
    public String getVersion() {
        return version;
    }

    /**
     * Files which have been told to exclude at the copy process
     * @return {@link List<String>} excluded file names
     */
    public List<String> getExcluded() {
        return excluded_;
    }

    /**
     * This method creates an instance from a template
     * @param dest {@link File} target instance directory
     */
    public void copy(File dest) {
        try {
            FileUtils.deleteDirectory(dest);
            FileUtils.forceMkdir(dest);
            FileUtils.copyDirectory(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
