package ru.terra.universal.shared.packet.worldserver;

import io.netty.buffer.ByteBuf;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.entity.AbstractEntity;
import ru.terra.universal.shared.packet.AbstractPacket;

/**
 * Created by Vadim_Korostelev on 11/15/2016.
 */
@Packet(opCode = OpCodes.WorldServer.ENTITY_ADD)
public class EntityAddPacket extends AbstractPacket {
    private AbstractEntity entity;

    public EntityAddPacket() {
        super();
    }

    public EntityAddPacket(Long to, AbstractEntity entity) {
        super();
        this.sender = to;
        this.entity = entity;
    }

    @Override
    public void get(ByteBuf buffer) {
        entity.readEntityInfo(buffer);
    }

    @Override
    public void send(ByteBuf buffer) {
        entity.writeEntityInfo(buffer);
    }

    public AbstractEntity getEntity() {
        return entity;
    }

    public void setEntity(AbstractEntity entity) {
        this.entity = entity;
    }
}
