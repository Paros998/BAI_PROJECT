package org.bai.security.library.api.lends;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LendDto implements Serializable {
    private String lendId;
    private String userId;
    private String bookId;
    private String bookStockId;

    private LocalDateTime lentOn;
    private LocalDateTime lentTill;

    private boolean isOverLent;
    private int overLent;
    private boolean isActive;
}