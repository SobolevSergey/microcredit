package ru.simplgroupp.contact.protocol.v2.response.unpacked;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by aro on 17.09.2014.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Dic {

    @XmlElement(name = "DATAPACKET")
    protected DataPacketUnp dataPacket;

    public DataPacketUnp getDataPacket() {
        return dataPacket;
    }

    public void setDataPacket(DataPacketUnp dataPacket) {
        this.dataPacket = dataPacket;
    }
}
