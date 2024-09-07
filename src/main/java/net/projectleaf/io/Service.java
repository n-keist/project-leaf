package net.projectleaf.io;

import static spark.Spark.*;

import java.util.Set;

import net.projectleaf.plugin.EventManager;
import net.projectleaf.plugin.event.events.ActionPerformedEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.reflections.Reflections;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.Constant;
import net.projectleaf.bootstrap.var;

public class Service {

    public Service(int port) {
        port(port);
        Reflections reflections = new Reflections("net.projectleaf.action.actions");
        Set<Class<? extends Action>> actions = reflections.getSubTypesOf(Action.class);
        before("/*", (req, res) -> {
            res.header("Server", "Watermelon/" + Constant.REST_VERSION);
            res.header("X-WELCOME", "Tweet this @nikokeist #LeafCP");
        });

        get("/", (req, res) -> {
            return "Holá";
        });

        path("/action", () -> {
            before("/*", (req_, res_) -> {
                if (!req_.headers().contains("Authentication")) {
                    halt(401, "No Authentication Header found");
                } else if (!req_.headers("Authentication").equalsIgnoreCase(Constant.TOKEN)) {
                    halt(401, "Incorrect Authentication Header, check documentation");
                }
                if (!req_.headers("Content-Type").equalsIgnoreCase("application/json")) {
                    halt(400, "We only accept and serve JSON.");
                }
                res_.header("Content-Type", "application/json");
            });
            post("/:action", (req, res) -> {
                String action = req.params(":action");
                System.out.println(req.ip() + " -> " + req.pathInfo());
                if (req.body() != null && !req.body().equalsIgnoreCase("")) {
                    JSONObject body = (JSONObject) new JSONParser().parse(req.body());
                    if (action.equalsIgnoreCase("get-log")) {
                        res.type("text/plain");
                    }

                    for (Class<? extends Action> a : actions) {
                        Action act = (Action) a.newInstance();
                        if (act.getName().equalsIgnoreCase(action)) {
                            String response = act.run(body);
                            EventManager.getInstance().callEvent(new ActionPerformedEvent(act, body, response, req.ip(), req.headers("Authentication"), req.requestMethod(), req.pathInfo()));
                            return response;
                        }
                    }
                } else {
                    halt(500, "Missing body.");
                }
                return "";
            });
        });
    }
}
