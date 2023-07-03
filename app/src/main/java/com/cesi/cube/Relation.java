package com.cesi.cube;

import androidx.annotation.Nullable;

public class Relation {
    public Relation(String name, String relation) {
        this.name = name;
        this.relation = relation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    String relation;
}
