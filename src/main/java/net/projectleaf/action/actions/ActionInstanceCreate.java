package net.projectleaf.action.actions;

import java.io.File;

import org.json.simple.JSONObject;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;
import net.projectleaf.io.Address;
import net.projectleaf.template.Template;
import net.projectleaf.template.TemplateManager;

public class ActionInstanceCreate extends Action {

    public ActionInstanceCreate() {
        super("instance-create");
    }

    @Override
    public String run(JSONObject input) {
        Template template = TemplateManager.getInstance().getByName(input.get("template").toString());
        String hostname = input.get("hostname").toString();
        long port = Long.parseLong(input.get("port").toString());
        Address address = new Address((int) port, hostname);
        if(!new File("instances/" + address.toString() + "/").exists()) {
            template.copy(new File("instances/" + address.toString() + "/"));
            return var.simpleJson(true, "created.");
        } else {
            return var.simpleJson(false, "exists.");
        }
    }

}
