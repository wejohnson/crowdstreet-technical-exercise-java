
/******************************************************************************************************************/
/* This is a 2nd approach to the problem.  It's still not great, but it completes and returns the correct         */
/* number of rows.  It is a bit slower, approximately 30s on my machine.                                          */
/* The algorithm goes through each numbe 1-20 and places the number in a random location in the result array.     */
/* It then goes through and makes sure no number is listed consecutively and then finds the 20s.                  */
/* I wish it didn't traverse the array as many times as it does.                                                  */
/******************************************************************************************************************/
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TechnicalExercise {
    public static void main(String args[]) {
        int[] distribution = new int[] { 83000, /* 1 */
                83000, /* 2 */
                83000, /* 3 */
                83000, /* 4 */
                83000, /* 5 */
                83000, /* 6 */
                83000, /* 7 */
                83000, /* 8 */
                83000, /* 9 */
                83000, /* 10 */
                83000, /* 11 */
                83000, /* 12 */
                1000, /* 13 */
                500, /* 14 */
                250, /* 15 */
                100, /* 16 */
                50, /* 17 */
                25, /* 18 */
                10, /* 19 */
                5 /* 20 */
        };

        ArrayList<Integer> excludes = new ArrayList<Integer>();
        Random random = new Random();
        int[] result = new int[997940];
        int[] cleanedResult = new int[997940];
        ArrayList<Integer> remainingLocations = initializeRemainingLocationsArray(result);
        int nextIndex;
        int nextLocation;

        Arrays.fill(result, 0); // Initialize the array to 0

        // Go through the distribution rules and place the numbers somewhere randomly
        for (int i = 0; i < distribution.length; i++) {
            // Continue placing each number until the allotment of that number is depleted
            while (distribution[i] > 0) {
                /*
                 * remainingLocations hold the indices are still available in the result array.
                 * Each random spot is chosen from the remaining locations
                 */
                nextIndex = getRandomNumber(0, remainingLocations.size() - 1, random, remainingLocations);
                nextLocation = remainingLocations.get(nextIndex);

                // Place the number in the randomly chosen location, update dist info, and
                // remove that location from remaining list
                if (result[nextLocation] == 0) {
                    result[nextLocation] = (i + 1);
                    remainingLocations.remove(nextIndex);
                    distribution[i]--;
                }
            }
        }

        cleanedResult = cleanUpOutput(result);
        writeOutputToFile(cleanedResult);
        System.out.println("Rows with 20 " + findTwenties(result));
    }

    // Return a random number, excluding the ones that have already used up their
    // number of instances
    private static int getRandomNumber(int start, int end, Random random, ArrayList<Integer> remainingLocations) {
        int rnd;
        rnd = start + random.nextInt((end - start) + 1);

        return rnd;
    }

    // Initialized the remaining list array so that the value is equal to the index
    private static ArrayList<Integer> initializeRemainingLocationsArray(int[] result) {
        ArrayList<Integer> remaining = new ArrayList<Integer>();

        for (int i = 0; i < result.length; i++) {
            remaining.add(i);
        }

        return remaining;
    }

    // Goes through the result array and makes sure no values are listed
    // consecutively
    private static int[] cleanUpOutput(int[] result) {
        boolean again = true;

        while (again) {
            again = false;
            for (int i = 0; i < (result.length - 1); i++) {

                if (result[i] == result[i + 1]) {
                    int pos1 = i + 1;
                    int pos2 = ((i + 2) >= result.length) ? 0 : i + 2;
                    again = true;

                    // Swap numbers. Separate this part
                    int temp = result[pos1];
                    result[pos1] = result[pos2];
                    result[pos2] = temp;
                }
            }
        }

        return result;
    }

    // Returns the rows that 20s are on
    private static ArrayList<Integer> findTwenties(int[] result) {
        ArrayList<Integer> twenties = new ArrayList<Integer>();

        for (int i = 0; i < result.length; i++) {
            if (result[i] == 20) {
                twenties.add(i + 1);
            }
        }
        return twenties;
    }

    // Writes the output to a file
    private static void writeOutputToFile(int[] result) {
        FileWriter fileWriter;

        try {
            fileWriter = new FileWriter("test.output");

            try {

                for (int i = 0; i < result.length; i++) {
                    fileWriter.write(Integer.toString(result[i]) + "\n");
                }

            } finally {
                fileWriter.close();
            }
        } catch (IOException e) {
            System.out.println("en error has occurred creating the file");
            e.printStackTrace();
        }
    }
}