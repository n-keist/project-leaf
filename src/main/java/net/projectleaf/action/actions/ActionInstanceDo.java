package net.projectleaf.action.actions;

import org.json.simple.JSONObject;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;
import net.projectleaf.io.Address;
import net.projectleaf.io.MinecraftServer;

public class ActionInstanceDo extends Action {

    public ActionInstanceDo() {
        super("instance-do");
    }

    @Override
    public String run(JSONObject input) {
        Address address = new Address((int) Long.parseLong(input.get("port").toString()), input.get("hostname").toString());
        if(var.servers.containsKey(address.toString())) {
            String command = input.get("command").toString();
            MinecraftServer server = var.servers.get(address.toString());
            if(command.toLowerCase().startsWith("stop")) {
                server.stopServer();
            } else {
                server.sendCommand(command);
            }
            return var.simpleJson(true, "sent.");
        }
        return var.simpleJson(false, "failed.");
    }

}
