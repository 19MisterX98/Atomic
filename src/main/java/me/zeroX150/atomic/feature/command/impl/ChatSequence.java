package me.zeroX150.atomic.feature.command.impl;

import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.helper.Client;

import java.util.Arrays;

public class ChatSequence extends Command {
    public ChatSequence() {
        super("ChatSequence","Configuration for the ChatSequence module","chatsequence","csequence","cseq");
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 0) {
            Client.notifyUser("you gotta give me a command");
            return;
        }
        switch(args[0].toLowerCase()) {
            case "messages" -> {
                if (args.length == 1) {
                    if (me.zeroX150.atomic.feature.module.impl.misc.ChatSequence.messages.isEmpty()) Client.notifyUser("No messages saved rn");
                    else {
                        Client.notifyUser("Messages:");
                        for (int i = 0; i < me.zeroX150.atomic.feature.module.impl.misc.ChatSequence.messages.size(); i++) {
                            Client.notifyUser("  #"+(i+1)+"  "+ me.zeroX150.atomic.feature.module.impl.misc.ChatSequence.messages.get(i));
                        }
                    }
                } else {
                    switch(args[1].toLowerCase()) {
                        case "add" -> {
                            if (args.length < 3) {
                                Client.notifyUser("You have to provide a message to add");
                                return;
                            }
                            if (ModuleRegistry.getByClass(me.zeroX150.atomic.feature.module.impl.misc.ChatSequence.class).isEnabled()) {
                                Client.notifyUser("You cant modify the config while the module is running.");
                                return;
                            }
                            String msg = String.join(" ",Arrays.copyOfRange(args,2,args.length));
                            Client.notifyUser("Added message \""+msg+"\"!");
                            me.zeroX150.atomic.feature.module.impl.misc.ChatSequence.messages.add(msg);
                        }
                        case "remove" -> {
                            if (args.length < 3) {
                                Client.notifyUser("You have to provide a message index to remove! Use .chatSequence messages to list them");
                                return;
                            }
                            if (ModuleRegistry.getByClass(me.zeroX150.atomic.feature.module.impl.misc.ChatSequence.class).isEnabled()) {
                                Client.notifyUser("You cant modify the config while the module is running.");
                                return;
                            }
                            String i = args[2];
                            int v = Client.tryParseInt(i,0)-1;
                            if (v<0||v> me.zeroX150.atomic.feature.module.impl.misc.ChatSequence.messages.size()-1) {
                                Client.notifyUser("Not sure if that message exists");
                                return;
                            }
                            String m = me.zeroX150.atomic.feature.module.impl.misc.ChatSequence.messages.get(v);
                            Client.notifyUser("Removed message \""+m+"\"");
                            me.zeroX150.atomic.feature.module.impl.misc.ChatSequence.messages.remove(v);
                        }
                        case "list" -> onExecute(new String[]{"messages"});
                        default -> Client.notifyUser("chatSequence:messages subcommands: add, remove, list");
                    }
                }
            }
            case "delay" -> {
                if (args.length == 1) {
                    Client.notifyUser("The current delay is "+ me.zeroX150.atomic.feature.module.impl.misc.ChatSequence.delay+" ms");
                } else {
                    if (ModuleRegistry.getByClass(me.zeroX150.atomic.feature.module.impl.misc.ChatSequence.class).isEnabled()) {
                        Client.notifyUser("You cant modify the config while the module is running.");
                        return;
                    }
                    String nd = args[1];
                    int v = Client.tryParseInt(nd,0);
                    if (v < 1 || v > 6000) {
                        Client.notifyUser("You have to specify a valid number above 0 and under 6000");
                        return;
                    }
                    Client.notifyUser("Set the delay to "+v+" ms");
                    me.zeroX150.atomic.feature.module.impl.misc.ChatSequence.delay = v;
                }
            }
            case "start" -> {
                Client.notifyUser("Starting ChatSequence. Disable the module to stop it, or wait until it runs out of messages.");
                me.zeroX150.atomic.feature.module.impl.misc.ChatSequence.shouldRun = true;
                ModuleRegistry.getByClass(me.zeroX150.atomic.feature.module.impl.misc.ChatSequence.class).setEnabled(true);
            }
            default -> Client.notifyUser("chatSequence subcommands: messages, delay, start");
        }
    }
}
