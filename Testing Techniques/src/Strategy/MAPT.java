package Strategy;

import java.util.Random;

/**
 * @author RoFire
 * @date 2021/6/15
 **/
public class MAPT {

    private double[][] MAPT;

    /**
     * parameters
     */
    private double MAPT_gamma = 0.1;

    private double MAPT_tau = 0.1;

    /**
     * initialize the test profile of MAPT
     *
     * @param numberOfPartitions the number of partitions
     */
    public void initializeMAPT(int numberOfPartitions) {
        MAPT = new double[numberOfPartitions][numberOfPartitions];
        for (int i = 0; i < numberOfPartitions; i++) {
            for (int j = 0; j < numberOfPartitions; j++) {
                MAPT[i][j] = 1.0 / numberOfPartitions;
            }
        }
    }

    /**
     * get a index of partition
     * Note that the first number of partitions is 0
     *
     * @param formerPartitionNumber
     * @return the index
     */
    public int nextPartition4MAPT(int formerPartitionNumber) {
        double[] tempArray = new double[MAPT.length];
        for (int i = 0; i < tempArray.length; i++) {
            tempArray[i] = MAPT[formerPartitionNumber][i];
        }
        int index = -1;
        double randomNumber = new Random().nextDouble();
        double sum = 0;
        do {
            index++;
            sum += tempArray[index];
        } while (randomNumber >= sum && index < tempArray.length - 1);
        return index;
    }

    /**
     * adjust the test profile for MAPT testing
     *
     * @param formerPartitionIndex
     * @param isKilledMutants
     */
    public void adjustMAPT(int formerPartitionIndex, boolean isKilledMutants) {

        double p_ii = MAPT[formerPartitionIndex][formerPartitionIndex];

        if (isKilledMutants) {// the test case killed a mutant
            double sum = 0;
            double threshold = MAPT_gamma * p_ii / (MAPT.length - 1);
            for (int i = 0; i < MAPT.length; i++) {
                if (i != formerPartitionIndex) {
                    if (MAPT[formerPartitionIndex][i] > threshold) {
                        MAPT[formerPartitionIndex][i] -= threshold;
                    }
                }
                sum += MAPT[formerPartitionIndex][i];
            }
            MAPT[formerPartitionIndex][formerPartitionIndex] = 1 - sum;
        } else {//did not kill a mutant
            double threshold = MAPT_tau * (1 - p_ii) / (MAPT.length - 1);
            for (int i = 0; i < MAPT.length; i++) {
                if (i != formerPartitionIndex) {
                    if (MAPT[formerPartitionIndex][formerPartitionIndex] > threshold) {
                        MAPT[formerPartitionIndex][i] += MAPT_tau * MAPT[formerPartitionIndex][i] / (MAPT.length - 1);
                    }
                } else {
                    if (MAPT[formerPartitionIndex][formerPartitionIndex] > threshold) {
                        MAPT[i][i] -= MAPT_tau * (1 - MAPT[i][i]) / (MAPT.length - 1);
                    }
                }
            }
        }
    }
}
