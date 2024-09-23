package org.example.library.mapper;

import org.example.library.dto.InstrumentDTO;
import org.example.library.model.Instrument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstrumentMapper {

    @Mapping(source = "categoryIns.id", target = "categoryInsId")
    @Mapping(source = "brand.id", target = "brandId")
    InstrumentDTO toDTO(Instrument instrument);

    @Mapping(source = "categoryInsId", target = "categoryIns.id")
    @Mapping(source = "brandId", target = "brand.id")
    Instrument toEntity(InstrumentDTO instrumentDTO);
}
