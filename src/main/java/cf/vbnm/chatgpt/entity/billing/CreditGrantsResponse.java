package cf.vbnm.chatgpt.entity.billing;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * 余额查询接口返回值
 *
 * @author plexpt
 * @author Yttrium
 */
public class CreditGrantsResponse {
    private String object;
    /**
     * 总金额：美元
     */
    @JsonProperty("total_granted")
    private BigDecimal totalGranted;
    /**
     * 总使用金额：美元
     */
    @JsonProperty("total_used")
    private BigDecimal totalUsed;
    /**
     * 总剩余金额：美元
     */
    @JsonProperty("total_available")
    private BigDecimal totalAvailable;
    /**
     * 余额明细
     */
    private Grants grants;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public BigDecimal getTotalGranted() {
        return totalGranted;
    }

    public void setTotalGranted(BigDecimal totalGranted) {
        this.totalGranted = totalGranted;
    }

    public BigDecimal getTotalUsed() {
        return totalUsed;
    }

    public void setTotalUsed(BigDecimal totalUsed) {
        this.totalUsed = totalUsed;
    }

    public BigDecimal getTotalAvailable() {
        return totalAvailable;
    }

    public void setTotalAvailable(BigDecimal totalAvailable) {
        this.totalAvailable = totalAvailable;
    }

    public Grants getGrants() {
        return grants;
    }

    public void setGrants(Grants grants) {
        this.grants = grants;
    }
}
