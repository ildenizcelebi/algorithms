import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

class Main {
    public static void main(String args[]) throws IOException {


        int[] sizes = {500, 1000, 2000, 4000, 8000, 16_000, 32_000, 64_000, 128_000, 250_000};
        int[] array = ReadFile.getData(args[0]);

        double[][] randomTimes = new double[3][10];
        double[][] sortedTimes = new double[3][10];
        double[][] reversedTimes = new double[3][10];
        double[][] searchTimes = new double[3][10];


        time_insertionSort(array, randomTimes, sizes);
        time_mergeSort(array, randomTimes, sizes);
        time_countingSort(array, randomTimes, sizes);
        time_linearSearch(array, searchTimes, sizes, false);

        Arrays.sort(array);
        time_insertionSort(array, sortedTimes, sizes);
        time_mergeSort(array, sortedTimes, sizes);
        time_countingSort(array, sortedTimes, sizes);
        time_linearSearch(array, searchTimes, sizes, true);
        time_binarySearch(array, searchTimes, sizes);

        reverseArray(array);
        time_insertionSort(array, reversedTimes, sizes);
        time_mergeSort(array, reversedTimes, sizes);
        time_countingSort(array, reversedTimes, sizes);

        showAndSaveChart("Tests on Random Data", sizes, randomTimes, true);
        showAndSaveChart("Tests on Sorted Data", sizes, sortedTimes, true);
        showAndSaveChart("Tests on Reversed Sorted Data", sizes, reversedTimes, true);
        showAndSaveChart("Tests on Searching", sizes, searchTimes, false);


    }

    public static void showAndSaveChart(String title, int[] xAxis, double[][] yAxis, boolean isItSort) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle(isItSort?"Time in Milliseconds":"Time in Nanoseconds").xAxisTitle("Input Size").build();

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        // Add a plot for a sorting algorithm
        if (isItSort) {
            chart.addSeries("Insertion Sort", doubleX, yAxis[0]);
            chart.addSeries("Merge Sort", doubleX, yAxis[1]);
            chart.addSeries("Counting Sort", doubleX, yAxis[2]);
        } else {
            chart.addSeries("Linear Search on Random Data", doubleX, yAxis[0]);
            chart.addSeries("Linear Search on Sorted Data", doubleX, yAxis[1]);
            chart.addSeries("Binary Search on Sorted Data", doubleX, yAxis[2]);
        }
        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();
    }


    public static void time_insertionSort(int[] array, double[][] times, int[] sizes) {
        double time1 = 0, time2 = 0, sumOfTimes = 0;
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < 10; j++){
                int[] indexedArray;
                indexedArray = Arrays.copyOfRange(array, 0, sizes[i]);
                time1 = System.currentTimeMillis();
                Sorting.insertionSort(indexedArray);
                time2 = System.currentTimeMillis();
                sumOfTimes += (time2 - time1);
            }
            times[0][i] = sumOfTimes/10;
            System.out.println("Insertion Sort Time taken: " + times[0][i] + " for size: " + sizes[i]);
            sumOfTimes = 0;
        }
    }
    public static void time_mergeSort(int[] array, double[][] times, int[] sizes){
        double time1 = 0, time2 = 0, sumOfTimes = 0;
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < 10; j++){
                int[] indexedArray;
                indexedArray = Arrays.copyOfRange(array, 0, sizes[i]);
                time1 = System.currentTimeMillis();
                Sorting.mergeSort(indexedArray);
                time2 = System.currentTimeMillis();
                sumOfTimes += (time2 - time1);
            }
            times[1][i] = sumOfTimes/10;
            System.out.println("Merge Sort Time taken: " + times[1][i] + " for size: " + sizes[i]);
            sumOfTimes = 0;
        }
    }

    public static void time_countingSort(int[] array, double[][] times, int[] sizes){
        double time1 = 0, time2 = 0, sumOfTimes = 0;
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < 10; j++){
                int[] indexedArray;
                indexedArray = Arrays.copyOfRange(array, 0, sizes[i]);
                time1 = System.currentTimeMillis();
                int maxElement = findMax(indexedArray);
                Sorting.countingSort(indexedArray, maxElement);
                time2 = System.currentTimeMillis();
                sumOfTimes += (time2 - time1);
            }
            times[2][i] = sumOfTimes/10;
            System.out.println("Counting Sort Time taken: " + times[2][i] + " for size: " + sizes[i]);
            sumOfTimes = 0;
        }
    }

    public static void time_linearSearch(int[] array, double[][] times, int[] sizes, boolean isSorted){
        Random random = new Random();
        double time1 = 0, time2 = 0, sumOfTimes = 0;
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < 1000; j++){
                int[] indexedArray;
                indexedArray = Arrays.copyOfRange(array, 0, sizes[i]);
                int randomIndex = random.nextInt(sizes[i]);
                int randomNumber = indexedArray[randomIndex];
                time1 = System.nanoTime();
                Searching.linearSearch(indexedArray, randomNumber);
                time2 = System.nanoTime();
                sumOfTimes += (time2 - time1);
            }
            if (isSorted) {
                times[1][i] = sumOfTimes/1000;
                System.out.println("Linear Search Time taken: " + times[1][i] + " for size: " + sizes[i]);
                sumOfTimes = 0;
            } else {
                times[0][i] = sumOfTimes/1000;
                System.out.println("Linear Search Time taken: " + times[0][i] + " for size: " + sizes[i]);
                sumOfTimes = 0;
            }

        }
    }

    public static void time_binarySearch(int[] array, double[][] times, int[] sizes){
        Random random = new Random();
        double time1 = 0, time2 = 0, sumOfTimes = 0;
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < 1000; j++){
                int[] indexedArray;
                indexedArray = Arrays.copyOfRange(array, 0, sizes[i]);
                int randomIndex = random.nextInt(sizes[i]);
                int randomNumber = indexedArray[randomIndex];
                time1 = System.nanoTime();
                Searching.binarySearch(indexedArray, randomNumber);
                time2 = System.nanoTime();
                sumOfTimes += (time2 - time1);
            }
            times[2][i] = sumOfTimes/1000;
            System.out.println("Binary Search Time taken: " + times[2][i] + " for size: " + sizes[i]);
            sumOfTimes = 0;
        }
    }
    public static void printArray(int[] arr) {
        System.out.println("The array:");
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

    private static void reverseArray(int[] array) {
        int left = 0;
        int right = array.length - 1;
        while (left < right) {
            int temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
    }
    public static Integer findMax(int[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
}