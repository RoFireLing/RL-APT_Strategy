package Strategy;

import java.util.Random;

/**
 * @author RoFire
 * @date 2021/6/15
 **/
public class RLAPT_Q {

    private double[][] RLAPT;

    /**
     * parameters
     */
    private double RLAPT_alpha;

    private double RLAPT_gamma = 0.5;

    /**
     * reward
     */
    private double RLAPT_r0 = 1;

    private double r1, r2, r3, r4;

    /**
     * initialize the Q-table of RL-APT
     * initialize immediate reward
     *
     * @param numberofPartitions
     */
    public void initializeRLAPT(int numberofPartitions) {
        RLAPT = new double[numberofPartitions][numberofPartitions];
        for (int i = 0; i < numberofPartitions; i++) {
            for (int j = 0; j < numberofPartitions; j++) {
                RLAPT[i][j] = 0;
            }
        }
        r1 = RLAPT_r0;
        r2 = -RLAPT_r0;
        r3 = -RLAPT_r0 / (RLAPT.length - 1);
        r4 = RLAPT_r0 / (RLAPT.length - 1);
    }

    /**
     * get a index of partition
     * Note that the first number of partitions is 0
     *
     * @param formerPartitionNumber
     * @param noTC                  the number of test cases executed
     * @return the index
     */
    public int nextPartition4RLAPT(int formerPartitionNumber, int noTC) {
        // epsilon-greedy strategy
        int index = -1;
        int num_of_testcases = 1;
        double randomNumber = new Random().nextDouble();
        double epsilon;
        epsilon = Math.max((1 - (double) noTC / num_of_testcases), 0.1);
        if (randomNumber <= epsilon) {
            index = new Random().nextInt(RLAPT.length);
        } else
            index = (int) getMax(RLAPT[formerPartitionNumber])[1];
        return index;
    }

    /**
     * adjust the Q-table for RLAPT testing based on Q-Learning
     *
     * @param noTC
     * @param nowPartitionIndex
     * @param nextPartitionIndex
     * @param isKilledMutants
     */
    public void adjustRLAPT_Q(int noTC, int nowPartitionIndex, int nextPartitionIndex, boolean isKilledMutants) {
        RLAPT_alpha = 1.0 / noTC;
        double r = 0;
        if (nowPartitionIndex == nextPartitionIndex) {
            if (isKilledMutants) {
                r = r1;
            } else
                r = r2;
        } else {
            if (isKilledMutants) {
                r = r3;
            } else
                r = r4;
        }
        RLAPT[nowPartitionIndex][nextPartitionIndex] += RLAPT_alpha * (r + RLAPT_gamma * getMax(RLAPT[nextPartitionIndex])[0] - RLAPT[nowPartitionIndex][nextPartitionIndex]);
    }

    /**
     * get MaxValue or MaxValueIndex
     *
     * @param array
     * @return 0_MaxValue, 1_MaxValueIndex
     */
    public double[] getMax(double[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        int maxIndex = 0;
        double[] max_result = new double[2];
        for (int i = 0; i < array.length - 1; i++) {
            if (array[maxIndex] < array[i + 1]) {
                maxIndex = i + 1;
            }
        }
        max_result[0] = array[maxIndex];
        max_result[1] = maxIndex;
        return max_result;
    }
}
