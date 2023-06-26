package github.zimoyin.seeker;

import github.zimoyin.seeker.reference.vs.interfaces.GeneralClass;

@FunctionalInterface
public interface Filter {
    public boolean test(GeneralClass cls);
}
