package cn.wang.findview.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author WANG
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface FindView {

    int value();
}
