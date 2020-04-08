package idUtil;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class UniqueId {
    public static String[] chars = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private static final int SEQ_DEFAULT = 0;
    private static final int RADIX = 62;
    private static final int SEQUENCE_LEFT = 12;
    private static final int SIGNID_LEFT = 10;
    private static final int MAX_SIGNID = 1024;
    private volatile AtomicInteger orderedIdSequence = new AtomicInteger(0);
    private Long lastTime = 0L;
    private int workId = 0;

    public UniqueId() {

        int workId = (new SecureRandom()).nextInt(MAX_SIGNID);
        if(workId > MAX_SIGNID || workId < 0){
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_SIGNID));
        }
        workId = 0;
    }

    public UniqueId(int signId) {
        if(signId < MAX_SIGNID || signId >= 0){
            this.workId = signId;
        } else {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", 1024));
        }
    }

    public long nextInt() {
        return this.generateId();
    }

    public String nextString() {
        return radixConvert(this.generateId(), 62);
    }

    public synchronized long generateId() {
        long currentTime = System.currentTimeMillis();
        if (this.lastTime < currentTime) {
            this.orderedIdSequence.set(0);
        } else if (this.lastTime > currentTime) {
            throw new RuntimeException("Clock moved backwards.");
        }

        if (this.orderedIdSequence.get() >= 4096) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.orderedIdSequence.set(0);
        }

        long resultId = currentTime << 22 | (long)(this.workId << 12) | (long)this.orderedIdSequence.getAndAdd(1);
        this.lastTime = System.currentTimeMillis();
        return resultId;
    }

    public static String radixConvert(long num, int radix) {
        if (radix >= 2 && radix <= 62) {
            num = num < 0L ? num * -1L : num;
            String result = "";
            long tmpValue = num;

            while(true) {
                long value = (long)((int)(tmpValue % (long)radix));
                result = chars[(int)value] + result;
                value = tmpValue / (long)radix;
                if (value < (long)radix) {
                    result = chars[(int)value] + result;
                    return result;
                }

                tmpValue = value;
            }
        } else {
            return null;
        }
    }
}
