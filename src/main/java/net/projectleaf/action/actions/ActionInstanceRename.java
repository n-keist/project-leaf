package net.projectleaf.action.actions;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;
import net.projectleaf.io.Address;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;

public class ActionInstanceRename extends Action {

    public ActionInstanceRename() {
        super("instance-rename");
    }

    @Override
    public String run(JSONObject input) {
        String hostname = input.get("hostname").toString();
        long port = Long.parseLong(input.get("port").toString());

        Address old = new Address((int) port, hostname);

        String newHostname = input.get("hostname_new").toString();
        long newPort = Long.parseLong(input.get("port_new").toString());

        Address new_ = new Address((int) newPort, newHostname);

        try {
            FileUtils.moveDirectory(new File("instances/" + old.toString()), new File("instances/" + new_.toString()));
            return var.simpleJson(true, "renamed.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return var.simpleJson(false, "failed.");
    }
}
