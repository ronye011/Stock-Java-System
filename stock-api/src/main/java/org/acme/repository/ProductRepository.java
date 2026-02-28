package org.acme.repository;

import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.entity.Product;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    public Product findByCode(String code) {
    	if (code == null) {
    		return null;
    	}
        return find("code", code).firstResult();
    }

    public boolean existsByCode(String code) {
    	if (code == null) {
    		return true;
    	}
        return count("code", code) > 0;
    }
}