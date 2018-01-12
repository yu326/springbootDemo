package com.example.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Created by koreyoshi on 2017/12/13.
 */

@Data
public class User {
    @Id
    public int id;
    public String name;
    public int age;
    public String desc;
}
