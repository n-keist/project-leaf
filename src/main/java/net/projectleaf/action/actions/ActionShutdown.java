package net.projectleaf.action.actions;

import org.json.simple.JSONObject;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;

public class ActionShutdown extends Action {

    public ActionShutdown() {
        super("shutdown");
    }

    @Override
    public String run(JSONObject input) {

        var.servers.values().forEach(server -> {
            server.stopServer();
        });

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.exit(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();

        return var.simpleJson(true, "bye bye.");
    }

}
