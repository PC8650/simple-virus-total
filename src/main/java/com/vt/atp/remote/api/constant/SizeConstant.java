package com.vt.atp.remote.api.constant;

import java.math.BigDecimal;

public interface SizeConstant {

    BigDecimal B2M_DIVIDE = new BigDecimal(1024 * 1024);

    int SIZE_BOUNDARIES = 32;

    int SIZE_LIMIT = 650;
}
