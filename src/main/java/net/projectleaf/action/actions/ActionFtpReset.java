package net.projectleaf.action.actions;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;
import org.json.simple.JSONObject;

public class ActionFtpReset extends Action {

    public ActionFtpReset() {
        super("ftp-reset");
    }

    /**
     *
     * @param input {@link JSONObject} Input provided with a Request
     * @return
     */
    @Override
    public String run(JSONObject input) {
        String username = input.get("username").toString();
        String password = input.get("password").toString();
        String home = input.get("homepath").toString();

        var.files.delUser(username);
        var.files.addUser(username, password, home);

        return var.simpleJson(true, "reset.");
    }
}
