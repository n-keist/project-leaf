package net.projectleaf.action.actions;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.Constant;
import net.projectleaf.bootstrap.var;
import net.projectleaf.io.Address;
import net.projectleaf.io.MinecraftServer;

public class ActionInstanceStart extends Action {

    public ActionInstanceStart() {
        super("instance-start");
    }

    @Override
    public String run(JSONObject input) {
        if(Constant.IS_FREE && var.servers.size() == 2) {
            return var.simpleJson(false, "limited.");
        }
        long port = Long.parseLong(input.get("port").toString());
        String hostname = input.get("hostname").toString();

        Map<String, String> params = new HashMap<String, String>();
        JSONObject ps = (JSONObject) input.get("params");
        for(Object k : ps.keySet()) {
            String key = k.toString();
            String value = ps.get(k).toString();
            params.put(key, value);
        }

        Address address = new Address((int) port, hostname);
        if(!var.servers.containsKey(address.toString())) {
            MinecraftServer server = new MinecraftServer(address, input.get("command").toString(), params);
            server.start();
            var.servers.put(address.toString(), server);
            return var.simpleJson(true, "started.");
        } else {
            return var.simpleJson(false, "already running.");
        }
    }

}
