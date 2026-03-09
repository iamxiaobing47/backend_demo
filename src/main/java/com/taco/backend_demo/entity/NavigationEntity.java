package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("navigations")
public class NavigationEntity extends BaseEntity {
    private Long navigationId;
    private String name;
    private String path;
    private String icon;
    private Integer sortOrder;
    private Long parentId;
    private Boolean enabled;
    private String userType; // 用户类型限制：business_owner, employee
    private String businessOwnerRestrictions; // 逗号分隔的business_owner IDs，为空表示不限制
    private String locationRestrictions; // 逗号分隔的location IDs，为空表示不限制
}