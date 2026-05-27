package com.vt.flow.enums;

import com.vt.enums.MsgEnum;
import com.vt.exception.WrapperException;
import com.vt.flow.dto.InputContent;
import com.vt.utils.MessageUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
public enum TypeEnum {

    FILE("/file/", SkillEnum.FILE) {
        @Override
        public String getCacheKey(InputContent input) {
            try (InputStream is = input.getFile().getInputStream()) {
                return DigestUtils.sha256Hex(is);
            } catch (IOException e) {
                throw new WrapperException(MessageUtils.getMessage(MsgEnum.SYS_FILE_HASH_ERROR), e);
            }
        }
    },
    URL("/url/", SkillEnum.URL) {
        @Override
        public String getCacheKey(InputContent input) {
            return DigestUtils.sha256Hex(input.getPayload());
        }
    },
    IP("/ip-address/", SkillEnum.IP) {
        @Override
        public String getCacheKey(InputContent input) {
            return input.getPayload();
        }
    },
    DOMAIN("/domain/", SkillEnum.DOMAIN) {
        @Override
        public String getCacheKey(InputContent input) {
            return input.getPayload();
        }
    };

    private static final String GUI_PREFIX = "https://www.virustotal.com/gui";

    private final String guiUri;

    @Getter
    private final SkillEnum skill;

    public abstract String getCacheKey(InputContent input);

    public String guiUrl(String reportId) {
        return GUI_PREFIX + guiUri + reportId;
    }

}
