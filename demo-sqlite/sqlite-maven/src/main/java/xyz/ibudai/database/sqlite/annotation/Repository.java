package xyz.ibudai.database.sqlite.annotation;

import xyz.ibudai.database.sqlite.enums.Database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Repository {

    Database value();
}
