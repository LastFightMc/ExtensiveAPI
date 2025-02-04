/*  AsyncOnlyAspect
 * By: jimmy "vSKAH" <vskahhh@gmail.com>
 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 * 01/02/2025
 */

package fr.skoupi.extensiveapi.minecraft.annotations.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.bukkit.Bukkit;

@Aspect
public class AsyncOnlyAspect {

    @Before("@annotation(fr.skoupi.extensiveapi.minecraft.annotations.AsyncOnly)")
    public void checkIfAsync() {
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("This method can't be called on the main thread");
        }
    }
}
