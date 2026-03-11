
package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("business_staff_navigation")
public class BusinessStaffNavigationEntity extends BaseEntity {
    private Integer businessPk;
    private Integer locationPk;
    private Long navigationPk;
}
