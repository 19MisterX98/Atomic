package me.zeroX150.atomic.helper.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventSystem {
    static Map<Event, List<Handler>> HANDLERS = new HashMap<>();

    public static void registerEventHandler(Event event, Handler handler) {
        if (!HANDLERS.containsKey(event)) HANDLERS.put(event, new ArrayList<>());
        HANDLERS.get(event).add(handler);
    }

    public static boolean fireEvent(Event event, PacketEvent argument) {
        if (HANDLERS.containsKey(event)) {
            for (Handler handler : HANDLERS.get(event)) {
                handler.onFired(argument);
            }
        }
        return argument.isCancelled();
    }

    public interface Handler {
        void onFired(PacketEvent event);
    }
}
