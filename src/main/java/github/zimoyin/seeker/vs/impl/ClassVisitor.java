package github.zimoyin.seeker.vs.impl;

import github.zimoyin.seeker.vs.GeneralClass;
import github.zimoyin.seeker.vs.GeneralField;
import github.zimoyin.seeker.vs.GeneralMethod;

import java.lang.annotation.Annotation;

public final class ClassVisitor extends GeneralImpl implements GeneralClass {
    @Override
    public String getName() {
        //TODO
        return null;
    }

    @Override
    public String getModifiers() {
        //TODO
        return null;
    }

    @Override
    public boolean isStatic() {
        //TODO
        return false;
    }

    @Override
    public boolean isFinal() {
        //TODO
        return false;
    }

    @Override
    public Annotation[] getAnnotations() {
        //TODO
        return new Annotation[0];
    }

    @Override
    public boolean isAnnotations(String annotation) {
        //TODO
        return false;
    }

    @Override
    public String getSimpleName() {
        //TODO
        return null;
    }

    @Override
    public String getInsClass() {
        //TODO
        return null;
    }

    @Override
    public String getLoadPath() {
        //TODO
        return null;
    }

    @Override
    public String getPackage() {
        //TODO
        return null;
    }

    @Override
    public String getTypeName() {
        //TODO
        return null;
    }

    @Override
    public GeneralClass getSuperclass() {
        //TODO
        return null;
    }

    @Override
    public GeneralMethod[] getConstructors() {
        //TODO
        return new GeneralMethod[0];
    }

    @Override
    public GeneralMethod[] getMethods() {
        //TODO
        return new GeneralMethod[0];
    }

    @Override
    public GeneralField[] getFields() {
        //TODO
        return new GeneralField[0];
    }

    @Override
    public boolean isAbstract() {
        //TODO
        return false;
    }

    @Override
    public boolean isInterface() {
        //TODO
        return false;
    }

    @Override
    public boolean isAnnotation() {
        //TODO
        return false;
    }

    @Override
    public boolean isEnum() {
        //TODO
        return false;
    }
}
