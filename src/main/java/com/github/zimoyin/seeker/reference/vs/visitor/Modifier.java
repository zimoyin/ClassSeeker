package com.github.zimoyin.seeker.reference.vs.visitor;

import lombok.Getter;

@Getter
public enum Modifier {
    Public(0),
    Private(1),
    Protected(2),
    Default(3);

    private final int modifier;

    Modifier(int i) {
        this.modifier = i;
    }

    public String getModifierName() {
        return this.name();
    }
}
