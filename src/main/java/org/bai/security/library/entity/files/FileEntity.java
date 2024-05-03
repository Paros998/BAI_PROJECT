package org.bai.security.library.entity.files;

import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "files")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            nullable = false,
            updatable = false
    )
    private UUID fileId;

    private String fileName;

    private String extension;

    @Lob
    private byte[] content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileEntity that = (FileEntity) o;
        return Objects.equals(fileId, that.fileId)
                && Objects.equals(fileName, that.fileName)
                && Objects.equals(extension, that.extension)
                && Arrays.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(fileId, fileName, extension);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}