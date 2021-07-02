package me.zeroX150.atomic.helper.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Events {

    public static class Packets {
        static Map<PacketEvents, List<Handler>> HANDLERS = new HashMap<>();

        public static void registerEventHandler(PacketEvents event, Handler handler) {
            if (!HANDLERS.containsKey(event)) HANDLERS.put(event, new ArrayList<>());
            HANDLERS.get(event).add(handler);
        }

        public static boolean fireEvent(PacketEvents event, PacketEvent argument) {
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

    public static class Rendering {
        static Map<RenderingEvents, List<Handler>> HANDLERS = new HashMap<>();

        public static void registerEventHandler(RenderingEvents event, Handler handler) {
            if (!HANDLERS.containsKey(event)) HANDLERS.put(event, new ArrayList<>());
            HANDLERS.get(event).add(handler);
        }

        public static boolean fireEvent(RenderingEvents event, RenderingEvent argument) {
            if (HANDLERS.containsKey(event)) {
                for (Handler handler : HANDLERS.get(event)) {
                    handler.onFired(argument);
                }
            }
            return argument.isCancelled();
        }

        public interface Handler {
            void onFired(RenderingEvent event);
        }
    }
}
