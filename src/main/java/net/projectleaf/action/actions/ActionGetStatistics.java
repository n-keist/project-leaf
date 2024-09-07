package net.projectleaf.action.actions;

import org.json.simple.JSONObject;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;
import net.projectleaf.io.MinecraftServer;

public class ActionGetStatistics extends Action {

    public ActionGetStatistics() {
        super("get-statistics");
    }

    @SuppressWarnings("unchecked")
    @Override
    public String run(JSONObject input) {
        JSONObject object = new JSONObject();

        object.put("instances-active", var.servers.size());
        double total = 0;
        for(MinecraftServer server : var.servers.values()) {
            total += server.getUsage()[1];
        }
        object.put("total-memory-used", total);

        double cpuavg = 0;

        for(MinecraftServer server : var.servers.values()) {
            cpuavg += server.getUsage()[0];
        }
        cpuavg /= var.servers.size();
        object.put("avg-cpu-used", cpuavg);

        return object.toJSONString();
    }
    //testing webooks. ageeen
}
