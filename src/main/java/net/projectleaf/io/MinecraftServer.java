package net.projectleaf.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import net.projectleaf.bootstrap.var;
import net.projectleaf.template.Template;


public class MinecraftServer extends Thread {

    private Address address;

    private Process process;
    private String command;
    private Map<String, String> params;

    private PrintWriter writer;

    private int pid;

    public MinecraftServer(Address address, String command, Map<String, String> params) {
        this.address = address;
        this.command = command;
        this.params = params;
        setName(address.toString());
    }

    /**
     *
     * @return {@link Address} Address Object of this instance
     */
    public Address getAddress() {
        return address;
    }

    /**
     *
     * @param template {@link Template} creates an instance from a template
     */
    public void create(Template template) {
        template.copy(new File("instances/" + getAddress().toString()));
    }

    /**
     * creates a process of an instance
     */
    public void startServer() {
        try {
            prepareProperties(this.params);
            ProcessBuilder builder = new ProcessBuilder(this.command.split(" "));
            builder.directory(new File("instances/" + getAddress().toString()));
            if(this.process == null) {
                this.process = builder.start();
                this.pid = getPid();
                System.out.println("[" + getName() + "] thread started (#" + this.pid + ")");
                this.writer = new PrintWriter(this.process.getOutputStream(), true);
                this.process.waitFor();
            } else {
                this.process.destroyForcibly();
                this.process = null;
                startServer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param params {@link Map<String, String>} server properties to overwrite while booting
     */
    public void prepareProperties(Map<String, String> params) {
        File f = new File("instances/" + address.toString() + "/server.properties");
        if(!f.exists()) {
            try {
                f.createNewFile();
                FileWriter fw = new FileWriter(f);
                for(String s : params.keySet()) {
                    fw.write(s + "=" + params.get(s) + "\n");
                }
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String newContents = "";
            String n = "";
            while((n = reader.readLine()) != null) {
                if(!n.startsWith("#")) {
                    String key = n.split("=")[0];
                    if(params.containsKey(key)) {
                        newContents += key + "=" + params.get(key) + "\n";
                    } else {
                        if(key.equals("server-port")) {
                            newContents += key + "=" + address.getPort() + "\n";
                        } else if (key.equalsIgnoreCase("server-ip")) {
                            newContents += key + "=" + address.getHostname() + "\n";
                        } else {
                            newContents += n + "\n";
                        }
                    }
                } else {
                    newContents += n + "\n";
                }
            }
            reader.close();
            FileUtils.deleteQuietly(f);

            newContents += "#This file was generated by Project Leaf, it will contain mostly\n";
            newContents += "#properties set by the webinterface, those will be overwritten on every boot.";

            if(!f.exists()) {
                f.createNewFile();
                PrintWriter writer = new PrintWriter(new FileWriter(f));
                writer.print(newContents);
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return {@link Integer} the process Id of an instance
     */
    public int getPid() {
        try {
            Field f = process.getClass().getDeclaredField("pid");
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            this.pid = (int) f.get(this.process);
        } catch (Exception e) {
            this.pid = -1;
        }
        return pid;
    }

    /**
     * forces the process to destroy
     * "kills it"
     */
    public void stopServer() {
        if(this.process != null && this.process.isAlive()) {
            try {
                System.out.println("[" + getName() + "] thread destroyed (#" + getPid() + ")");
                this.process.destroyForcibly();
                var.servers.remove(this.address.toString());
                interrupt();
            } catch (Exception e) {
            }
        }
    }

    /**
     *
     * @param command {@link String} send a command to the process
     */
    public void sendCommand(String command) {
        this.writer.println(command);
    }

    /**
     * gets the process system usage via "ps -p"
     * @return {@link Double[]} 0 => CPU usage 1 => Memory usage (%)
     */
    public double[] getUsage() {
        double[] data = new double[2];
        ProcessBuilder builder = new ProcessBuilder("ps", "-p", "" + getPid(), "-o", "%cpu,%mem", "--no-headers");
        try {
            Process p = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String n = "";
            while ((n = reader.readLine()) != null) {
                data = var.format(n);
            }
        } catch (IOException e) {
            data[0] = 0.0;
            data[1] = 5;
        }

        return data;
    }


    @Override
    public void run() {
        startServer();
    }

}
