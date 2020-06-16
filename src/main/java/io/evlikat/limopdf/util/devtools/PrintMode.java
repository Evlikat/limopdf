package io.evlikat.limopdf.util.devtools;

import lombok.Getter;
import lombok.Setter;

public enum PrintMode {

    INSTANCE;

    @Setter
    @Getter
    private boolean debug = Boolean.parseBoolean(System.getProperty("io.evlikat.limopdf.debug-mode-enabled", "false"));
}
