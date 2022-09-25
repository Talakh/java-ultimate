package org.course;

import lombok.ToString;
import org.course.annotations.Column;
import org.course.annotations.Table;

import java.math.BigDecimal;
import java.sql.Date;

@ToString
@Table("products")
public class Product {
    private Integer id;
    private String name;
    private BigDecimal price;
    @Column("created_at")
    private Date createdAt;
}
