/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.example.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zero.boot.core.annotation.Comment;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "t_example")
public class Example {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("主键")
    private Long id;

    @Column(name = "username")
    @Comment("用户名")
    @NotBlank
    private String username;

    @Comment("性别")
    private Byte sex;

    @Comment("头像")
    private String avatar;

    @Comment("密码")
    @NotBlank
    private String password;

    @Comment("状态")
    private Byte status;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Comment("创建时间")
    private Timestamp createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Comment("修改时间")
    private Timestamp updateTime;
}
