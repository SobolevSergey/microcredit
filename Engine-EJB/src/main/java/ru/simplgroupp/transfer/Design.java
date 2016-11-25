package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Design implements Serializable {
    private Integer id;
    private String type;
    private String value;
    private List<Design> children;
    private List<Attribute> attributes;

    public Design() {
    }

    public Design(String type) {
        this.type = type;
    }

    public Design(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public Design(String type, String value, Integer id) {
        this.type = type;
        this.value = value;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Design> getChildren() {
        return children;
    }

    public void setChildren(List<Design> children) {
        this.children = children;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer(this.type);
        if (this.value != null) {
            s.append(":\"" + this.value + "\"");
            if (this.attributes != null) {
                if (this.attributes.size() > 0) {
                    for (Attribute attibute : this.attributes) {
                        s.append(",");
                        s.append(attibute.getName());
                        s.append(":\"");
                        s.append(attibute.getValue());
                        s.append("\"  ");
                    }
                }
            }
        }  else {
            if (this.type.equalsIgnoreCase("body")) {
                s.append(":{ ");
                if (this.children != null) {
                    if (this.children.size() > 0) {
                        for (Design element : this.children) {
                            s.append( element.toString() );
                        }
                    }
                }
                s.append("} ");
            }
            if (this.type.equalsIgnoreCase("elements")) {
                s.append(":[ ");
                if (this.children != null) {
                    if (this.children.size() > 0) {
                        for (Design element : this.children) {
                            s.append( element.toString() );
                        }
                    }
                }
                s.append("] ");
            }

        }
        return s.toString();
    }
}