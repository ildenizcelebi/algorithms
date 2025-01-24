import java.util.Arrays;

public class Sorting {
    public static int[] insertionSort(int[] A) {
        int n = A.length;
        for (int j = 1; j < n; j++) {
            int key = A[j];
            int i = j - 1;
            while (i >= 0 && A[i] > key) {
                A[i + 1] = A[i];
                i = i - 1;
            }
            A[i + 1] = key;
        }
        return A;
    }

    public static int[] mergeSort(int[] A) {
        int n = A.length;
        if (n <= 1) {
            return A;
        }
        int[] left = Arrays.copyOfRange(A, 0, n / 2);
        int[] right = Arrays.copyOfRange(A, n / 2, n);

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }

    public static int[] merge(int[] A, int[] B) {
        int aLength = A.length;
        int bLength = B.length;
        int[] C = new int[aLength + bLength];
        int i = 0, j = 0, k = 0;

        while (i < aLength && j < bLength) {
            if (A[i] <= B[j]) {
                C[k++] = A[i++];
            } else {
                C[k++] = B[j++];
            }
        }
        while (i < aLength) {
            C[k++] = A[i++];
        }
        while (j < bLength) {
            C[k++] = B[j++];
        }
        return C;
    }

    public static int[] countingSort(int[] A, int k) {
        int[] count = new int[k + 1];
        int[] output = new int[A.length];
        int size = A.length;

        Arrays.fill(count, 0);

        for (int i = 0; i < size; i++) {
            int j = A[i];
            count[j]++;
        }

        for (int i = 1; i <= k; i++) {
            count[i] += count[i - 1];
        }

        for (int i = size - 1; i >= 0; i--) {
            int j = A[i];
            count[j]--;
            output[count[j]] = A[i];
        }
        return output;
    }
}
