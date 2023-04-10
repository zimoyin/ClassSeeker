package github.zimoyin.seeker.vs.impl;

import github.zimoyin.seeker.vs.General;

public abstract class GeneralImpl implements General {

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
