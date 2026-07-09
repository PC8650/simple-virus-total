package com.vt.flow.advisor;

import com.vt.enums.MsgEnum;
import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.component.CacheManager;
import com.vt.flow.dto.CacheDto;
import com.vt.flow.dto.ReportContent;
import com.vt.flow.enums.ContentEnum;
import com.vt.flow.enums.TypeEnum;
import com.vt.flow.scan.interfaces.Scanner;
import com.vt.flow.utils.FlowSseUtil;
import com.vt.flow.utils.PollUtil;
import com.vt.remote.dto.VtResult;
import com.vt.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

/**
 * 报告顾问。获取报告
 */
@Component
@RequiredArgsConstructor
public class ReportAdvisor implements StreamAdvisor {

    private final String metaKey = "sandboxes_in_progress";

    private final CacheManager cacheManager;

    private VtResult<?> getBehaviourReport(Scanner scanner, String reportId, ChatClientRequest chatClientRequest) {
        com.vt.flow.dto.InputContent inputContent = ChainKey.INPUT.get(chatClientRequest);
        String lang = inputContent.getLanguage();

        FlowSseUtil.send(chatClientRequest, getName(), ContentEnum.NOTICE,
                MessageUtils.getMessage(lang, MsgEnum.SSE_REPORT_SANDBOX_START, reportId));
        return PollUtil.poll(600000L, 30000L,
                () -> {
                    VtResult<?> behaviourReport = scanner.getBehaviourReport(reportId);
                    String process = behaviourReport.getMeta().getOrDefault(metaKey, "[]").toString();
                    FlowSseUtil.send(chatClientRequest, getName(), ContentEnum.NOTICE,
                            MessageUtils.getMessage(lang, MsgEnum.SSE_REPORT_SANDBOX_POLLING, process));
                    return behaviourReport;
                },
                (r) -> !r.getMeta().containsKey(metaKey));
    }

    private String getReportId(ChatClientRequest chatClientRequest, Scanner scanner) {
        //一些特殊的url，例如 https://xxx/#xxx vt会减去后缀，导致入参计算的缓存id和实际id不一致
        //进行对比对齐
        String reportId = scanner.getReportId(chatClientRequest);

        CacheDto cacheDto = ChainKey.CACHE.get(chatClientRequest);
        String cacheId = cacheDto.getReportId();

        if (StringUtils.hasText(reportId)) {
            if (!reportId.equals(cacheId)) {
                cacheDto.setReportId(reportId);
                cacheManager.put(cacheDto);
            }
        }else {
            reportId = cacheId;
        }

        return  reportId;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Flux<ChatClientResponse> adviseStream(@NotNull ChatClientRequest chatClientRequest, @NotNull StreamAdvisorChain streamAdvisorChain) {
        ChainKey.CURRENT.get(chatClientRequest).set(getName());
        Scanner scanner = ChainKey.SCANNER.get(chatClientRequest);

        String reportId = getReportId(chatClientRequest, scanner);

        // 获取报告
        String lang = ChainKey.INPUT.get(chatClientRequest).getLanguage();
        FlowSseUtil.send(chatClientRequest, getName(), ContentEnum.NOTICE,
                MessageUtils.getMessage(lang, MsgEnum.SSE_REPORT_FETCH, reportId));
        VtResult<?> report = scanner.getReport(reportId);

        // 获取行为报告
        TypeEnum type = scanner.type();
        Object behaviourReportData = null;
        Object behaviourMitreData = null;
        if (TypeEnum.FILE.equals(type)) {
            VtResult<?> behaviourReport = getBehaviourReport(scanner, reportId, chatClientRequest);
            behaviourReportData = behaviourReport.getData();
            // 获取 战术/技术 汇总
            FlowSseUtil.send(chatClientRequest, getName(), ContentEnum.NOTICE,
                    MessageUtils.getMessage(lang, MsgEnum.SSE_REPORT_MITRE_START, reportId));
            VtResult<?> behaviourMitre = scanner.getBehaviourMitre(reportId);
            behaviourMitreData = behaviourMitre.getData();
        }

        FlowSseUtil.send(chatClientRequest, getName(), ContentEnum.NOTICE,
                MessageUtils.getMessage(lang, MsgEnum.SSE_REPORT_SUMMARY, reportId));

        ReportContent reportContent = new ReportContent()
                .setType(type)
                .setUrl(type.guiUrl(reportId))
                .setReport(report.getData())
                .setBehaviour(behaviourReportData)
                .setMitre(behaviourMitreData);

        ChainKey.REPORT_SUMMARY.put(chatClientRequest, reportContent);

        return streamAdvisorChain.nextStream(chatClientRequest);
    }

    @NotNull
    @Override
    public String getName() {
        return "ReportAdvisor";
    }

    @Override
    public int getOrder() {
        return 20;
    }

}
