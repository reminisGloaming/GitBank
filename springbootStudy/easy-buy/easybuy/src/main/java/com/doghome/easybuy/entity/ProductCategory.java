package com.doghome.easybuy.entity;


import java.util.List;

public class ProductCategory {

    private long id;
    private String name;

    private long type;
    private String iconClass;

    private long parentId;

    private String parentName;

    private long oldParentId;

    private String oldParentName;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }


    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }


    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public long getOldParentId() {
        return oldParentId;
    }

    public void setOldParentId(long oldParentId) {
        this.oldParentId = oldParentId;
    }

    public String getOldParentName() {
        return oldParentName;
    }

    public void setOldParentName(String oldParentName) {
        this.oldParentName = oldParentName;
    }
}
