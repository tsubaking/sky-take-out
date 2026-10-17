package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

@RestController
@Slf4j
@Api(tags = "数据统计相关接口")
@RequestMapping("/admin/report")
public class ReportController {

    @Resource
    private ReportService reportService;

    @GetMapping("/turnoverStatistics")
    @Operation(description = "营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end) {

        log.info("营业额统计，{}到{}",begin,end);
        TurnoverReportVO vo = reportService.turnoverStatistics(begin,end);
        return Result.success(vo);
    }
}
