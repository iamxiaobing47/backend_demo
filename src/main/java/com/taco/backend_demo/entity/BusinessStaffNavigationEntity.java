
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
@TableName("business_staff_navigation")
public class BusinessStaffNavigationEntity extends BaseEntity {
    private Integer businessPk;
    private Integer locationPk;
    private Long navigationPk;
}
