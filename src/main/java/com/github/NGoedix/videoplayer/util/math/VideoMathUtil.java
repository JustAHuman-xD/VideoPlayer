package com.github.NGoedix.videoplayer.util.math;

public class VideoMathUtil {
    /**
     * Converts arguments into an ease-in value usable on animations.
     *
     * @param start The beginning of the result across time.
     * @param end The end of the result across time.
     * @param t Time from 0.0 to 1.0
     * @return The calculated result of ease-in interpolation between start and end at time t.
     */
    public static double easeIn(double start, double end, double t) {
        return start + (end - start) * t * t;
    }

    /**
     * Converts arguments into an ease-out value usable on animations.
     *
     * @param start The beginning of the result across time.
     * @param end The end of the result across time.
     * @param t Time from 0.0 to 1.0
     * @return The calculated result of ease-out interpolation between start and end at time t.
     */
    public static double easeOut(double start, double end, double t) {
        return start + (end - start) * (1 - Math.pow(1 - t, 2));
    }
}
