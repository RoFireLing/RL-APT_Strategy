package Strategy;

import java.util.Random;

/**
 * @author RoFire
 * @date 2021/6/15
 **/
public class DRT {

    private double[] DRT;

    /**
     * parameters
     */
    private double DRT_epsilon = 0.05;

    private double DRT_delta;

    /**
     * initialize the test profile of DRT
     *
     * @param numberOfPartitions the number of partitions
     */
    public void initializeDRT(int numberOfPartitions) {
        DRT = new double[numberOfPartitions];
        for (int i = 0; i < numberOfPartitions; i++) {
            DRT[i] = 1.0 / numberOfPartitions;
        }
    }

    /**
     * get a index of partition
     * Note that the first number of partitions is 0
     *
     * @return the index
     */
    public int nextPartition4DRT() {
        double[] tempArray = DRT;
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
     * adjust the test profile for DRT testing
     *
     * @param formerPartitionIndex
     * @param isKilledMutants
     */
    public void adjustDRT(int formerPartitionIndex, boolean isKilledMutants) {
        if (isKilledMutants) {// the test case killed a mutant
            double sum = 0;
            double threshold = DRT_epsilon / (DRT.length - 1);
            for (int i = 0; i < DRT.length; i++) {
                if (i != formerPartitionIndex) {
                    if (DRT[i] > threshold) {
                        DRT[i] -= threshold;
                    } else {
                        DRT[i] = 0;
                    }
                }
                sum += DRT[i];
            }
            DRT[formerPartitionIndex] = 1 - sum;
        } else {//did not kill a mutant
            double threshold;
            if (DRT[formerPartitionIndex] >= DRT_delta) {
                threshold = DRT_delta / (DRT.length - 1);
                DRT[formerPartitionIndex] -= DRT_delta;
            } else {
                threshold = DRT[formerPartitionIndex] / (DRT.length - 1);
                DRT[formerPartitionIndex] = 0;
            }
            for (int i = 0; i < DRT.length; i++) {
                if (i != formerPartitionIndex) {
                    DRT[i] += threshold;
                }
            }
        }
    }

    private void setDRT_delta(double DRT_delta) {
        this.DRT_delta = DRT_delta;
    }

    public void setParameters4DRT(String program_name) {
        if (program_name.equals("name")) {
            setDRT_delta(0);
        }
    }
}
