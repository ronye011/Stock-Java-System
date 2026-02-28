package org.acme.resource;

import java.net.URI;
import java.util.List;

import org.acme.entity.RawMaterial;
import org.acme.service.RawMaterialService;

import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RawMaterialResource {

    @Inject
    RawMaterialService service;

    @GET
    public List<RawMaterial> listAll() {
        return service.listAll();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        RawMaterial rawMaterial = service.findById(id);

        if (rawMaterial == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(rawMaterial).build();
    }

    @POST
    public Response create(RawMaterial rawMaterial) {
        try {
            RawMaterial created = service.create(rawMaterial);
            return Response.created(URI.create("/raw-materials/" + created.id))
                           .entity(created).build();
        } catch (RuntimeException e) {
            // Change OK to BAD_REQUEST or INTERNAL_SERVER_ERROR
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, RawMaterial rawMaterial) {
        try {
            RawMaterial updated = service.update(id, rawMaterial);
            return Response.ok(updated).build();
        }  catch (RuntimeException e) {
        	return Response.status(Response.Status.OK).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}