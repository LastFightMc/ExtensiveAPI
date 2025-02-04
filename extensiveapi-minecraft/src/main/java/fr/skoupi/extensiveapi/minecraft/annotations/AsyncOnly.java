/*  AsyncOnly
 * By: jimmy "vSKAH" <vskahhh@gmail.com>
 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 * 01/02/2025
 */

package fr.skoupi.extensiveapi.minecraft.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AsyncOnly { }
