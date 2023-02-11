package org.eaglescript.vm;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Enumeration;

/**
 * {@link GlobalSequence} provides global sequence to be used as thread ID.
 * This is a modified version of snowflake algorithm, by choosing more bits for machine ID compare to original algorithm.
 */
class GlobalSequence {
    private static final int TIME_BITS = 41;
    private static final int SHARD_BITS = 12;
    private static final int SEQ_BITS = 10;

    private static final long TIME_MASK = (1L << TIME_BITS) - 1;
    private static final long SHARD_MASK = (1L << SHARD_BITS) - 1;

    private static final int MAX_SEQ = (1 << SEQ_BITS) - 1;

    private static final int MACHINE_ID = getMachineId();

    private static long lastMillis = System.currentTimeMillis();
    private static int lastSeq = 0;

    private static int getMachineId() {
        int machineId = 0;
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface iface = networkInterfaces.nextElement();
                if (iface.isLoopback()) { // skip loopback
                    continue;
                }

                byte[] hwAddr;
                if ((hwAddr = iface.getHardwareAddress()) != null) {
                    machineId ^= ByteBuffer.wrap(hwAddr).hashCode();
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException("Unable to get machine ID", e);
        }

        return machineId;
    }

    private static long build() {
        long timeBits = (lastMillis & TIME_MASK) << (SHARD_BITS + SEQ_BITS);
        long shardBits = (MACHINE_ID & SHARD_MASK) << SEQ_BITS;
        return timeBits | shardBits | lastSeq;
    }

    static synchronized long nextId() {
        while (true) {
            long millis = System.currentTimeMillis();
            if (millis == lastMillis) {
                if (lastSeq < MAX_SEQ) {
                    lastSeq++;
                    return build();
                }
                continue;
            }

            long elapsed = millis - lastMillis;
            if (elapsed > 0) {
                lastMillis = millis;
                lastSeq = 0;
                return build();
            } else {
                try {
                    Thread.sleep(-elapsed);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
