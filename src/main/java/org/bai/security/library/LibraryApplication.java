package org.bai.security.library;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@DeclareRoles({"ADMIN", "USER"})
@ApplicationPath("/api")
public class LibraryApplication extends Application {
}