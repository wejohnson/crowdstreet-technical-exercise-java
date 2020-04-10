/******************************************************************************************************************/
/* This isn't a great solution and I would love to take more time developing a better one. It randomly selects    */
/* numbers until you get down to your last two. Those should have an equal amount left in the distribution.       */
/* It then takes alternates between those 2 so you don't have repeats.  There were times during testing where it  */
/* hang and not complete because the distribution count wasn't changing.                                          */
/* There are some efficiencies to be made and duplicate code to clean up.                                         */
/* Also, the count variable is acurate but is usually 1 less than the # of lines in the output file.              */
/******************************************************************************************************************/
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TechnicalExercise {

    // Distribution count 
    static final Map<Integer, Integer> distribution;

    static {
        distribution = new HashMap<>();
        distribution.put(1, 83000);
        distribution.put(2, 83000);
        distribution.put(3, 83000);
        distribution.put(4, 83000);
        distribution.put(5, 83000);
        distribution.put(6, 83000);
        distribution.put(7, 83000);
        distribution.put(8, 83000);
        distribution.put(9, 83000);
        distribution.put(10, 83000);
        distribution.put(11, 83000);
        distribution.put(12, 83000);
        distribution.put(13, 1000);
        distribution.put(14, 500);
        distribution.put(15, 250);
        distribution.put(16, 100);
        distribution.put(17, 50);
        distribution.put(18, 25);
        distribution.put(19, 10);
        distribution.put(20, 5);
    }

    public static void main(String args[]){
        ArrayList<Long> twenties = new ArrayList<Long>();
        ArrayList<Integer> excludes = new ArrayList<Integer>();
        int nextNumber = -1;
        int lastNumber = 0;
        int firstHighest = 83000;
        int secondHighest = 83000;
        long count = 1;
        FileWriter fileWriter;

        try {
            fileWriter = new FileWriter("test.output");

            try {
                while (excludes.size() < 20) { // end when all the numbers have used up their allocation
                    nextNumber = getRandomNumber(1, 20, excludes);

                    // The first part of algorithm completes until the only two numbers have instances left
                    if (excludes.size() <= 17 && Math.abs((firstHighest - secondHighest)) <= 1) {
                        if (secondHighest != distribution.get(nextNumber) || firstHighest == secondHighest) {
                            if (distribution.get(nextNumber) == 0 && !excludes.contains(nextNumber)) {
                                excludes.add(nextNumber);
                            }

                            if ((nextNumber != lastNumber) && !excludes.contains(nextNumber)  ) {
                                distribution.put(nextNumber, distribution.get(nextNumber) - 1);

                                fileWriter.write(Integer.toString(nextNumber) + "\n");
                                
                                if (nextNumber == 20) {
                                    twenties.add(count);
                                }
                
                                count++;
                                lastNumber = nextNumber;
                            }
                        }
                    }
                    // the 2nd part takes turns picking from the 2 that are left.
                    else if (excludes.size() >= 18 && (lastNumber != nextNumber)) {

                        int numToUse;

                        while (distribution.get(lastNumber) > 0 && distribution.get(nextNumber) > 0) {
                            if (distribution.get(lastNumber) > distribution.get(nextNumber)) {
                                numToUse = lastNumber;
                            }
                            else {
                                numToUse = nextNumber;
                            }
    
                            if ((nextNumber != lastNumber) && !excludes.contains(numToUse)) {
                                distribution.put(numToUse, distribution.get(numToUse) - 1);
                                fileWriter.write(Integer.toString(numToUse) + "\n");

                                // This probably isn't necessary at this point
                                if (numToUse == 20) {
                                    twenties.add(count);
                                }

                                count++;
                            }
                        }

                        excludes.add(lastNumber);
                        excludes.add(nextNumber);
                    }

                    firstHighest = findFirstHighest();
                    secondHighest = findSecondHighest();
            
                }
             } finally {
                fileWriter.close();
             }            
        } catch (IOException e) {
            System.out.println("en error has occurred creating the file");
            e.printStackTrace();
        } finally {
            System.out.println("Rows with 20 " + twenties);
        }
    }

    // Return a random number, excluding the ones that have already used up their number of instances
    private static int getRandomNumber(int start, int end, ArrayList<Integer> excludes) {
        int rnd;

        do {
            Random random = new Random();
            rnd = start + random.nextInt((end - start) + 1);
        } while (excludes.contains(rnd));

        return rnd;
    }

    // Find the number with the highest number of instances remaining
    private static int findFirstHighest() {
        ArrayList<Integer> sortedDistValues = getSortedDistributionValues();
        return sortedDistValues.get(0);
    }

    // Find the humber with the highest number of instances remaining
    private static int findSecondHighest() {
        ArrayList<Integer> sortedDistValues = getSortedDistributionValues();
        return sortedDistValues.get(1);
    }

    // Sort the distribution
    private static ArrayList<Integer> getSortedDistributionValues() {
        ArrayList<Integer> distValues = new ArrayList<Integer>(distribution.values());
        Collections.sort(distValues, Collections.reverseOrder());
        return distValues;
    }
}