package xyz.ibudai.model;

public class DriverPackageEntity {

    private String name;

    private String path;

    private String className;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "DriverPackageEntity{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
