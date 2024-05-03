package org.bai.security.library.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.NonNull;
import org.bai.security.library.domain.files.FileRepository;

import java.util.UUID;

@Path("files")
public class FilesController {
    private final FileRepository fileRepository;

    @Inject
    public FilesController(final FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @GET
    @Path("/{fileId}")
    @Produces(value = MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(final @NonNull @PathParam("fileId") UUID fileId) {
        final var file = fileRepository.getFile(fileId);
        return Response.ok(file.getContent(), MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition",
                        "attachment; filename=\"" + file.getFileName() + "." + file.getExtension() + "\"")
                .build();
    }
}