package com.vt.atp.api.constant;

public interface ApiConstant {

    String PREFIX = "https://www.virustotal.com/api/v3";

    String UPLOAD_LT_32 = "/files";

    String GET_UPLOAD_GT_32_URL = "/files/upload_url";

    String GET_ANALYSE = "/analyses/";

    // /files/{id}/analyse
    String RE_ANALYSE = "/files/%s/analyse";

    String GET_REPORT = "/files/";

    // /files/{id}/behaviours
    String GET_BEHAVIOUR_REPORT = "/files/%s/behaviours";
}
