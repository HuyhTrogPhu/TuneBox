package org.example.library.service.implement;

import lombok.AllArgsConstructor;
import org.example.library.dto.CategoryDto;
import org.example.library.dto.InstrumentDto;
import org.example.library.mapper.InstrumentMapper;
import org.example.library.model.Brand;
import org.example.library.model.CategoryIns;
import org.example.library.model.Instrument;
import org.example.library.model.InstrumentImage;
import org.example.library.repository.BrandRepository;
import org.example.library.repository.CategoryInsRepository;
import org.example.library.repository.InstrumentImageRepository;
import org.example.library.repository.InstrumentRepository;
import org.example.library.service.InstrumentService;
import org.example.library.utils.ImageUploadInstrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InstrumentServiceImpl implements InstrumentService {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private InstrumentImageRepository instrumentImageRepository;

    @Autowired
    private CategoryInsRepository categoryInsRepository;

    @Autowired
    private BrandRepository brandRepository;

    private final ImageUploadInstrument imageUploadInstrument;

    @Override
    public InstrumentDto createInstrument(InstrumentDto instrumentDto, MultipartFile[] images) {
        try {
            // Khởi tạo danh sách hình ảnh nếu nó là null
            if (instrumentDto.getImage() == null) {
                instrumentDto.setImage(new ArrayList<>());
            }

            Instrument instrument = InstrumentMapper.mapperInstrument(instrumentDto);

            // Thiết lập danh mục và thương hiệu
            instrument.setCategoryIns(getManagedCategory(instrumentDto.getCategoryIns().getId()));
            instrument.setBrand(getManagedBrand(instrumentDto.getBrand().getId()));
            instrument.setStatus(true);

            // Lưu nhạc cụ vào cơ sở dữ liệu trước để có thể liên kết ảnh
            Instrument savedInstrument = instrumentRepository.save(instrument);

            // Nếu có danh sách ảnh được tải lên
            if (images != null && images.length > 0) {
                List<InstrumentImage> imageList = new ArrayList<>();
                for (MultipartFile image : images) {
                    // Kiểm tra xem tệp đã tồn tại chưa
                    if (!imageUploadInstrument.checkExist(image)) {
                        InstrumentImage instrumentImage = new InstrumentImage();
                        String filePath = imageUploadInstrument.uploadFile(image); // Upload và lấy đường dẫn ảnh
                        instrumentImage.setImagePath(filePath); // Lưu đường dẫn ảnh
                        instrumentImage.setInstrument(savedInstrument); // Liên kết ảnh với nhạc cụ đã lưu
                        imageList.add(instrumentImage);
                    } else {
                        System.out.println("File already exists: " + image.getOriginalFilename());
                    }
                }
                // Lưu danh sách ảnh vào cơ sở dữ liệu
                if (!imageList.isEmpty()) {
                    instrumentImageRepository.saveAll(imageList);
                    savedInstrument.setImages(imageList);
                }
            }

            return InstrumentMapper.mapperInstrumentDto(savedInstrument);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public List<InstrumentDto> getAllInstrument() {
        List<Instrument> instruments = instrumentRepository.getAllInstrument();
        return instruments.stream().map(InstrumentMapper::mapperInstrumentDto).collect(Collectors.toList());
    }


    @Override
    public InstrumentDto getInstrumentById(Long id) {
        try {
            Instrument instrument = instrumentRepository.getInstrument(id);
            if (instrument != null) {
                return InstrumentMapper.mapperInstrumentDto(instrument);
            } else {
                // Xử lý trường hợp không tìm thấy instrument
                return null; // Hoặc ném một exception cụ thể
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Có thể xử lý riêng cho từng loại exception nếu cần
        }
    }


    @Override
    public InstrumentDto updateInstrument(Long id, InstrumentDto instrumentDto, MultipartFile[] images) {
        try {
            Instrument instrument = instrumentRepository.getInstrument(id);

            // Khởi tạo danh sách hình ảnh nếu nó là null
            if (instrumentDto.getImage() == null) {
                instrumentDto.setImage(new ArrayList<>());
            }

            // Cập nhật các thuộc tính của nhạc cụ
            instrument.setName(instrumentDto.getName());
            instrument.setCostPrice(instrumentDto.getCostPrice());
            instrument.setQuantity(instrumentDto.getQuantity());
            instrument.setColor(instrumentDto.getColor());
            instrument.setDescription(instrumentDto.getDescription());
            instrument.setBrand(instrumentDto.getBrand());
            instrument.setCategoryIns(instrumentDto.getCategoryIns());
            instrument.setStatus(instrumentDto.isStatus());

            // Xóa các ảnh cũ trong cơ sở dữ liệu
            instrumentImageRepository.deleteAll(instrument.getImages());
            instrument.getImages().clear();

            // Thêm danh sách ảnh mới
            if (images != null && images.length > 0) {
                List<InstrumentImage> newImages = new ArrayList<>();
                for (MultipartFile image : images) {
                    InstrumentImage instrumentImage = new InstrumentImage();
                    String filePath = imageUploadInstrument.uploadFile(image); // Upload và lấy đường dẫn ảnh
                    instrumentImage.setImagePath(filePath); // Lưu đường dẫn ảnh
                    instrumentImage.setInstrument(instrument); // Liên kết ảnh với nhạc cụ
                    newImages.add(instrumentImage);
                }
                // Lưu danh sách ảnh vào cơ sở dữ liệu
                instrumentImageRepository.saveAll(newImages);
                instrument.setImages(newImages); // Gán danh sách ảnh mới vào nhạc cụ
            }

            // Lưu cập nhật vào cơ sở dữ liệu
            Instrument updatedInstrument = instrumentRepository.save(instrument);
            return InstrumentMapper.mapperInstrumentDto(updatedInstrument);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

    public CategoryIns getManagedCategory(Long id) {
        return categoryInsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    public Brand getManagedBrand(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Brand not found"));
    }


}
