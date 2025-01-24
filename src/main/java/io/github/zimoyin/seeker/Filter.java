package io.github.zimoyin.seeker;

import io.github.zimoyin.seeker.reference.vs.interfaces.GeneralClass;

@FunctionalInterface
public interface Filter {
    public boolean test(GeneralClass cls);
}
