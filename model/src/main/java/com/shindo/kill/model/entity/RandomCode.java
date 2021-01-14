package com.shindo.kill.model.entity;

import java.io.Serializable;

/**
 * random_code
 * @author 
 */
public class RandomCode implements Serializable {
    private Integer id;

    private String code;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}