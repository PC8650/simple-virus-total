package com.vt.atp.remote.api.constant;

public interface ApiConstant {

    String PREFIX = "https://www.virustotal.com/api/v3";

    //文件

    String UPLOAD_LT_32 = "/files";

    String GET_UPLOAD_GT_32_URL = "/files/upload_url";

    // /analyses/{analyses_id}
    String GET_ANALYSE = "/analyses/%s";

    // /files/{id}/analyse
    String RE_ANALYSE_FILE = "/files/%s/analyse";

    // /files/{id}
    String GET_FILE_REPORT = "/files/%s";

    // /files/{id}/behaviours
    String GET_BEHAVIOUR_REPORT = "/files/%s/behaviours";

    //URL

    String SCAN_URL = "/urls";

    // /urls/{id}/analyse
    String RE_ANALYSE_URL = "/urls/%s/analyse";

    // /urls/{id}
    String GET_URL_REPORT = "/urls/%s";

    //域名

    // /domains/{domain}/analyse
    String SCAN_DOMAIN = "/domains/%s/analyse";

    // /domains/{domain}
    String GET_DOMAIN_REPORT = "/domains/%s";

    // ip

    // /ip_addresses/{ip}/analyse
    String SCAN_IP = "/ip_addresses/%s/analyse";

    String GET_IP_REPORT = "/ip_addresses/%s";

}
