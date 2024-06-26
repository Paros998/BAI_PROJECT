package org.bai.security.library.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.NonNull;
import org.bai.security.library.domain.files.FileRepository;
import org.bai.security.library.rest.helper.FileService;
import org.bai.security.library.rest.helper.FilesHelper;
import org.bai.security.library.security.permission.checker.DisablePermissionChecking;
import org.bai.security.library.security.permission.checker.PermissionChecker;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Path("files")
@MultipartConfig(
        fileSizeThreshold=1024*1024*10,
        maxFileSize=1024*1024*20,
        maxRequestSize=1024*1024*50)
public class FilesController {
    private final FileRepository fileRepository;
    private final FilesHelper filesHelper;
    private final PermissionChecker userPermissionChecker;

    @Inject
    public FilesController(final FileRepository fileRepository,
                           final @FileService FilesHelper filesHelper,
                           final PermissionChecker userPermissionChecker) {
        this.fileRepository = fileRepository;
        this.filesHelper = filesHelper;
        this.userPermissionChecker = userPermissionChecker;
    }

    @GET
    @Path("/{fileId}")
    @Produces(value = MediaType.APPLICATION_OCTET_STREAM)
    @DisablePermissionChecking
    public Response downloadFile(final @NonNull @PathParam("fileId") UUID fileId) {
        userPermissionChecker.check();
        final var file = fileRepository.getFile(fileId);
        return Response.ok(file.getContent(), MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition",
                        "attachment; filename=\"" + file.getFileName() + "." + file.getExtension() + "\"")
                .build();
    }

    @POST
    @Consumes(value = MediaType.MULTIPART_FORM_DATA)
    @Produces(value = MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "USER"})
    public UUID uploadFile(final List<FormDataBodyPart> parts) {
        userPermissionChecker.check();
        final File file = filesHelper.savePartsToTempFile(parts);
        return fileRepository.saveFile(file);
    }
}