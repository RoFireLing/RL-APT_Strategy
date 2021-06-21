package Strategy;

import java.util.Random;

/**
 * @author RoFire
 * @date 2021/6/15
 **/
public class RAPT {

    // 70% * the number of test cases, in each partition
    private static final int[] pun4program = {};
    private double[] RAPT;
    /**
     * parameters
     */
    private double RAPT_epsilon = 0.05;
    private double RAPT_delta;
    /**
     * the factor of reward and punishment
     */
    private int[] rew;
    private int[] pun;
    private int[] bou;

    /**
     * initialize the test profile of RAPT
     *
     * @param numberOfPartitions the number of partitions
     */
    public void initializeRAPT(int numberOfPartitions) {
        RAPT = new double[numberOfPartitions];
        for (int i = 0; i < RAPT.length; i++) {
            RAPT[i] = 1.0 / numberOfPartitions;
            pun[i] = 0;
        }
    }

    /**
     * get a index of partition
     * Note that the first number of partitions is 0
     *
     * @return the index
     */
    public int nextPartition4RAPT() {
        boolean flag = false;
        int partitionindex = -1;
        for (int i = 0; i < rew.length; i++) {
            if (rew[i] > 0) {
                flag = true;
                partitionindex = i;
                break;
            }
        }
        if (flag) {
            return partitionindex;
        } else {
            int index = -1;
            double randomNumber = new Random().nextDouble();
            double sum = 0;
            do {
                index++;
                sum += RAPT[index];
            } while (randomNumber >= sum && index < RAPT.length - 1);
            return index;
        }
    }

    /**
     * adjust the test profile for RAPT testing
     *
     * @param formerPartitionIndex
     * @param isKilledMutant
     */
    public void adjustRAPT(int formerPartitionIndex, boolean isKilledMutant) {

        double p_i = RAPT[formerPartitionIndex];

        if (isKilledMutant) {// the test case killed a mutant
            pun[formerPartitionIndex] = 0;
            rew[formerPartitionIndex]++;
        } else {// did not kill a mutant
            pun[formerPartitionIndex]++;
            if (rew[formerPartitionIndex] != 0) {
                double sum = 0;
                for (int i = 0; i < RAPT.length; i++) {
                    if (i != formerPartitionIndex) {
                        RAPT[i] -= (1 + Math.log(rew[formerPartitionIndex]))
                                * RAPT_epsilon / (RAPT.length - 1);
                        if (RAPT[i] < 0) {
                            RAPT[i] = 0;
                        }
                    }
                    sum += RAPT[i];
                }
                RAPT[formerPartitionIndex] = 1 - sum;
                rew[formerPartitionIndex] = 0;
            } else {
                for (int i = 0; i < RAPT.length; i++) {
                    if (i == formerPartitionIndex) {
                        if (p_i >= RAPT_delta) {
                            RAPT[i] -= RAPT_delta;
                        }
                        if (p_i < RAPT_delta || bou[i] == pun[i]) {
                            RAPT[i] = 0;
                        }
                    } else {
                        if (p_i >= RAPT_delta) {
                            RAPT[i] += RAPT_delta / (RAPT.length - 1);
                        }
                        if (p_i < RAPT_delta || bou[i] == pun[i]) {
                            RAPT[i] += p_i / (RAPT.length - 1);
                        }
                    }
                }
            }
        }
    }

    private void setRAPT_delta(double RAPT_delta) {
        this.RAPT_delta = RAPT_delta;
    }

    private void setRAPT_bou(String program_name, int[] punArray) {
        if (program_name.equals("name")) {
            int num_of_partitions = 0;
            rew = new int[num_of_partitions];
            pun = new int[num_of_partitions];
            bou = new int[num_of_partitions];
            for (int i = 0; i < punArray.length; i++) {
                bou[i] = punArray[i];
            }
        }
    }

    public void setParameters4RAPT(String program_name) {
        if (program_name.equals("Grep")) {
            setRAPT_delta(0);
            setRAPT_bou(program_name, pun4program);
        }
    }
}
