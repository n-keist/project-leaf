package net.projectleaf.action.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;

import net.projectleaf.action.Action;
import net.projectleaf.bootstrap.var;
import net.projectleaf.io.Address;

public class ActionGetLog extends Action {

    public ActionGetLog() {
        super("get-log");
    }

    @Override
    public String run(JSONObject input) {

        Address address = new Address((int) Long.parseLong(input.get("port").toString()), input.get("hostname").toString());

        if(new File("instances/" + address.toString() + "/logs/latest.log").exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(new File("instances/" + address.toString() + "/logs/latest.log")));
                String n = "";
                String f = "";
                while ((n = reader.readLine()) != null) {
                    f += n + "\n";
                }
                reader.close();
                return f;
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }

        return var.simpleJson(false, "failed. was the server started at least once?");
    }

}
