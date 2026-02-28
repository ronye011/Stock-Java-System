package org.acme.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.entity.Product;
import org.acme.entity.RawMaterial;
import org.acme.repository.ProductRepository;
import org.acme.repository.RawMaterialRepository;
import org.acme.entity.ProductHasRawMaterial;
import org.acme.repository.ProductHasRawMaterialRepository;

@ApplicationScoped
public class ProductHasRawMaterialService {

    @Inject
    ProductHasRawMaterialRepository repository;

    @Inject
	ProductRepository productRepository;
    
    @Inject
    RawMaterialRepository rawMaterialRepository;
    
    @Transactional
    public ProductHasRawMaterial create(Product product, RawMaterial rawMaterial, Integer quantityRequired) {
        if (product == null || rawMaterial == null || quantityRequired == null) {
            throw new RuntimeException("Request invalid: Product or Raw Material not found");
        }

        if (quantityRequired <= 0) {
            throw new RuntimeException("The quantity must be greater than 0");
        }

        if (repository.findByProductAndRawMaterial(product, rawMaterial).isPresent()) {
            throw new RuntimeException("Association already exists");
        }

        ProductHasRawMaterial relation = new ProductHasRawMaterial();
        relation.product = product;
        relation.raw_material = rawMaterial;
        relation.quantity_required = quantityRequired;

        repository.persist(relation);

        return relation;
    }

    @Transactional
    public ProductHasRawMaterial update(Long id, Integer quantityRequired) {
    	if (id == null || quantityRequired == null) {
    		throw new RuntimeException("ID or Quantity Required is null");
    	}

    	if (quantityRequired <= 0) {
    		throw new RuntimeException("The quantity must be greater than 0");
    	}
    	
        ProductHasRawMaterial relation = repository.findById(id);

        if (relation == null) {
            throw new RuntimeException("Association not found");
        }

        relation.quantity_required = quantityRequired;

        return relation;
    }

    @Transactional
    public void delete(Long id) {
    	if (id == null) {
    		throw new RuntimeException("ID is null");
    	}

        ProductHasRawMaterial relation = repository.findById(id);

        if (relation == null) {
            throw new RuntimeException("Association not found");
        }

        repository.delete(relation);
    }

    public List<ProductHasRawMaterial> listAll() {
        return repository.listAll();
    }

    public List<ProductHasRawMaterial> listByProduct(Product product) {
        return repository.findByProduct(product);
    }

    public List<ProductHasRawMaterial> listByRawMaterial(RawMaterial rawMaterial) {
        return repository.findByRawMaterial(rawMaterial);
    }

    public ProductHasRawMaterial findById(Long id) {
    	if (id == null) {
    		throw new RuntimeException("ID is null");
    	}
    	
        ProductHasRawMaterial relation = repository.findById(id);

        if (relation == null) {
            throw new RuntimeException("Association not found");
        }

        return relation;
    }
}