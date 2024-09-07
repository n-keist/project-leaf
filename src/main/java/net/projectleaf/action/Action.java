package net.projectleaf.action;

import org.json.simple.JSONObject;

public abstract class Action {

    private String name;

    public Action(String name) {
        this.name = name;
    }

    /**
     *
     * @return Action Name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param input {@link JSONObject} Input provided with a Request
     * @return {@link String} has to be valid JSON
     */
    public abstract String run(JSONObject input);

}
