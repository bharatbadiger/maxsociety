package co.bharat.maxsociety.enums;

import java.util.ArrayList;
import java.util.List;

public enum Relationships {
	SELF,
	FATHER,
    MOTHER,
    DAUGHTER,
    SON,
    BROTHER,
    SISTER,
    TENANT,
    OTHER;

    public static Relationships getRelationships(String status) {
        for(Relationships s: values()) {
            if(s.name().equals(status)) {
                return s;
            }
        }
        return null;
    }

    public static List<String> getStages() {
        List<String> stages = new ArrayList<>();
        for(Relationships s: values()) {
            stages.add(s.name());
        }
        return stages;
    }

}
