package org.acme.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "product_has_raw_material")
public class ProductHasRawMaterial extends PanacheEntity {
	@ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
	@JsonIgnore
    public Product product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "raw_material_id")
    @JsonIgnore
    public RawMaterial raw_material;

    @Column(nullable = false)
    public Integer quantity_required;
}
