package net.projectleaf.action.actions;

import org.json.simple.JSONObject;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;

public class ActionPing extends Action {

    public ActionPing() {
        super("ping");
    }

    @Override
    public String run(JSONObject input) {
        return var.simpleJson(true, "Pong!");
    }

}
