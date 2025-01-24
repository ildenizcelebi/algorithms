public class Searching {
    public static int linearSearch(int[] A, int x) {
        int size = A.length;
        for (int i = 0; i < size; i++) {
            if (A[i] == x) {
                return i;
            }
        }
        return -1;
    }
    public static int binarySearch(int[] A, int x) {
        int low = 0;
        int high = A.length - 1;
        while (high - low > 1) {
            int mid = (high + low) / 2;
            if (A[mid] < x) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        if (A[low] == x) {
            return low;
        } else if (A[high] == x) {
            return high;
        }
        return -1;
    }
}
