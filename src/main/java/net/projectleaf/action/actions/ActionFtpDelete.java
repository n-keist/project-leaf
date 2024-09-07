package net.projectleaf.action.actions;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;
import org.json.simple.JSONObject;

public class ActionFtpDelete extends Action {

    public ActionFtpDelete() {
        super("ftp-delete");
    }

    @Override
    public String run(JSONObject input) {
        String username = input.get("username").toString();
        var.files.delUser(username);
        return var.simpleJson(true, "deleted.");
    }
}
