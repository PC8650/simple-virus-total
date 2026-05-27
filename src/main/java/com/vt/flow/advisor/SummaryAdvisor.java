package com.vt.flow.advisor;

import com.google.gson.Gson;
import com.vt.exception.WrapperException;
import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.component.ExternalSkillManager;
import com.vt.flow.dto.InputContent;
import com.vt.flow.utils.FlowSseUtil;
import com.vt.flow.dto.ReportContent;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import com.vt.enums.MsgEnum;
import com.vt.utils.MessageUtils;

import java.util.List;

/**
 * 总结顾问。获取模型汇总
 */
@Component
@RequiredArgsConstructor
public class SummaryAdvisor implements StreamAdvisor {

    private final Gson gson;

    private final ExternalSkillManager externalSkillManager;

    private String systemPrompt(ReportContent reportContent, String contentJson) {
        String skillContent = reportContent.getType().getSkill().getSkillContent();
        if (!StringUtils.hasText(skillContent)) {
            throw new WrapperException(MessageUtils.getMessage(MsgEnum.SYS_SKILL_LOAD_ERROR));
        }

        String externalSkill = externalSkillManager.externalGuidBuild(contentJson);

        if (StringUtils.hasText(externalSkill)) {
            return skillContent + "\n\n" + externalSkill;
        }
        return skillContent;
    }

    private String usrPrompt(InputContent inputContent, String contentJson) {
        StringBuilder builder = new StringBuilder();
        String lang = inputContent.getLanguage();

        // 1. 核心任务指令
        builder.append(MessageUtils.getMessage(lang, MsgEnum.PROMPT_SUMMARY_TASK, inputContent.getLanguage()))
                .append("\n");

        // 2. 补充背景上下文
        if (StringUtils.hasText(inputContent.getDescription())) {
            builder.append(MessageUtils.getMessage(lang, MsgEnum.PROMPT_SUMMARY_BG_TITLE))
                    .append(inputContent.getDescription())
                    .append("\n\n");
        }

        // 3. 数据载荷
        builder.append(MessageUtils.getMessage(lang, MsgEnum.PROMPT_SUMMARY_DATA_TITLE))
                .append(contentJson);

        return builder.toString();
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Flux<ChatClientResponse> adviseStream(@NotNull ChatClientRequest chatClientRequest, @NotNull StreamAdvisorChain streamAdvisorChain) {
        ChainKey.CURRENT.get(chatClientRequest).set(getName());
        ReportContent reportContent = ChainKey.REPORT_SUMMARY.get(chatClientRequest);
        String contentJson = gson.toJson(reportContent);

        // 使用工具类推送实况
        InputContent inputContent = ChainKey.INPUT.get(chatClientRequest);
        String lang = inputContent.getLanguage();
        FlowSseUtil.sendNotMainText(chatClientRequest, getName(),
                MessageUtils.getMessage(lang, MsgEnum.SSE_SUMMARY_START));

        String sysPrompt = systemPrompt(reportContent, contentJson);

        // 将 JSON 数据作为用户提示词，并加上明确的分析引导语
        String usrPrompt = usrPrompt(inputContent, contentJson);

        Message systemMessage = new SystemMessage(sysPrompt);
        Message userMessage = new UserMessage(usrPrompt);

        ChatClientRequest newRequest = ChatClientRequest
                .builder()
                .context(chatClientRequest.context())
                .prompt(new Prompt(List.of(systemMessage, userMessage), chatClientRequest.prompt().getOptions()))
                .build();

        return streamAdvisorChain.nextStream(newRequest);
    }

    @NotNull
    @Override
    public String getName() {
        return "SummaryAdvisor";
    }

    @Override
    public int getOrder() {
        return 30;
    }
}
