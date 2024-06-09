package fr.skoupi.extensiveapi.minecraft.utils;

/*  ItemStackSerializer
 * By: vSKAH <vskahhh@gmail.com>
 * Created with IntelliJ IDEA
 * For the project minecraft-modules
 */

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class BukkitBase64Serializer {

    public static String writeNow(Object... objects) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeInt(objects.length);

            for (Object object : objects)
                dataOutput.writeObject(object);

            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (Exception ignored) {
            return "";
        }
    }

    public static Object[] read(String source) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(source));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            Object[] items = new Object[dataInput.readInt()];

            for (int i = 0; i < items.length; i++)
                items[i] = dataInput.readObject();

            return items;
        } catch (Exception ignored) {
            return new Object[0];
        }
    }

}
