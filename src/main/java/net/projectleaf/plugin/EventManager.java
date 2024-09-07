package net.projectleaf.plugin;

import net.projectleaf.plugin.event.Event;
import net.projectleaf.plugin.event.EventHandler;
import net.projectleaf.plugin.event.Listener;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class EventManager {

    private static EventManager instance;

    private Set<Class<? extends Event>> events;

    private EventManager() {
        Reflections reflections = new Reflections("net.projectleaf.plugin.event.events");
        events = reflections.getSubTypesOf(Event.class);
    }

    public static EventManager getInstance() {
        if(instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    public void callEvent(Event event) {
        PluginManager.getInstance().getListeners().values().forEach(lList -> {
            lList.forEach(listener -> {
                Class<? extends Listener> listenerClass = listener.getClass();
                for(int m = 0; m < listenerClass.getMethods().length; m++) {
                    Method method = listenerClass.getMethods()[m];
                    if(method.isAnnotationPresent(EventHandler.class)) {
                        if(event.getClass() == method.getParameters()[0].getType()) {
                            try {
                                if(!method.isAccessible()) {
                                    method.setAccessible(true);
                                }
                                method.invoke(instance, event);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        });
    }

}
