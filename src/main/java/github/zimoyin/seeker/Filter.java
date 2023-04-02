package github.zimoyin.seeker;

import github.zimoyin.seeker.reference.ClassReferencePacket;

public interface Filter {
    public boolean test(ClassReferencePacket packet);
}
