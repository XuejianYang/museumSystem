package hue.edu.xiong.util;

import java.util.UUID;

public class IdGenerator {

    public static String id() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
