package net.projectleaf.action.actions;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;
import org.json.simple.JSONObject;

public class ActionGetPlugins extends Action {

    public ActionGetPlugins() {
        super("get-plugins");
    }

    @Override
    public String run(JSONObject input) {
        return var.simpleJson(false, "not available right now.");
    }
}
