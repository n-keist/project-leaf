package net.projectleaf.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.SaltedPasswordEncryptor;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

public class Files {

    private int port;

    private FtpServerFactory serverFactory;
    private FtpServer server;

    private static Files instance;

    private Files(int port) {
        this.port = port;

        serverFactory = new FtpServerFactory();
        server = serverFactory.createServer();

        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(getPort());

        serverFactory.addListener("default", listenerFactory.createListener());

        try {
            server.start();
            System.out.println("Ftp-Server listening on :" + getPort());
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }

    /**
     * deletes a User from the ftp program
     * @param username {@link String} the FTP Username
     */
    public void delUser(String username) {
        serverFactory.getFtplets().keySet().forEach(k -> {
            Ftplet ftp = serverFactory.getFtplets().get(k);
            if(k.equalsIgnoreCase(username)) {
                ftp.destroy();
            }
        });
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        File userFile = new File("ftpusers.properties");
        if (!userFile.exists()) {
            try {
                userFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userManagerFactory.setFile(new File("ftpusers.properties"));
        userManagerFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
        UserManager manager = userManagerFactory.createUserManager();

        try {
            manager.delete(username);
            serverFactory.setUserManager(manager);
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param username {@link String} the ftp username
     * @param password {@link String} the ftp password
     * @param home {@link String} the ftp root directory
     */
    public void addUser(String username, String password, String home) {
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();

        File userFile = new File("ftpusers.properties");
        if (!userFile.exists()) {
            try {
                userFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        userManagerFactory.setFile(new File("ftpusers.properties"));
        userManagerFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
        UserManager manager = userManagerFactory.createUserManager();
        BaseUser user = new BaseUser();
        user.setName(username);
        user.setPassword(password);
        user.setHomeDirectory(home);
        user.setAuthorities(new ArrayList<Authority>(Arrays.asList(new WritePermission())));
        try {
            manager.save(user);
            serverFactory.setUserManager(manager);
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @return {@link Integer} FTP Port
     */
    public int getPort() {
        return port;
    }

    /**
     *
     * @return {@link Files} static class instance
     */
    public static Files getInstance() {
        if (instance == null) {
            instance = new Files(4485);
        }
        return instance;
    }

}
