package org.acme.dto;

public class ProductHasRawMaterialDTO {
    public Long id;
    public Long productId;
    public String productName;
    public Long rawMaterialId;
    public String rawMaterialName;
    public Integer quantityRequired;

    public ProductHasRawMaterialDTO(org.acme.entity.ProductHasRawMaterial entity) {
        this.id = entity.id;
        this.productId = entity.product.id;
        this.productName = entity.product.name;
        this.rawMaterialId = entity.raw_material.id;
        this.rawMaterialName = entity.raw_material.name;
        this.quantityRequired = entity.quantity_required;
    }
}