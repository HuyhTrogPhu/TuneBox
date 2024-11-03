package org.example.library.mapper;

import org.example.library.dto.OtherAttachmentDto;
import org.example.library.model.OtherAttachment;
import java.util.List;
import java.util.stream.Collectors;

public class OtherAttachmentMapper {

    // Chuyển từ EntityOther sang DTOO
    public static OtherAttachmentDto toDto(OtherAttachment otherAttachment) {
        if (otherAttachment == null) {
            return null;
        }
        return new OtherAttachmentDto(
                otherAttachment.getId(),
                otherAttachment.getFileName(),
                otherAttachment.getFileUrl(),
                otherAttachment.getMimeType(),
                otherAttachment.getContentType(),
                otherAttachment.getSize()
        );
    }

    // Chuyển từ DTO sang Entity
    public static OtherAttachment toEntity(OtherAttachmentDto otherAttachmentDto) {
        if (otherAttachmentDto == null) {
            return null;
        }
        OtherAttachment otherAttachment = new OtherAttachment();
        otherAttachment.setId(otherAttachmentDto.getId());
        otherAttachment.setFileName(otherAttachmentDto.getFileName());
        otherAttachment.setFileUrl(otherAttachmentDto.getFileUrl());
        otherAttachment.setMimeType(otherAttachmentDto.getMimeType());
        otherAttachment.setSize(otherAttachmentDto.getSize());
        return otherAttachment;
    }

    // Chuyển danh sách Entity sang danh sách DTO
    public static List<OtherAttachmentDto> toDtoList(List<OtherAttachment> otherAttachments) {
        if (otherAttachments == null) {
            return null;
        }
        return otherAttachments.stream()
                .map(OtherAttachmentMapper::toDto)
                .collect(Collectors.toList());
    }

    // Chuyển danh sách DTO sang danh sách Entity
    public static List<OtherAttachment> toEntityList(List<OtherAttachmentDto> otherAttachmentDtos) {
        if (otherAttachmentDtos == null) {
            return null;
        }
        return otherAttachmentDtos.stream()
                .map(OtherAttachmentMapper::toEntity)
                .collect(Collectors.toList());
    }
}
