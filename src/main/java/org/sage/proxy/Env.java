package org.sage.proxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Env {
    private final Map<String, String> vals;

    public Env() throws FileNotFoundException {
        this(".env");
    }

    public Env(String path) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(path));
        Map<String, String> val = new HashMap<>();
        while (scan.hasNextLine()) {
            String[] line = scan.nextLine()
                    .replace(" ", "")
                    .split("=");
            val.put(line[0], line[1]);
        }

        vals = Collections.unmodifiableMap(val);
    }

    public Optional<String> getOpt(String name) {
        return Optional.ofNullable(name)
                .map(vals::get);
    }

    public String get(String name) {
        return Optional.ofNullable(name)
                .map(vals::get)
                .orElseThrow(() -> new IllegalArgumentException("Value with name " + name + "not found"));
    }

    @Override
    public String toString() {
        return "Env{" +
                "vals=" + vals +
                '}';
    }
}
