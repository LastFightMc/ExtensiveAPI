package fr.skoupi.extensiveapi.minecraft.progressbar;

/*  ProgressBarBuilder
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import org.bukkit.ChatColor;

public class ProgressBarBuilder {

    private ProgressBarOrientation progressBarOrientation;
    private ProgressBarType progressBarType;
    private int progressValue;
    private int progressMaxValue;

    private char patternColor;
    private char progressColor;

    /**
     * It sets the orientation of the progress bar.
     *
     * @param progressBarOrientation The orientation of the progress bar.
     * @return The ProgressBarBuilder object.
     */
    public ProgressBarBuilder setBarOrientation(ProgressBarOrientation progressBarOrientation) {
        this.progressBarOrientation = progressBarOrientation;
        return this;
    }

    /**
     * It sets the progress bar type.
     *
     * @param progressBarType The type of progress bar you want to use.
     * @return The ProgressBarBuilder object.
     */
    public ProgressBarBuilder setBarType(ProgressBarType progressBarType) {
        this.progressBarType = progressBarType;
        return this;
    }

    /**
     * Sets the maximum value of the progress bar.
     *
     * @param progressBarMaxValue The maximum value of the progress bar.
     * @return The ProgressBarBuilder object.
     */
    public ProgressBarBuilder setProgressBarMaxValue(int progressBarMaxValue) {
        this.progressMaxValue = progressBarMaxValue;
        return this;
    }

    /**
     * It sets the progress value of the progress bar.
     *
     * @param progressValue The value of the progress bar.
     * @return The ProgressBarBuilder object.
     */
    public ProgressBarBuilder setProgressBarValue(int progressValue) {
        this.progressValue = progressValue;
        return this;
    }

    /**
     * It sets the pattern color of the progress bar.
     *
     * @param character The character to use for the pattern.
     * @return The ProgressBarBuilder object.
     */
    public ProgressBarBuilder setPaternColor(char character) {
        this.patternColor = character;
        return this;
    }

    /**
     * `setProgressColor` sets the progress color
     *
     * @param character The character to use for the progress bar.
     * @return The ProgressBarBuilder object.
     */
    public ProgressBarBuilder setProgressColor(char character) {
        this.progressColor = character;
        return this;
    }


    /**
     * It builds the progress bar
     *
     * @param characterUsed The character used to build the progress bar.
     * @return A progress bar on String format
     */
    public String build(char characterUsed) {
        final StringBuilder stringBuilder = new StringBuilder();

        // It's the classic progress bar.
        if(progressBarType == ProgressBarType.CLASSIC) {
            final int coloredCharacter = (int) (progressMaxValue * (100 * progressValue) / 100F);
            // It checks if the progress bar orientation is left to right.
            if(progressBarOrientation == ProgressBarOrientation.LEFT_RIGHT) {
                // It's adding the progress color to the progress bar.
                for (int i = 0; i < coloredCharacter; i++) stringBuilder.append('&').append(progressColor).append(characterUsed);
                // It's adding the pattern color to the progress bar.
                for (int i = 0; i < (progressMaxValue - coloredCharacter); i++) stringBuilder.append('&').append(patternColor).append(characterUsed);
            }
            // It checks if the progress bar orientation is right to left.
            if(progressBarOrientation == ProgressBarOrientation.RIGHT_LEFT) {
                // It's adding the pattern color to the progress bar.
                for (int i = 0; i < (progressMaxValue - coloredCharacter); i++) stringBuilder.append('&').append(patternColor).append(characterUsed);
                // It's adding the progress color to the progress bar.
                for (int i = 0; i < coloredCharacter; i++) stringBuilder.append('&').append(progressColor).append(characterUsed);
            }
            return ChatColor.translateAlternateColorCodes('&', stringBuilder.toString());
        }


        // It's checking if the progress bar type is AKIRA.
        if(progressBarType == ProgressBarType.AKIRA) {
            // It checks if the progress bar orientation is right to left.
            if(progressBarOrientation == ProgressBarOrientation.RIGHT_LEFT) {
                for (int i = progressMaxValue; i > 0; i--) {
                    if (i == progressValue) stringBuilder.append('&').append(progressColor).append(characterUsed);
                    else stringBuilder.append('&').append(patternColor).append(characterUsed);
                }
            }
            // It checks if the progress bar orientation is left to right.
            if(progressBarOrientation == ProgressBarOrientation.LEFT_RIGHT) {
                for (int i = 0; i < progressMaxValue; i++) {
                    if (i == progressValue) stringBuilder.append('&').append(progressColor).append(characterUsed);
                    else stringBuilder.append('&').append(patternColor).append(characterUsed);
                }
            }
            return ChatColor.translateAlternateColorCodes('&', stringBuilder.toString());
        }

        return "error";
    }

}
