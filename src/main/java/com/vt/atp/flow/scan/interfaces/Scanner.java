package com.vt.atp.flow.scan.interfaces;

import com.vt.atp.flow.dto.InputContent;
import com.vt.atp.flow.enums.TypeEnum;
import com.vt.atp.remote.dto.VtResult;
import com.vt.atp.remote.dto.vt.abs.UploadScanResp;

public interface Scanner {

    VtResult<? extends UploadScanResp> scan(InputContent input);

    TypeEnum type();

}
