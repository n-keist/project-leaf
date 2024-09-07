package net.projectleaf.bootstrap;

import net.projectleaf.config.FileConfiguration;
import net.projectleaf.io.Files;
import net.projectleaf.io.Service;
import net.projectleaf.plugin.PluginManager;
import net.projectleaf.template.TemplateManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Bootstrap {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        if (!(System.getProperty("os.name").toLowerCase().contains("nux"))) {
            System.out.println("Currently only linux is supported.");
            System.exit(0);
        }

        var.servers = new HashMap<>();

        var.setupTemplates();

        try {
            FileUtils.forceMkdir(new File("instances/"));
        } catch (IOException e) {
        }

        try {
            FileUtils.forceMkdir(new File("plugins/"));
        } catch (IOException e) {
        }

        try {
            FileUtils.forceMkdir(new File("backups/"));
        } catch (IOException e) {
        }

        var.config = new FileConfiguration("config.cfg");
        var.config.copyDefaults(
                new String[] { "#Project Leaf Configuration", "#Version 0.1", "rest-port=4484", "ftp-port=4485",
                        "token=" + UUID.randomUUID().toString().replace("-", ""), "share-stats=true", "licence=free" });

        Constant.TOKEN = var.config.getString("token");

        new Service(var.config.getInt("rest-port"));
        System.out.println("Service listening on :" + var.config.getInt("rest-port"));
        var.files = Files.getInstance();

        System.out.println("Using token: " + Constant.TOKEN);

        TemplateManager.getInstance().find().init();

        Constant.LICENCE = var.config.getString("licence");
        PluginManager.getInstance().findPlugins().initPlugins();
        var.checkLicence();
    }

}
