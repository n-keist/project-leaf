package net.projectleaf.action.actions;

import java.util.Date;

import org.json.simple.JSONObject;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;
import net.projectleaf.io.Address;

public class ActionBackupCreate extends Action {

    public ActionBackupCreate() {
        super("backup-create");
    }

    @Override
    public String run(JSONObject input) {
        Address address = new Address((int) Long.parseLong(input.get("port").toString()), input.get("hostname").toString());
        try {
            ZipFile zip = new ZipFile("backups/" + address.toString() + "-" + new Date().toString().replace(" ", "_") + ".zip");
            zip.setRunInThread(true);
            zip.createZipFileFromFolder("instances/" + address.toString() + "/", new ZipParameters(), false, 10);
            return var.simpleJson(true, zip.getFile().getName());
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return var.simpleJson(false, "failed.");
    }

}
