package org.bai.security.library.domain.lend;

import org.bai.security.library.api.lends.LendDto;

import java.util.List;
import java.util.UUID;

public interface LendRepository {
    UUID lendBook(UUID userId, UUID bookId);

    List<LendDto> findAllByUser(UUID userId);
}
