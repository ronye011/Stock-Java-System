package org.acme.dto;

import java.math.BigDecimal;
import java.util.List;
import org.acme.entity.Product;

public class ProductDTO {
    public Long id;
    public String code;
    public String name;
    public BigDecimal price;
    public List<ProductHasRawMaterialDTO> rawsMaterials;

    public ProductDTO(Product product) {
        this.id = product.id;
        this.code = product.code;
        this.name = product.name;
        this.price = product.price;

        if (product.raws_materials != null) {
            this.rawsMaterials = product.raws_materials.stream()
                .map(ProductHasRawMaterialDTO::new)
                .toList();
        }
    }
}