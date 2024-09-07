package net.projectleaf.action.actions;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.projectleaf.action.Action;
import net.projectleaf.template.Template;
import net.projectleaf.template.TemplateManager;

public class ActionGetTemplates extends Action {

    public ActionGetTemplates() {
        super("get-templates");
    }

    @SuppressWarnings("unchecked")
    @Override
    public String run(JSONObject input) {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        for(Template t : TemplateManager.getInstance().getTemplates()) {
            JSONObject template = new JSONObject();
            template.put("name", t.getName());
            template.put("version", t.getVersion());
            array.add(template);
        }
        object.put("templates", array);
        return object.toJSONString();
    }

}
