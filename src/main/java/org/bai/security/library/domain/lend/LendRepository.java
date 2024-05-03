package org.bai.security.library.domain.lend;

import java.util.UUID;

public interface LendRepository {
    UUID lendBook(UUID userId, UUID bookId);
}
