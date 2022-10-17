package kh.farrukh.espiel_spring_cloud_aws_s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final S3Repository s3Repository;
    private final ImageRepository imageRepository;

    public static final String IMAGES_DIRECTORY = "images";

    @Override
    public Image uploadImage(MultipartFile image) throws IOException {
        String name = FileUtils.getUniqueImageName(image);
        String url = s3Repository.savePublicReadObject(image.getInputStream(), IMAGES_DIRECTORY + "/" + name);
        Image imageEntity = new Image(name, url, image.getSize() / 1024f / 1024f);
        return imageRepository.save(imageEntity);
    }

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public Image findById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));
    }

    @Override
    public void deleteImage(Long id) {
        if (!imageRepository.existsById(id)) throw new RuntimeException("Image not found");
        s3Repository.deleteObject(IMAGES_DIRECTORY + "/" + findById(id).getName());
        imageRepository.deleteById(id);
    }
}
