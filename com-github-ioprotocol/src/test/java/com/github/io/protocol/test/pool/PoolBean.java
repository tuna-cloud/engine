package com.github.io.protocol.test.pool;

public class PoolBean {
    private Integer age;
    private Integer old;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getOld() {
        return old;
    }

    public void setOld(Integer old) {
        this.old = old;
    }

    @Override
    public String toString() {
        return "PoolBean{" +
                "age=" + age +
                ", old=" + old +
                '}';
    }
}
