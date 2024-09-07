package net.projectleaf.action.actions;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;
import net.projectleaf.io.Address;

public class ActionInstanceDelete extends Action {

    public ActionInstanceDelete() {
        super("instance-delete");
    }

    @Override
    public String run(JSONObject input) {
        Address address = new Address((int) Long.parseLong(input.get("port").toString()), input.get("hostname").toString());
        if(!new File("instances/" + address.toString() + "/").exists()) {
            try {
                FileUtils.deleteDirectory(new File("instances/" + address.toString() + "/"));
                return var.simpleJson(true, "created.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return var.simpleJson(false, "exists.");
        }
    }

}
