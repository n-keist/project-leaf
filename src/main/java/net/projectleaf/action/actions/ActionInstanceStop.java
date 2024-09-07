package net.projectleaf.action.actions;

import org.json.simple.JSONObject;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;
import net.projectleaf.io.Address;

public class ActionInstanceStop extends Action {

    public ActionInstanceStop() {
        super("instance-stop");
    }

    @Override
    public String run(JSONObject input) {
        Address address = new Address((int) Long.parseLong(input.get("port").toString()), input.get("hostname").toString());
        if(var.servers.containsKey(address.toString())) {
            var.servers.get(address.toString()).stopServer();
            return var.simpleJson(true, "stopped");
        } else {
            return var.simpleJson(false, "unreachable");
        }
    }

}
