package io.evlikat.limopdf.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * An annotation class indicating that method contains drawing rules, size calculation rules.
 */
@Target(ElementType.METHOD)
public @interface Rule {
}
