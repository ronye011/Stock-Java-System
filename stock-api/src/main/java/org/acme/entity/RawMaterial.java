package org.acme.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "raw_material")
public class RawMaterial extends PanacheEntity {
    
    @Column(nullable = false, unique = true)
    public String code;

    @Column(nullable = false)
    public String name;

    @Column(name = "quantityStock", nullable = false)
    public Integer quantityStock;

    @OneToMany(mappedBy = "raw_material")
    @JsonIgnore
    public List<ProductHasRawMaterial> products;
}