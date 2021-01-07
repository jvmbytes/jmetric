package com.jvmbytes.jmetric;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;

/**
 * @author wongoo
 */
public class Jmetric {

    private Jmetric() {
    }

    private final static OperatingSystemMXBean OPERATING_SYSTEM_BEAN = ManagementFactory.getOperatingSystemMXBean();

    private static Method detectMethod(String name) {
        try {
            Method method = OPERATING_SYSTEM_BEAN.getClass().getDeclaredMethod(name);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private final static Method osNameMethod = detectMethod("getName");
    private final static Method osVersionMethod = detectMethod("getVersion");
    private final static Method archMethod = detectMethod("getArch");
    private final static Method availableProcessorsMethod = detectMethod("getAvailableProcessors");
    private final static Method processCpuTimeMethod = detectMethod("getProcessCpuTime");
    private final static Method systemCpuLoadMethod = detectMethod("getSystemCpuLoad");
    private final static Method processCpuLoadMethod = detectMethod("getProcessCpuLoad");
    private final static Method systemLoadAverageMethod = detectMethod("getSystemLoadAverage");
    private final static Method committedVirtualMemorySizeMethod = detectMethod("getCommittedVirtualMemorySize");
    private final static Method totalSwapSpaceSizeMethod = detectMethod("getTotalSwapSpaceSize");
    private final static Method freeSwapSpaceSizeMethod = detectMethod("getFreeSwapSpaceSize");
    private final static Method freePhysicalMemorySizeMethod = detectMethod("getFreePhysicalMemorySize");
    private final static Method totalPhysicalMemorySizeMethod = detectMethod("getTotalPhysicalMemorySize");


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
        return invokeString(osNameMethod);
    }

    public static String getOsVersion() {
        return invokeString(osVersionMethod);
    }

    public static String getArch() {
        return invokeString(archMethod);
    }

    public static long getAvailableProcessors() {
        return invokeLong(availableProcessorsMethod);
    }

    public static long getProcessCpuTime() {
        return invokeLong(processCpuTimeMethod);
    }

    public static double getSystemCpuLoad() {
        return invokeDouble(systemCpuLoadMethod);
    }

    public static double getProcessCpuLoad() {
        return invokeDouble(processCpuLoadMethod);
    }

    public static double getSystemLoadAverage() {
        return invokeDouble(systemLoadAverageMethod);
    }

    public static long getCommittedVirtualMemorySize() {
        return invokeLong(committedVirtualMemorySizeMethod);
    }

    public static long getTotalSwapSpaceSize() {
        return invokeLong(totalSwapSpaceSizeMethod);
    }

    public static long getFreeSwapSpaceSize() {
        return invokeLong(freeSwapSpaceSizeMethod);
    }

    public static long getFreePhysicalMemorySize() {
        return invokeLong(freePhysicalMemorySizeMethod);
    }

    public static long getTotalPhysicalMemorySize() {
        return invokeLong(totalPhysicalMemorySizeMethod);
    }

    private static void printMetrics() {
        System.out.printf("%32s: %s\n", "OS Name", getOsName());
        System.out.printf("%32s: %s\n", "OS Version", getOsVersion());
        System.out.printf("%32s: %s\n", "Arch", getArch());
        System.out.printf("%32s: %d\n", "Available Processors", getAvailableProcessors());
        System.out.printf("%32s: %f\n", "System Load Average", getSystemLoadAverage());
        System.out.printf("%32s: %f\n", "System Cpu Load", getSystemCpuLoad());
        System.out.printf("%32s: %f\n", "Process Cpu Load", getProcessCpuLoad());
        System.out.printf("%32s: %d\n", "Process Cpu Time", getProcessCpuTime());
        System.out.printf("%32s: %d\n", "Total Physical Memory Size", getTotalPhysicalMemorySize());
        System.out.printf("%32s: %d\n", "Free Physical Memory Size", getFreePhysicalMemorySize());
        System.out.printf("%32s: %d\n", "Total Swap Space Size", getTotalSwapSpaceSize());
        System.out.printf("%32s: %d\n", "Free Swap Space Size", getFreeSwapSpaceSize());
        System.out.printf("%32s: %d\n", "Committed Virtual Memory Size", getCommittedVirtualMemorySize());
    }

    public static void main(String[] args) {
        printMetrics();
    }

}
