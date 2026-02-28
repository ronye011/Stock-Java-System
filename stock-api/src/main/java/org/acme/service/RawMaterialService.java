package org.acme.service;

import java.util.List;

import org.acme.entity.RawMaterial;
import org.acme.repository.RawMaterialRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RawMaterialService {

    @Inject
    RawMaterialRepository repository;

    public List<RawMaterial> listAll() {
        return repository.listAll();
    }

    public RawMaterial findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public RawMaterial create(RawMaterial rawMaterial) {
    	if (rawMaterial.name == null || rawMaterial.code == null || rawMaterial.quantityStock == null) {
    	    throw new RuntimeException("Request invalid: Missing required fields.");
    	}
    	
        if (repository.existsByCode(rawMaterial.code)) {
            throw new RuntimeException("Raw material code already exists");
        }

        repository.persist(rawMaterial);
        return rawMaterial;
    }

    @Transactional
    public RawMaterial update(Long id, RawMaterial data) {
        if (id == null) {
            throw new RuntimeException("ID is null");
        }
        
        if (data == null) {
            throw new RuntimeException("Raw material data is null");
        }

        if (data.code == null && data.name == null && data.quantityStock == null) {
            throw new RuntimeException("No data to update");
        }
        
        if (data.code != null) {
	        if (repository.existsByCode(data.code)) {
	            throw new RuntimeException("Raw material code already exists");
	        }
        }

        RawMaterial entity = repository.findById(id);
        if (entity == null) {
            throw new RuntimeException("Raw material not found");
        }
        
        if (entity.products != null) {
            entity.products.size(); 
        }

        if (data.name != null) {
            entity.name = data.name;
        }
        if (data.code != null) {
            entity.code = data.code;
        }
        if (data.quantityStock != null) {
            entity.quantityStock = data.quantityStock;
        }

        return entity;
    }
    
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}