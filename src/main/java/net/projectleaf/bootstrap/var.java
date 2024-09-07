package net.projectleaf.bootstrap;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.json.simple.JSONObject;

import net.projectleaf.config.FileConfiguration;
import net.projectleaf.io.Files;
import net.projectleaf.io.MinecraftServer;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class var {

    public static FileConfiguration config;
    public static Files files;
    public static Map<String, MinecraftServer> servers;

    /**
     *
     * @param arg0 success value
     * @param arg1 additional message
     * @return {@link String} out of {@link JSONObject}
     */
    public static String simpleJson(boolean arg0, String arg1) {
        JSONObject o = new JSONObject();
        o.put("success", arg0);
        o.put("body", arg1);
        return o.toJSONString();
    }

    /**
     * method parses the output of the "getUsage" @ MinecraftServer.class
     * @param n {@link String}
     * @return double[] with usage
     */
    public static double[] format(String n) {
        String[] x = n.split(" ");
        double[] f = new double[2];
        int c = 0;
        for (int i = 0; i < x.length; i++) {
            if (!x[i].equalsIgnoreCase("")) {
                f[c] = Double.valueOf(x[i]);
                c++;
            }
        }
        return f;
    }


    /**
     * creates a default template
     */
    public static void setupTemplates() {
        File root = new File("template/");
        if (!root.exists()) {
            if (root.mkdir()) {
                File sub = new File("template/minecraft");
                sub.mkdir();
                File desc = new File("template/minecraft/template.json");
                if(!desc.exists()) {
                    try {
                        desc.createNewFile();
                        FileWriter fw = new FileWriter(desc);
                        fw.write("{\n");
                        fw.write("\t\"name\": \"Minecraft\",\n");
                        fw.write("\t\"minecraft-version\": \"1.12.1\",\n");
                        fw.write("\t\"excluded-files\": [\"template.json\"]\n");
                        fw.write("}");
                        fw.flush();
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * sends a validation request to our licence service
     */
    public static void checkLicence() {
        if (Constant.LICENCE.equalsIgnoreCase("free")) {
            System.out.println("You are limited to - two parallel running instances -");
            Constant.IS_FREE = true;
        } else {
            System.out.println("Waiting for Licence verification...");
            JSONObject v = new JSONObject();
            v.put("licence", Constant.LICENCE);
            v.put("salt", Constant.TOKEN);
            v.put("stats", var.config.getBoolean("share-stats"));

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL("https://api.project-leaf.net/v1/validate").openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("User-Agent", "aClient/validate");
                PrintWriter writer = new PrintWriter(connection.getOutputStream());
                writer.write(v.toJSONString());
                writer.flush();
                writer.close();
                connection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String n = reader.readLine();
                reader.close();
                JSONObject response = (JSONObject) new JSONParser().parse(n);
                if (Boolean.valueOf(response.get("success").toString())) {
                    Constant.IS_FREE = false;
                    System.out.println("Licence verification: success");
                } else {
                    System.out.println("Invalid Licence, shutting down.. \nIf you do not own a premium licence enter 'free' as licence.");
                    System.exit(0);
                }
            } catch (IOException e) {
                System.out.println("An error occoured: \n" + e.getMessage() + "\n -> Shutting down..");
                System.exit(0);
            } catch (ParseException e) {
                System.out.println("An error occoured: \n" + e.getMessage() + "\n -> Shutting down..");
                System.exit(0);
            }

        }
    }

}
