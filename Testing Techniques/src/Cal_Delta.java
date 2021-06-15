/**
 * @author RoFire
 * @date 2021/6/15
 **/
public class Cal_Delta {
    /**
     * calculate delta
     *
     * @param max  the largest failure rate
     * @param max2 the second largest failure rate
     * @return
     */
    public static double cal_delta(double max, double max2) {
        double epsilon = 0.05;
        double delta = ((max * 0.05 / (1 - max)) - (max2 * 0.05 / (1 - max2))) * 0.8 + (max2 * 0.05 / (1 - max2));
        return delta;
    }
}
