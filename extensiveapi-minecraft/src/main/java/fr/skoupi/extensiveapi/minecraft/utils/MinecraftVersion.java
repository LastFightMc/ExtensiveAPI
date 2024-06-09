package fr.skoupi.extensiveapi.minecraft.utils;

/*  MinecraftVersion
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import fr.skoupi.extensiveapi.minecraft.ExtensiveCore;
import org.bukkit.Bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the current Minecraft version the plugin loaded on
 */
public final class MinecraftVersion {

    /**
     * The string representation of the version, for example V1_13
     */
    private static String serverVersion;

    /**
     * The wrapper representation of the version
     */
    private static V current;

    /**
     * The version wrapper
     */
    public enum V {
        v1_19(19, false),
        v1_18(18),
        v1_17(17),
        v1_16(16),
        v1_15(15),
        v1_14(14),
        v1_13(13),
        v1_12(12),
        v1_11(11),
        v1_10(10),
        v1_9(9),
        v1_8(8),
        v1_7(7),
        v1_6(6),
        v1_5(5),
        v1_4(4),
        v1_3_AND_BELOW(3);

        /**
         * The numeric version (the second part of the 1.x number)
         */
        private final int minorVersionNumber;

        /**
         * Is this library tested with this Minecraft version?
         */
        @lombok.Getter
        private final boolean tested;

        /**
         * Creates new enum for an MC version that is tested
         *
         * @param version an minecraft version
         */
        V(int version) {
            this(version, true);
        }

        /**
         * Creates new enum for an MC version
         *
         * @param version an minecraft version
         * @param tested is tested
         */
        V(int version, boolean tested) {
            this.minorVersionNumber = version;
            this.tested = tested;
        }


        /**
         * If the number is equal to the minor version number, return the version, otherwise throw an exception.
         *
         * @param number The version number to parse.
         * @return The enum value that matches the number passed in.
         */
        private static V parse(int number) {
            for (final V v : values())
                if (v.minorVersionNumber == number)
                    return v;

            throw new NumberFormatException("Invalid version number: " + number);
        }

        /**
         * @see Enum#toString()
         */
        @Override
        public String toString() {
            return "1." + this.minorVersionNumber;
        }


    }

    /**
     * Returns true if the version is equal to the version passed in.
     *
     * @param version The version to compare with.
     * @return The return type is boolean.
     */
    public static boolean equals(V version) {
        return compareWith(version) == 0;
    }


    /**
     * Returns true if the current version is older than the given version.
     *
     * @param version The version to compare with.
     * @return A boolean value.
     */
    public static boolean olderThan(V version) {
        return compareWith(version) < 0;
    }


    /**
     * Returns true if the current version is newer than the given version.
     *
     * @param version The version to compare with.
     * @return A boolean value.
     */
    public static boolean newerThan(V version) {
        return compareWith(version) > 0;
    }


    /**
     * Returns true if the current version is equal to or newer than the given version.
     *
     * @param version The version to compare to.
     * @return A boolean value.
     */
    public static boolean atLeast(V version) {
        return equals(version) || newerThan(version);
    }

    /**
     * If the current version is less than the version passed in, return a negative number, if the current version is
     * greater than the version passed in, return a positive number, if the current version is equal to the version passed
     * in, return 0.
     *
     * @param version The version of the API you want to check against.
     * @return The difference between the current minor version number and the version number passed in.
     */
    private static int compareWith(V version) {
        try {
            return getCurrent().minorVersionNumber - version.minorVersionNumber;

        } catch (final Throwable t) {
            t.printStackTrace();

            return 0;
        }
    }


    /**
     * If the server version is "craftbukkit", return an empty string, otherwise return the server version
     *
     * @return The server version.
     */
    public static String getServerVersion() {
        return serverVersion.equals("craftbukkit") ? "" : serverVersion;
    }

    // Initialize the version
    static {
        final Logger logger = ExtensiveCore.getInstance().getLogger();

        try {
            final String packageName = Bukkit.getServer().getClass().getPackage().getName();
            final String curr = packageName.substring(packageName.lastIndexOf('.') + 1);
            final boolean hasGatekeeper = !"craftbukkit".equals(curr) && !"".equals(packageName);

            serverVersion = curr;

            if (hasGatekeeper) {
                int pos = 0;

                for (final char ch : curr.toCharArray()) {
                    pos++;

                    if (pos > 2 && ch == 'R')
                        break;
                }

                final String numericVersion = curr.substring(1, pos - 2).replace("_", ".");

                int found = 0;

                for (final char ch : numericVersion.toCharArray())
                    if (ch == '.')
                        found++;

                if (found != 1) {
                    logger.log(Level.SEVERE, () -> "Minecraft Version checker malfunction. Plugin disabled. Could not detect your server version. Detected: " + numericVersion + " Current: " + curr);
                    ExtensiveCore.getInstance().getPluginLoader().disablePlugin(ExtensiveCore.getInstance());
                }

                current = V.parse(Integer.parseInt(numericVersion.split("\\.")[1]));

            } else
                current = V.v1_3_AND_BELOW;

        } catch (final Throwable t) {
            logger.log(Level.SEVERE, t, () -> "Error detecting your Minecraft version. Plugin disabled. Check your server compatibility.");
            ExtensiveCore.getInstance().getPluginLoader().disablePlugin(ExtensiveCore.getInstance());
        }
    }

    public static V getCurrent() {
        return current;
    }


}