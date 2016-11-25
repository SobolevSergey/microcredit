package ru.simplgroupp.contact.protocol.v2.response.unpacked;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by aro on 17.09.2014.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DATAPACKET")
public class DataPacketUnp {

    @XmlElementWrapper(name = "ROWDATA")
    @XmlElement(name = "ROW")
    private List<RowUnp> rows;

    public List<RowUnp> getRows() {
        return rows;
    }

    public void setRows(List<RowUnp> rows) {
        this.rows = rows;
    }
}
