package com.github.zimoyin.seeker;

import com.github.zimoyin.seeker.reference.vs.interfaces.GeneralClass;

@FunctionalInterface
public interface Filter {
    public boolean test(GeneralClass cls);
}
