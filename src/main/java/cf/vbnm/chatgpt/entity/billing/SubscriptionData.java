package cf.vbnm.chatgpt.entity.billing;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * @author Yttrium
 */
public class SubscriptionData {

    /**
     * 赠送金额：美元
     */
    @JsonProperty("hard_limit_usd")
    private BigDecimal hardLimitUsd;

    public BigDecimal getHardLimitUsd() {
        return hardLimitUsd;
    }

    public void setHardLimitUsd(BigDecimal hardLimitUsd) {
        this.hardLimitUsd = hardLimitUsd;
    }
}
