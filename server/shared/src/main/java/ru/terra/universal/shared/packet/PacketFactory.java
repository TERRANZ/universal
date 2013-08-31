package ru.terra.universal.shared.packet;

import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.core.ClassSearcher;

import java.util.HashMap;

public class PacketFactory {
    private static PacketFactory instance = new PacketFactory();
    private HashMap<Integer, AbstractPacket> packets = new HashMap<>();

    private PacketFactory() {
        ClassSearcher<AbstractPacket> searcher = new ClassSearcher<>();
        for (AbstractPacket ap : searcher.load("ru.terra.universal.shared.packet", Packet.class))
            packets.put(ap.getClass().getAnnotation(Packet.class).opCode(), ap);
    }

    public static PacketFactory getInstance() {
        return instance;
    }

    public AbstractPacket getPacket(Integer opCode, long sender) {
        AbstractPacket packet = packets.get(opCode);
        if (packet != null) {
            try {
                packet = packet.getClass().newInstance();
                packet.setSender(sender);
                return packet;
            } catch (InstantiationException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        }
        return null;
    }
}
