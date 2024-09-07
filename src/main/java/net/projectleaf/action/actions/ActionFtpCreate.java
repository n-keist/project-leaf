package net.projectleaf.action.actions;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;
import org.json.simple.JSONObject;

public class ActionFtpCreate extends Action {

    public ActionFtpCreate() {
        super("ftp-create");
    }

    @Override
    public String run(JSONObject input) {

        String username = input.get("username").toString();
        String password = input.get("password").toString();
        String homePath = input.get("homepath").toString();

        var.files.addUser(username, password, homePath);

        return var.simpleJson(true, "created.");
    }
}
