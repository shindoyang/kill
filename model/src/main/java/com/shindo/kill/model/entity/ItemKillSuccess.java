package com.shindo.kill.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * item_kill_success
 * @author 
 */
public class ItemKillSuccess implements Serializable {
    /**
     * 秒杀成功生成的订单编号
     */
    private String code;

    /**
     * 商品id
     */
    private Integer itemId;

    /**
     * 秒杀id
     */
    private Integer killId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 秒杀结果: -1无效  0成功(未付款)  1已付款  2已取消
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getKillId() {
        return killId;
    }

    public void setKillId(Integer killId) {
        this.killId = killId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}