package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("navigation")
public class NavigationEntity extends BaseEntity {
    private String chineseName;
    private String englishName;
    private String path;
    private String icon;
    private Integer sortOrder;
    private Long parentId;
}
