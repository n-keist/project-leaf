package net.projectleaf.plugin.event.events;

import net.projectleaf.action.Action;
import net.projectleaf.plugin.event.Cancellable;
import net.projectleaf.plugin.event.Event;
import org.json.simple.JSONObject;

public class ActionPerformedEvent extends Event implements Cancellable {

    private Action action;
    private JSONObject input;
    private String response;
    private String ip_address;
    private String token;
    private String method;
    private String path;
    private boolean cancelled;

    public ActionPerformedEvent(Action action, JSONObject input, String response, String ip_address, String token, String method, String path) {
        this.action = action;
        this.input = input;
        this.response = response;
        this.ip_address = ip_address;
        this.token = token;
        this.method = method;
        this.path = path;
    }

    public Action getAction() {
        return action;
    }

    public JSONObject getInput() {
        return input;
    }

    public void setInput(JSONObject input) {
        this.input = input;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getIp_address() {
        return ip_address;
    }

    public String getToken() {
        return token;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
