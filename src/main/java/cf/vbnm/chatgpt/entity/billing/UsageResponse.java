package cf.vbnm.chatgpt.entity.billing;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * 余额查询接口返回值
 *
 * @author plexpt
 * @author Yttrium
 */
public class UsageResponse {

    /**
     * 总使用金额：美元
     */
    @JsonProperty("total_usage")
    private BigDecimal totalUsage;

    public BigDecimal getTotalUsage() {
        return totalUsage;
    }

    public void setTotalUsage(BigDecimal totalUsage) {
        this.totalUsage = totalUsage;
    }
}
