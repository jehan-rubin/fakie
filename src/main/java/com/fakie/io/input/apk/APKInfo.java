package com.fakie.io.input.apk;

import java.util.HashMap;
import java.util.Map;

public class APKInfo {
    private final Map<String, Entry> infoTable = new HashMap<>();

    public void add(Entry entry) {
        infoTable.put(entry.getApk(), entry);
    }

    public Entry get(String apk) {
        return infoTable.get(apk);
    }

    public boolean contains(String apk) {
        return infoTable.containsKey(apk);
    }

    public static class Entry {
        private final String name;
        private final String pkg;
        private final String apk;

        public Entry(String name, String pkg, String apk) {
            this.name = name;
            this.pkg = pkg;
            this.apk = apk;
        }

        public String getName() {
            return name;
        }

        public String getPkg() {
            return pkg;
        }

        public String getApk() {
            return apk;
        }
    }
}
