package com.xuaiyuan.smp.common.validator;

import com.xuaiyuan.smp.common.exception.CustomException;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据校验
 *
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new CustomException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new CustomException(message);
        }
    }
}
