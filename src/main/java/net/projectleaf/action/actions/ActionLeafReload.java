package net.projectleaf.action.actions;

import net.projectleaf.bootstrap.Constant;
import org.json.simple.JSONObject;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;
import net.projectleaf.template.TemplateManager;

public class ActionLeafReload extends Action {

    public ActionLeafReload() {
        super("reload");
    }

    @Override
    public String run(JSONObject input) {
        System.out.println("RELOAD INSTANCIATED");
        TemplateManager.getInstance().find().init();
        var.checkLicence();
        System.out.println("RELOAD FINISHED");
        if (Constant.IS_FREE) {
            if (var.servers.size() > Constant.FREE_INSTANCES) {
                for (String s : var.servers.keySet()) {
                    if (var.servers.size() > Constant.FREE_INSTANCES) {
                        var.servers.get(s).stopServer();
                    }
                }
            }
        }
        return var.simpleJson(true, "reloaded.");
    }

}
