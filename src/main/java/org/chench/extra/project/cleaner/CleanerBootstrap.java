package org.chench.extra.project.cleaner;

import org.apache.commons.io.FileUtils;
import org.chench.extra.project.cleaner.util.HexUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chench
 * @desc org.chench.extra.project.cleaner.CleanerBootstrap
 * @date 2024.03.02
 */
public class CleanerBootstrap {
    private static List<PatternTarget> CLEAN_TARGET_LIST = new ArrayList<PatternTarget>();
    private static List<PatternTarget> IGNORE_TARGET_LIST = new ArrayList<PatternTarget>();
    static {
        Properties cleanTargetProp = new Properties();
        Properties ignoreTargetProp = new Properties();
        try {
            cleanTargetProp.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("clean_target.properties"));
            ignoreTargetProp.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("ignore_target.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Enumeration cleanTargetEnumeration = cleanTargetProp.propertyNames();
        while (cleanTargetEnumeration.hasMoreElements()) {
            String name = cleanTargetEnumeration.nextElement().toString();
            int type = HexUtil.parse2Int(cleanTargetProp.getProperty(name));
            CLEAN_TARGET_LIST.add(new PatternTarget(name, type));
        }

        Enumeration ignoreTargetEnumeration = ignoreTargetProp.propertyNames();
        while (ignoreTargetEnumeration.hasMoreElements()) {
            String name = ignoreTargetEnumeration.nextElement().toString();
            int type = HexUtil.parse2Int(ignoreTargetProp.getProperty(name));
            IGNORE_TARGET_LIST.add(new PatternTarget(name, type));
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage:");
            System.out.println(String.format("java -jar <%s.jar> <path> [<path>...]", CleanerBootstrap.class.getSimpleName()));
            return;
        }

        List<String> paths = Arrays.asList(args);
        System.out.println("clean dirs:");
        System.out.println(paths);

        List<File> dirs = new ArrayList<File>();
        for (String path : paths) {
            dirs.addAll(listAllDirectory(path));
        }
        List<File> targetList = findTargetList(dirs);
        System.out.println(String.format("clean target size: %s", targetList.size()));
        cleanTargetList(targetList);
    }

    private static List<File> listAllDirectory(String path) {
        List<File> list = new ArrayList<File>();
        File file = new File(path);
        list.add(file);
        list.addAll(listAllSubDirectory(file));
        return list;
    }

    /**
     * 列出指定路径下的所有子目录
     * @param file
     * @return
     */
    private static List<File> listAllSubDirectory(File file) {
        List<File> list = new ArrayList<File>();
        if (file == null) {
            return list;
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                continue;
            }
            if (filterTargetType(f, IGNORE_TARGET_LIST)) {
                continue;
            }
            if (!filterTargetType(f, CLEAN_TARGET_LIST)) {
                list.add(f);
                list.addAll(listAllSubDirectory(f));
            }
        }
        return list;
    }

    private static List<File> findTargetList(List<File> files) {
        List<File> list = new ArrayList<File>();
        for (File f : files) {
            list.addAll(findTargetList(f));
        }
        return list;
    }

    private static List<File> findTargetList(File path) {
        List<File> list = new ArrayList<File>();
        for (File f : path.listFiles()) {
            if (filterTargetType(f, CLEAN_TARGET_LIST)) {
                list.add(f);
            }
        }
        return list;
    }

    private static void cleanTargetList(List<File> targetList) {
        for (File target: targetList) {
            if (!target.exists()) {
                continue;
            }
            try {
                FileUtils.forceDelete(target);
                System.out.println(String.format("clean target: %s", target.getAbsolutePath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static boolean filterTargetType(File f, List<PatternTarget> targetList) {
        for (PatternTarget target : targetList) {
            Pattern pattern = Pattern.compile(target.getName());
            Matcher matcher = pattern.matcher(f.getName());
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }
}