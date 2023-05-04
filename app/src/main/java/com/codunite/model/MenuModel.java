package com.codunite.model;

public class MenuModel {
    public String menuName;
    public boolean hasChildren, isGroup;
    public String[] strMenuChildItem;

    public MenuModel(String menuName, boolean isGroup, boolean hasChildren, String[] strMenuChildItem) {
        this.strMenuChildItem = new String[strMenuChildItem.length];
        this.strMenuChildItem = strMenuChildItem;
        this.menuName = menuName;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
    }
}
