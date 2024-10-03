package org.example.library.service.implement;

import lombok.AllArgsConstructor;
import org.example.library.dto.CategoryDto;
import org.example.library.dto.InstrumentDto;
import org.example.library.mapper.InstrumentMapper;
import org.example.library.model.Brand;
import org.example.library.model.CategoryIns;
import org.example.library.model.Instrument;
import org.example.library.repository.BrandRepository;
import org.example.library.repository.CategoryInsRepository;
import org.example.library.repository.InstrumentRepository;
import org.example.library.service.InstrumentService;
import org.example.library.utils.ImageUploadInstrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InstrumentServiceImpl implements InstrumentService {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private CategoryInsRepository categoryInsRepository;

    @Autowired
    private BrandRepository brandRepository;

    private final ImageUploadInstrument imageUploadInstrument;

    @Override
    public InstrumentDto createInstrument(InstrumentDto instrumentDto, MultipartFile[] image) {
        try {
            Instrument instrument = InstrumentMapper.mapperInstrument(instrumentDto);
            if (image == null || image.length == 0) {
                instrument.setImage(null); // Cập nhật nếu không có hình ảnh
            } else {
                String[] base64Images = new String[image.length];
                for (int i = 0; i < image.length; i++) {
                    imageUploadInstrument.uploadFile(image[i]);
                    base64Images[i] = Base64.getEncoder().encodeToString(image[i].getBytes());
                }
                instrument.setImage(List.of(base64Images)); // Lưu dưới dạng List<String>
            }

            instrument.setCategoryIns(getManagedCategory(instrumentDto.getCategoryIns().getId()));
            instrument.setBrand(getManagedBrand(instrumentDto.getBrand().getId()));
            instrument.setStatus(true);

            Instrument saveInstrument = instrumentRepository.save(instrument);
            return InstrumentMapper.mapperInstrumentDto(saveInstrument);


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<InstrumentDto> getAllInstrument() {
        List<Instrument> instruments = instrumentRepository.findAll();
        return instruments.stream().map(InstrumentMapper::mapperInstrumentDto).collect(Collectors.toList());
    }


    @Override
    public InstrumentDto getInstrumentById(Long id) {
        Instrument instrument = instrumentRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Instrument not found")
        );
        return InstrumentMapper.mapperInstrumentDto(instrument);
    }

    @Override
    public InstrumentDto updateInstrument(Long id, InstrumentDto instrumentDto, MultipartFile[] image) {
        try {
            Instrument instrument = instrumentRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("Instrument not found")
            );

            // Cập nhật các trường khác của nhạc cụ
            instrument.setName(instrumentDto.getName());
            instrument.setCostPrice(instrumentDto.getCostPrice());
            instrument.setQuantity(instrumentDto.getQuantity());
            instrument.setColor(instrumentDto.getColor());
            instrument.setDescription(instrumentDto.getDescription());
            instrument.setBrand(instrumentDto.getBrand());
            instrument.setCategoryIns(instrumentDto.getCategoryIns());
            instrument.setStatus(instrumentDto.isStatus());

            // Kiểm tra hình ảnh mới
            if (image != null && image.length > 0) {
                String[] base64Images = new String[image.length];
                for (int i = 0; i < image.length; i++) {
                    boolean isUploaded = imageUploadInstrument.uploadFile(image[i]);
                    if (isUploaded) {
                        base64Images[i] = Base64.getEncoder().encodeToString(image[i].getBytes());
                    } else {
                        throw new RuntimeException("Failed to upload the image");
                    }
                }
                instrument.setImage(List.of(base64Images));
            }



            Instrument saveInstrument = instrumentRepository.save(instrument);
            return InstrumentMapper.mapperInstrumentDto(saveInstrument);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update instrument due to IOException", e);
        }
    }





    @Override
    public void deleteInstrument(Long id) {
        Instrument instrument = instrumentRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Instrument not found")
        );
        instrument.setStatus(false);
        instrumentRepository.save(instrument);
    }

    public CategoryIns getManagedCategory(Long id){
        return categoryInsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    public Brand getManagedBrand(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Brand not found"));
    }

    @Override
    public List<InstrumentDto> getInstrumentsByBrandId(Long brandId) {
        List<Instrument> instruments = instrumentRepository.findByBrandId(brandId);
        return instruments.stream().map(InstrumentMapper::mapperInstrumentDto).collect(Collectors.toList());
    }
}
