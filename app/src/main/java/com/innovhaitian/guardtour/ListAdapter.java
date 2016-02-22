package com.innovhaitian.guardtour;

/**
 * Created by hollyn on 1/14/16.
 */

public class ListAdapter {

    String name = null;
    boolean selected = false;

    public ListAdapter(String name, boolean selected) {
        super();
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}