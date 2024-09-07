package net.projectleaf.action.actions;

import org.json.simple.JSONObject;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;
import net.projectleaf.io.Address;
import net.projectleaf.io.MinecraftServer;

public class ActionInstanceStatus extends Action {

    public ActionInstanceStatus() {
        super("instance-status");
    }

    @SuppressWarnings("unchecked")
    @Override
    public String run(JSONObject input) {
        Address address = new Address((int) Long.parseLong(input.get("port").toString()), input.get("hostname").toString());
        if(var.servers.containsKey(address.toString())) {
            MinecraftServer server = var.servers.get(address.toString());
            if(server.isAlive()) {
                JSONObject object = new JSONObject();
                object.put("memory", server.getUsage()[1]);

                double cpu = server.getUsage()[0];
                double fcpu = 0;
                if(cpu > 100 && cpu < 200) {
                    fcpu = cpu / 2;
                } else if (cpu < 100) {
                    fcpu = cpu;
                } else if (cpu > 200 && cpu < 300) {
                    fcpu = cpu / 3;
                } else if (cpu > 300 && cpu < 400) {
                    fcpu = cpu / 4;
                }

                object.put("cpu", fcpu);
                object.put("pid", server.getPid());
                return object.toJSONString();
            }
        }
        return var.simpleJson(false, "unreachable.");
    }

}
