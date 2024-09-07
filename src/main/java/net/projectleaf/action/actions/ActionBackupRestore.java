package net.projectleaf.action.actions;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.UnzipParameters;
import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;
import net.projectleaf.io.Address;

public class ActionBackupRestore extends Action {

    public ActionBackupRestore() {
        super("backup-restore");
    }

    @Override
    public String run(JSONObject input) {
        Address address = new Address((int) Long.parseLong(input.get("port").toString()), input.get("hostname").toString());
        String error = "";
        try {
            ZipFile zipFile = new ZipFile("backups/" + input.get("filename"));
            if(zipFile != null) {
                FileUtils.deleteDirectory(new File("instances/" + address.toString() + "/"));
                zipFile.extractAll("instances/", new UnzipParameters());
                return var.simpleJson(true, "restored");
            }
        } catch (ZipException e) {
            e.printStackTrace();
            error = e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            error = e.getMessage();
        }
        return var.simpleJson(false, error);
    }

}
