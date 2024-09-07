package net.projectleaf.action.actions;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;

public class ActionBackupDelete extends Action {

    public ActionBackupDelete() {
        super("backup-delete");
    }

    @Override
    public String run(JSONObject input) {
        if(new File("backups/" + input.get("filename").toString()).exists()) {
            FileUtils.deleteQuietly(new File("backups/" + input.get("filename").toString()));
            return var.simpleJson(true, "deleted");
        }
        return var.simpleJson(false, "doesnt exist");
    }

}
