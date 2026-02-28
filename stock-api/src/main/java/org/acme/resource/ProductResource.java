package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import org.acme.dto.ProductDTO;
import org.acme.entity.Product;
import org.acme.service.ProductService;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService service;

    @POST
    public Response create(Product product) {
    	try {
            Product created = service.create(product);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (RuntimeException e) {
        	return Response.status(Response.Status.OK).entity(e.getMessage()).build();
        }
    }

    @GET
    public List<ProductDTO> list() {
        return service.findAll().stream()
                .map(ProductDTO::new)
                .toList();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        Product product = service.findById(id);
        
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Product with ID " + id + " not found")
                           .build();
        }
        
        return Response.ok(new ProductDTO(product)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Product product) {
        try {
            Product updated = service.update(id, product);
            return Response.ok(new ProductDTO(updated)).build();
        }  catch (RuntimeException e) {
        	return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        service.delete(id);
    }
}