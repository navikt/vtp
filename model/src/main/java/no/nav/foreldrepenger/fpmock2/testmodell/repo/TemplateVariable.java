package no.nav.foreldrepenger.fpmock2.testmodell.repo;

import java.util.Objects;

/** Beskriver en template variabel.*/
public class TemplateVariable {

    private final Class<?> targetClass;
    private final String name;
    private final String path;
    private String defaultVerdi;

    public TemplateVariable(Class<?> targetClass, String name, String path, String defaultVerdi) {
        this.targetClass = targetClass;
        this.name = name;
        this.path = path;
        this.defaultVerdi = defaultVerdi;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, targetClass);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        TemplateVariable other = (TemplateVariable) obj;
        return Objects.equals(targetClass, other.targetClass)
            && Objects.equals(name, other.name)
            && Objects.equals(path, other.path);
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
    
    public String getDefaultVerdi() {
        return defaultVerdi;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName() + "<");
        if (name != null) {
            builder.append("name=").append(name).append(", ");
        }
        if (path != null) {
            builder.append("path=").append(path).append(", ");
        }
        if (targetClass != null) {
            builder.append("targetClass=").append(targetClass);
        }
        builder.append(">");
        return builder.toString();
    }
    
    
}
