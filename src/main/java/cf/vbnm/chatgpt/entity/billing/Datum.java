package cf.vbnm.chatgpt.entity.billing;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * @author plexpt
 * @author Yttrium
 */
public class Datum {
    private String object;
    private String id;
    /**
     * 赠送金额：美元
     */
    @JsonProperty("grant_amount")
    private BigDecimal grantAmount;
    /**
     * 使用金额：美元
     */
    @JsonProperty("used_amount")
    private BigDecimal usedAmount;
    /**
     * 生效时间戳
     */
    @JsonProperty("effective_at")
    private Long effectiveAt;
    /**
     * 过期时间戳
     */
    @JsonProperty("expires_at")
    private Long expiresAt;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getGrantAmount() {
        return grantAmount;
    }

    public void setGrantAmount(BigDecimal grantAmount) {
        this.grantAmount = grantAmount;
    }

    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }

    public Long getEffectiveAt() {
        return effectiveAt;
    }

    public void setEffectiveAt(Long effectiveAt) {
        this.effectiveAt = effectiveAt;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }
}
