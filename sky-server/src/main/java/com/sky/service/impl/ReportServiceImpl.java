package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private OrderMapper orderMapper;

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {

        //求出begin到end之间的日期list
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        while(!begin.equals(end)) {
            begin = begin.plusDays(1);
            localDateList.add(begin);
        }
        //查询每一天内的营业额
        List<Double> amounts = new ArrayList<>();
        for (LocalDate localDate : localDateList) {
            LocalDateTime time1 = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime time2 = LocalDateTime.of(localDate, LocalTime.MAX);
            //满足订单时间在time1和time2之内，并且status正确的
            Double sum = orderMapper.amountSumByDay(time1,time2, Orders.COMPLETED);
            //当当天的营业额为0时，sum就是null
            sum = sum == null ? 0 : sum;
            amounts.add(sum);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(localDateList,","))
                .turnoverList(StringUtils.join(amounts,","))
                .build();
    }
}
