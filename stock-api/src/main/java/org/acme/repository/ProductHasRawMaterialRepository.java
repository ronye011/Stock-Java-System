package org.acme.repository;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import org.acme.entity.Product;
import org.acme.entity.RawMaterial;
import org.acme.entity.ProductHasRawMaterial;

@ApplicationScoped
public class ProductHasRawMaterialRepository implements PanacheRepository<ProductHasRawMaterial> {

    public List<ProductHasRawMaterial> findByProduct(Product product) {
        return list("product", product);
    }

    public List<ProductHasRawMaterial> findByRawMaterial(RawMaterial rawMaterial) {
        return list("raw_material", rawMaterial);
    }

    public Optional<ProductHasRawMaterial> findByProductAndRawMaterial(Product product, RawMaterial rawMaterial) {
        return find("product = ?1 and raw_material = ?2", product, rawMaterial)
                .firstResultOptional();
    }

    public void deleteByProduct(Product product) {
        delete("product", product);
    }

    public void deleteByRawMaterial(RawMaterial rawMaterial) {
        delete("raw_material", rawMaterial);
    }
}