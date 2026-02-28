package org.acme.resource;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.acme.dto.ProductHasRawMaterialDTO;
import org.acme.entity.Product;
import org.acme.entity.RawMaterial;
import org.acme.entity.ProductHasRawMaterial;
import org.acme.service.ProductHasRawMaterialService;

import io.smallrye.common.constraint.NotNull;

import org.acme.repository.ProductRepository;
import org.acme.repository.RawMaterialRepository;

@Path("/product-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductHasRawMaterialResource {

    @Inject
    ProductHasRawMaterialService service;

    @Inject
    ProductRepository productRepository;

    @Inject
    RawMaterialRepository rawMaterialRepository;

    @POST
    public Response create(ProductRawMaterialRequest request) {
        try {
        	var product = productRepository.findById(request.productId);
            var rawMaterial = rawMaterialRepository.findById(request.rawMaterialId);

            if (product == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Product not found").build();
            }
            if (rawMaterial == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Raw Material not found").build();
            }
            
            var relation = service.create(product, rawMaterial, request.quantityRequired);
            return Response.status(Response.Status.CREATED)
                           .entity(new ProductHasRawMaterialDTO(relation))
                           .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
        	return Response.status(Response.Status.BAD_REQUEST).entity("").build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, ProductRawMaterialRequest request) {
        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Request body is empty")
                           .build();
        }
        
        if (request.quantityRequired == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("quantityRequired is missing")
                           .build();
        }

        try {
            var relation = service.update(id, request.quantityRequired);
            return Response.ok(new ProductHasRawMaterialDTO(relation)).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {

        service.delete(id);
        return Response.noContent().build();
    }

    @GET
    public List<ProductHasRawMaterialDTO> listAll() {
        return service.listAll().stream()
                .map(ProductHasRawMaterialDTO::new)
                .toList();
    }

    @GET
    @Path("/{id}")
    public ProductHasRawMaterialDTO findById(@PathParam("id") Long id) {
        var relation = service.findById(id);
        return new ProductHasRawMaterialDTO(relation);
    }

    public static class ProductRawMaterialRequest {

        @NotNull
        public Long productId;

        @NotNull
        public Long rawMaterialId;

        @NotNull
        public Integer quantityRequired;
    }
}