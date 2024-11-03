package org.example.library.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OtherAttachmentDto {
    private Long id;
    private String fileName;
    private String fileUrl;
    private String mimeType;
    private String contentType;
    private long size;
}
