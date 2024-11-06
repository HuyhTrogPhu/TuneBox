package org.example.library.service.implement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.example.library.dto.InstrumentAccordingTo;
import org.example.library.dto.InstrumentDto;
import org.example.library.dto.InstrumentSalesDto;
import org.example.library.dto.StatisticalInstrumentDto;
import org.example.library.mapper.InstrumentMapper;
import org.example.library.model.Brand;
import org.example.library.model.CategoryIns;
import org.example.library.model.Instrument;
import org.example.library.repository.BrandRepository;
import org.example.library.repository.CategoryInsRepository;
import org.example.library.repository.InstrumentRepository;
import org.example.library.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private Cloudinary cloudinary;


    @Override
    public InstrumentDto createInstrument(InstrumentDto instrumentDto, MultipartFile image) {
        try {
//            Instrument instrument = InstrumentMapper.mapperInstrument(instrumentDto);
//            if (image == null){
//                instrument.setImage(null);
//            }else {
//                imageUploadInstrument.uploadFile(image);
//                instrument.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
//            }
            Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");
            instrumentDto.setImage(imageUrl);

            Instrument instrument = new Instrument();
            instrument.setCategoryIns(getManagedCategory(instrumentDto.getCategoryIns().getId()));
            instrument.setBrand(getManagedBrand(instrumentDto.getBrand().getId()));
            instrument.setStatus(false);
            instrument.setImage(imageUrl);
            instrument.setDescription(instrumentDto.getDescription());
            instrument.setName(instrumentDto.getName());
            instrument.setCostPrice(instrumentDto.getCostPrice());
            instrument.setQuantity(instrumentDto.getQuantity());
            instrument.setColor(instrumentDto.getColor());
            instrumentRepository.save(instrument);
            return instrumentDto;
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
    public InstrumentDto updateInstrument(Long id, InstrumentDto instrumentDto, MultipartFile image) {
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
            if (image != null && !image.isEmpty()) {
//                // Nếu có hình ảnh mới, upload và lưu lại ảnh
//                boolean isUploaded = imageUploadInstrument.uploadFile(image);
//                if (isUploaded) {
//                    // Lưu ảnh dưới dạng Base64 trong cơ sở dữ liệu
//                    instrument.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
//                } else {
//                    throw new RuntimeException("Failed to upload the image");
//                }
                if (instrument.getImage() != null) {
                    String publicId = extractPublicIdFromUrl(instrument.getImage());
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                }
                Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = (String) uploadResult.get("url");
                instrument.setImage(imageUrl);
            }

            Instrument saveInstrument = instrumentRepository.save(instrument);
            return InstrumentMapper.mapperInstrumentDto(saveInstrument);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Có thể trả về một đối tượng phản hồi có lỗi
        }
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        String[] parts = imageUrl.split("/");
        String publicIdWithExtension = parts[parts.length - 1];
        return publicIdWithExtension.split("\\.")[0];  // Lấy publicId trước phần mở rộng ảnh (ví dụ: .jpg)
    }


    @Override
    public void deleteInstrument(Long id) {
        Instrument instrument = instrumentRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Instrument not found")
        );
        instrument.setStatus(false);
        instrumentRepository.save(instrument);
    }

    public CategoryIns getManagedCategory(Long id) {
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

    @Override
    public List<InstrumentDto> getInstrumentByCategoryId(Long categoryId) {
        List<Instrument> instruments = instrumentRepository.findByCategoryInsId(categoryId);
        return instruments.stream().map(InstrumentMapper::mapperInstrumentDto).collect(Collectors.toList());
    }

    @Override
    public List<StatisticalInstrumentDto> getIdAndNameInstrument() {
        return instrumentRepository.getStatisticalInstruments();
    }

    @Override
    public List<InstrumentDto> getInstrumentByCategoryIdAndBrandId(Long categoryId, Long brandId) {
        List<Instrument> instruments = instrumentRepository.getInstrumentByCategoryIdAndBrandId(categoryId, brandId);
        return instruments.stream().map(InstrumentMapper::mapperInstrumentDto).collect(Collectors.toList());
    }

    @Override
    public List<InstrumentSalesDto> instrumentSalesTheMostOfDay() {
        try {
            return instrumentRepository.getInstrumentSalesTheMostOfDay();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<InstrumentSalesDto> instrumentSalesTheMostOfWeek() {
        try {
            return instrumentRepository.getInstrumentSalesTheMostOfWeek();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<InstrumentSalesDto> instrumentSalesTheMostOfMonth() {
        try {
            return instrumentRepository.getInstrumentSalesTheMostOfMonth();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<InstrumentSalesDto> instrumentSalesTheLeastOfDay() {
        try {
            return instrumentRepository.getInstrumentSalesTheLeastOfDay();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<InstrumentSalesDto> instrumentSalesTheLeastOfWeek() {
        try {
            return instrumentRepository.getInstrumentSalesTheLeastOfWeek();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<InstrumentSalesDto> instrumentSalesTheLeastOfMonth() {
        try {
            return instrumentRepository.getInstrumentSalesTheLeastOfMonth();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Double getRevenueInstrumentOfDay(Long instrumentId) {
        try {
            return instrumentRepository.getTotalRevenueInstrumentOfDay(instrumentId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    @Override
    public Double getRevenueInstrumentOfWeek(Long instrumentId) {
        try {
            return instrumentRepository.getTotalRevenueInstrumentOfWeek(instrumentId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    @Override
    public Double getRevenueInstrumentOfMonth(Long instrumentId) {
        try {
            return instrumentRepository.getTotalRevenueInstrumentOfMonth(instrumentId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }

    }

    @Override
    public Double getRevenueInstrumentOfYear(Long instrumentId) {
        try {
            return instrumentRepository.getTotalRevenueInstrumentOfYear(instrumentId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }

    }

    @Override
    public List<InstrumentAccordingTo> getListInstrumentByDay(LocalDate date) {
        return instrumentRepository.getInstrumentAccordingToDay(date);
    }

    @Override
    public List<InstrumentAccordingTo> getListInstrumentBetween(LocalDate startDate, LocalDate endDate) {
        return instrumentRepository.getInstrumentBetween(startDate, endDate);
    }

    @Override
    public List<InstrumentAccordingTo> getListInstrumentByWeek(LocalDate date) {
        return instrumentRepository.getInstrumentByWeek(date);
    }

    @Override
    public List<InstrumentAccordingTo> getListInstrumentBetweenWeek(LocalDate startDate, LocalDate endDate) {
        return instrumentRepository.getInstrumentBetweenWeek(startDate, endDate);
    }

    @Override
    public List<InstrumentAccordingTo> getListInstrumentByMonth(int year, int month) {
        return instrumentRepository.getInstrumentsByMonth(year, month);
    }

    @Override
    public List<InstrumentAccordingTo> getListInstrumentBetweenMonth(int year, int monthStart, int monthEnd) {
        return instrumentRepository.getInstrumentsBetweenMonths(year, monthStart, monthEnd);
    }

    @Override
    public List<InstrumentAccordingTo> getListInstrumentByYear(int year) {
        return instrumentRepository.getInstrumentByYear(year);
    }

    @Override
    public List<InstrumentAccordingTo> getListInstrumentBetweenYear(int yearStart, int yearEnd) {
        return instrumentRepository.getInstrumentBetweenYears(yearStart, yearEnd);
    }


}
