package com.zdjf.qqh.data.entity;

/**
 * 统计实体
 */
public class StatisticsBean extends BaseBean {
    /**
     * 产品统计id（用于统计时长）
     */
    private String statisticsDetailId;

    public String getStatisticsDetailId() {
        return statisticsDetailId;
    }

    public void setStatisticsDetailId(String statisticsDetailId) {
        this.statisticsDetailId = statisticsDetailId;
    }
}
