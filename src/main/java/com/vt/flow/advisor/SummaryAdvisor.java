package com.vt.flow.advisor;

import com.google.gson.Gson;
import com.vt.exception.WrapperException;
import com.vt.flow.advisor.constant.ChainKey;
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

import java.util.List;

/**
 * 总结顾问。获取模型汇总
 */
@Component
@RequiredArgsConstructor
public class SummaryAdvisor implements StreamAdvisor {

    private final Gson gson;

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Flux<ChatClientResponse> adviseStream(@NotNull ChatClientRequest chatClientRequest, @NotNull StreamAdvisorChain streamAdvisorChain) {
        ChainKey.CURRENT.get(chatClientRequest).set(getName());
        ReportContent reportContent = ChainKey.REPORT_SUMMARY.get(chatClientRequest);

        // 使用工具类推送实况
        FlowSseUtil.sendNotMainText(chatClientRequest, getName(), "数据采集完成，启动深度分析...");

        String sysPrompt = reportContent.getType().getSkill().getSkillContent();
        if (!StringUtils.hasText(sysPrompt)) throw new WrapperException("Failed to load skill content");

        // 将 JSON 数据作为用户提示词，并加上明确的分析引导语
        InputContent inputContent = ChainKey.INPUT.get(chatClientRequest);
        String usrPrompt =
                String.format(
                        "请根据以下提供的原始 JSON 数据，严格执行你系统提示词中的专家分析流程，并使用使用语言[%s]输出结果：\n", inputContent.getLanguage()
                ) + gson.toJson(reportContent);

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
