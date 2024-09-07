package net.projectleaf.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TemplateManager {

    private static TemplateManager instance;

    private List<File> templateDirs;
    private List<Template> templates;
    private JSONParser parser;

    private TemplateManager() {
        this.templateDirs = new ArrayList<File>();
        this.parser = new JSONParser();
        this.templates = new ArrayList<Template>();
    }

    /**
     *
     * @return {@link TemplateManager} a static instance
     */
    public static TemplateManager getInstance() {
        if (instance == null) {
            instance = new TemplateManager();
        }
        return instance;
    }

    /**
     * Method looks for directories in the "template" folder, adds them to a list
     * if the directory contains a "template.json" file
     * @return {@link TemplateManager} instance of itself
     */
    public TemplateManager find() {
        this.templateDirs.clear();

        File dir = new File("template");
        if (!dir.exists()) {
            dir.mkdir();
        }

        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                List<String> contents = new ArrayList<String>(Arrays.asList(f.list()));
                if (contents.contains("template.json")) {
                    this.templateDirs.add(f);
                }
            }
        }

        System.out.println("Found " + this.templateDirs.size() + " Template(s)");
        return instance;
    }


    /**
     * Checks every found directory for a description file, if the file is
     * valid it detects the directory as a template
     */
    public void init() {
        this.templates.clear();
        this.templateDirs.forEach((File file) -> {
            List<File> files = new ArrayList<File>(Arrays.asList(file.listFiles()));
            files.forEach(f -> {
                if (f.getName().equalsIgnoreCase("template.json")) {
                    try {
                        JSONObject object = (JSONObject) parser.parse(new FileReader(f));
                        templates.add(new Template(file, object));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    /**
     *
     * @return {@link ArrayList<Template>} a list of templates that have been found
     */
    public List<Template> getTemplates() {
        return templates;
    }

    /**
     * This method returns a {@link Template} instance just with its name
     * @param name A unique template name
     * @return {@link Template}
     */
    public Template getByName(String name) {
        for (Template t : this.templates) {
            if (t.getName().equalsIgnoreCase(name)) {
                return t;
            }
        }
        return null;
    }

}
