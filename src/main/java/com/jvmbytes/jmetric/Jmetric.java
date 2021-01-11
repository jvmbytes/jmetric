package com.jvmbytes.jmetric;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Method;

/**
 * @author wongoo
 */
public class Jmetric {

    private Jmetric() {
    }

    private final static OperatingSystemMXBean OPERATING_SYSTEM_BEAN = ManagementFactory.getOperatingSystemMXBean();
    private final static ThreadMXBean THREAD_BEAN = ManagementFactory.getThreadMXBean();

    private static Method detectMethod(String name) {
        try {
            Method method = OPERATING_SYSTEM_BEAN.getClass().getDeclaredMethod(name);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private final static Method PROCESS_CPU_TIME_METHOD = detectMethod("getProcessCpuTime");
    private final static Method SYSTEM_CPU_LOAD_METHOD = detectMethod("getSystemCpuLoad");
    private final static Method PROCESS_CPU_LOAD_METHOD = detectMethod("getProcessCpuLoad");
    private final static Method COMMITTED_VIRTUAL_MEMORY_SIZE_METHOD = detectMethod("getCommittedVirtualMemorySize");
    private final static Method TOTAL_SWAP_SPACE_SIZE_METHOD = detectMethod("getTotalSwapSpaceSize");
    private final static Method FREE_SWAP_SPACE_SIZE_METHOD = detectMethod("getFreeSwapSpaceSize");
    private final static Method FREE_PHYSICAL_MEMORY_SIZE_METHOD = detectMethod("getFreePhysicalMemorySize");
    private final static Method TOTAL_PHYSICAL_MEMORY_SIZE_METHOD = detectMethod("getTotalPhysicalMemorySize");


    private static Object invoke(Method method) {
        try {
            return method != null ? method.invoke(OPERATING_SYSTEM_BEAN) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private static String invokeString(Method method) {
        Object value = invoke(method);
        return value == null ? "" : (String) value;
    }

    private static long invokeLong(Method method) {
        Object value = invoke(method);
        return value == null ? 0L : (long) value;
    }

    private static double invokeDouble(Method method) {
        Object value = invoke(method);
        return value == null ? 0L : (double) value;
    }

    public static String getOsName() {
        return OPERATING_SYSTEM_BEAN.getName();
    }

    public static String getOsVersion() {
        return OPERATING_SYSTEM_BEAN.getVersion();
    }

    public static String getArch() {
        return OPERATING_SYSTEM_BEAN.getArch();
    }

    public static long getAvailableProcessors() {
        return OPERATING_SYSTEM_BEAN.getAvailableProcessors();
    }

    public static long getProcessCpuTime() {
        return invokeLong(PROCESS_CPU_TIME_METHOD);
    }

    public static double getSystemCpuLoad() {
        return invokeDouble(SYSTEM_CPU_LOAD_METHOD);
    }

    public static double getProcessCpuLoad() {
        return invokeDouble(PROCESS_CPU_LOAD_METHOD);
    }

    public static double getSystemLoadAverage() {
        return OPERATING_SYSTEM_BEAN.getSystemLoadAverage();
    }

    public static long getCommittedVirtualMemorySize() {
        return invokeLong(COMMITTED_VIRTUAL_MEMORY_SIZE_METHOD);
    }

    public static long getTotalSwapSpaceSize() {
        return invokeLong(TOTAL_SWAP_SPACE_SIZE_METHOD);
    }

    public static long getFreeSwapSpaceSize() {
        return invokeLong(FREE_SWAP_SPACE_SIZE_METHOD);
    }

    public static long getFreePhysicalMemorySize() {
        return invokeLong(FREE_PHYSICAL_MEMORY_SIZE_METHOD);
    }

    public static long getTotalPhysicalMemorySize() {
        return invokeLong(TOTAL_PHYSICAL_MEMORY_SIZE_METHOD);
    }

    public static MemoryUsage getHeapMemoryUsage() {
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
    }


    public static long getTotalStartedThreadCount() {
        return THREAD_BEAN.getTotalStartedThreadCount();
    }

    public static long getThreadCount() {
        return THREAD_BEAN.getThreadCount();
    }

    public static long getDaemonThreadCount() {
        return THREAD_BEAN.getDaemonThreadCount();
    }

    public static long getPeakThreadCount() {
        return THREAD_BEAN.getPeakThreadCount();
    }

    private static void printMetrics() {
        System.out.printf("%32s: %s\n", "OS Name", getOsName());
        System.out.printf("%32s: %s\n", "OS Version", getOsVersion());
        System.out.printf("%32s: %s\n", "Arch", getArch());
        System.out.printf("%32s: %d\n", "Available Processors", getAvailableProcessors());
        System.out.printf("%32s: %.2f\n", "System Load Average", getSystemLoadAverage());
        System.out.printf("%32s: %.2f\n", "System Cpu Load", getSystemCpuLoad());
        System.out.printf("%32s: %.2f\n", "Process Cpu Load", getProcessCpuLoad());
        System.out.printf("%32s: %d\n", "Process Cpu Time", getProcessCpuTime());
        System.out.printf("%32s: %.2f MB\n", "Total Physical Memory Size", megabytes(getTotalPhysicalMemorySize()));
        System.out.printf("%32s: %.2f MB\n", "Free Physical Memory Size", megabytes(getFreePhysicalMemorySize()));
        System.out.printf("%32s: %.2f MB\n", "Total Swap Space Size", megabytes(getTotalSwapSpaceSize()));
        System.out.printf("%32s: %.2f MB\n", "Free Swap Space Size", megabytes(getFreeSwapSpaceSize()));
        System.out.printf("%32s: %.2f MB\n", "Committed Virtual Memory Size", megabytes(getCommittedVirtualMemorySize()));

        MemoryUsage memoryUsage = getHeapMemoryUsage();
        System.out.printf("%32s: %.2f MB\n", "Heap Size Init", megabytes(memoryUsage.getInit()));
        System.out.printf("%32s: %.2f MB\n", "Heap Size Max", megabytes(memoryUsage.getMax()));
        System.out.printf("%32s: %.2f MB\n", "Heap Size Committed", megabytes(memoryUsage.getCommitted()));
        System.out.printf("%32s: %.2f MB\n", "Heap Size Used", megabytes(memoryUsage.getUsed()));

        System.out.printf("%32s: %d\n", "Total Started Thread Count", getTotalStartedThreadCount());
        System.out.printf("%32s: %d\n", "Thread Count", getThreadCount());
        System.out.printf("%32s: %d\n", "Daemon Thread Count", getDaemonThreadCount());
        System.out.printf("%32s: %d\n", "Peak Thread Count", getPeakThreadCount());
    }

    private static double megabytes(long size) {
        return ((double) size) / (1024 * 1024);
    }

    public static void main(String[] args) throws Exception {
        printMetrics();
        if (args.length > 0) {
            long sleep = Long.parseLong(args[0]);
            Thread.sleep(sleep);
        }
    }

}
