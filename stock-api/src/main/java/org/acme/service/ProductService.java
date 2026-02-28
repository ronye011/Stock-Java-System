package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import org.acme.entity.Product;
import org.acme.repository.ProductRepository;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository repository;

    @Transactional
    public Product create(Product product) {
    	if (product == null || product.code == null || product.name == null || product.price == null) {
    		throw new RuntimeException("Resquest invalid");
    	}
    	
        if (repository.existsByCode(product.code)) {
            throw new RuntimeException("Product code already exists");
        }
        repository.persist(product);
        return product;
    }

    public List<Product> findAll() {
        return repository.listAll();
    }

    @Transactional
    public Product findById(Long id) {
        Product product = repository.findById(id);
        
        if (product != null) {
            if (product.raws_materials != null) {
                product.raws_materials.size(); 
            }
        }
        
        return product;
    }

    @Transactional
    public Product update(Long id, Product updated) {
        if (id == null) {
            throw new RuntimeException("ID is null");
        }

        Product entity = repository.findById(id);
        if (entity == null) {
            throw new RuntimeException("Product not found");
        }
        if (updated.code != null) {
	        if (repository.existsByCode(updated.code)) {
	            throw new RuntimeException("Product code already exists");
	        }
        }

        if (updated.name != null) { entity.name = updated.name; }
        if (updated.code != null) { entity.code = updated.code; }
        if (updated.price != null) { entity.price = updated.price; }

        if (entity.raws_materials != null) {
            entity.raws_materials.size(); 
        }

        return entity;
    }
    
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}