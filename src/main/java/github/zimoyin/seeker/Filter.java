package github.zimoyin.seeker;

import github.zimoyin.seeker.reference.vs.interfaces.GeneralClass;

public interface Filter {
    public boolean test(GeneralClass cls);
}
