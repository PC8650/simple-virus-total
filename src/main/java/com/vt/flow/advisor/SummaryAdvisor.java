package com.vt.flow.advisor;

import com.google.gson.Gson;
import com.vt.enums.MsgEnum;
import com.vt.exception.WrapperException;
import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.component.ExternalSkillManager;
import com.vt.flow.dto.InputContent;
import com.vt.flow.dto.ReportContent;
import com.vt.flow.enums.ContentEnum;
import com.vt.flow.utils.FlowSseUtil;
import com.vt.utils.MessageUtils;
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

import java.util.List;

/**
 * 总结顾问。获取模型汇总
 */
@Component
@RequiredArgsConstructor
public class SummaryAdvisor implements StreamAdvisor {

    private final Gson gson;

    private final ExternalSkillManager externalSkillManager;

    private String systemPrompt(String lang, ReportContent reportContent, String contentJson) {
        String skillContent = reportContent.getType().getSkill().getSkillContent();
        if (!StringUtils.hasText(skillContent)) {
            throw new WrapperException(MessageUtils.getMessage(lang, MsgEnum.SYS_SKILL_LOAD_ERROR));
        }

        String externalSkill = externalSkillManager.externalGuidBuild(lang, contentJson);

        if (StringUtils.hasText(externalSkill)) {
            return skillContent + "\n\n" + externalSkill;
        }
        return skillContent;
    }

    private String usrPrompt(String lang, String desc, String contentJson) {
        StringBuilder builder = new StringBuilder();

        // 1. 核心任务指令
        builder.append(MessageUtils.getMessage(lang, MsgEnum.PROMPT_SUMMARY_TASK, lang))
                .append("\n");

        // 2. 补充背景上下文
        if (StringUtils.hasText(desc)) {
            builder.append(MessageUtils.getMessage(lang, MsgEnum.PROMPT_SUMMARY_BG_TITLE))
                    .append(desc)
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
        FlowSseUtil.send(chatClientRequest, getName(), ContentEnum.NOTICE,
                MessageUtils.getMessage(lang, MsgEnum.SSE_SUMMARY_PROMPT));

        String sysPrompt = systemPrompt(lang, reportContent, contentJson);

        // 将 JSON 数据作为用户提示词，并加上明确的分析引导语
        String usrPrompt = usrPrompt(lang, inputContent.getDescription(), contentJson);

        //推送最终的提示词，用于审查核实
        FlowSseUtil.send(chatClientRequest, getName(), ContentEnum.NOTICE,
                MessageUtils.getMessage(lang, MsgEnum.SSE_SUMMARY_PROMPT_AUDIT));

        FlowSseUtil.send(chatClientRequest, getName(), ContentEnum.NOTICE,
                MessageUtils.getMessage(lang, MsgEnum.SSE_SUMMARY_PROMPT_SYS));

        FlowSseUtil.send(chatClientRequest, getName(), ContentEnum.PROMPT, sysPrompt);

        FlowSseUtil.send(chatClientRequest, getName(), ContentEnum.NOTICE,
                MessageUtils.getMessage(lang, MsgEnum.SSE_SUMMARY_PROMPT_USR));

        FlowSseUtil.send(chatClientRequest, getName(), ContentEnum.PROMPT, usrPrompt);

        Message systemMessage = new SystemMessage(sysPrompt);
        Message userMessage = new UserMessage(usrPrompt);

        ChatClientRequest newRequest = ChatClientRequest
                .builder()
                .context(chatClientRequest.context())
                .prompt(new Prompt(List.of(systemMessage, userMessage), chatClientRequest.prompt().getOptions()))
                .build();

        FlowSseUtil.send(chatClientRequest, getName(), ContentEnum.NOTICE,
                MessageUtils.getMessage(lang, MsgEnum.SSE_SUMMARY_START_ANALYSE));

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