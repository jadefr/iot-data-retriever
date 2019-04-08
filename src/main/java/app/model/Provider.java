package app.model;

import java.util.List;

public class Provider {

    private final static String TYPE_SOLCAST = "SOLCAST";

    private final static String TYPE_DARKSKY = "DARKSKY";

    public String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

  }
