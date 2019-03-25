package com.hiway.kryo.test;

import java.io.Serializable;

/**
 * @author cheguangai
 * @date 2019/3/25 0025
 */
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
