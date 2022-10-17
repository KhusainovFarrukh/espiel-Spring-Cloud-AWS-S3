package kh.farrukh.espiel_spring_cloud_aws_s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final AmazonS3Client s3Client;
    private final ImageRepository imageRepository;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public static final String IMAGES_DIRECTORY = "images";

    @Override
    public Image uploadImage(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();
        String nameWithoutExtension = FilenameUtils.removeExtension(originalFilename);
        if (nameWithoutExtension == null) nameWithoutExtension = "image";
        nameWithoutExtension = nameWithoutExtension.replace(" ", "_");
        String name = nameWithoutExtension + "-" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(originalFilename);
        String key = IMAGES_DIRECTORY + "/" + name;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, image.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead);
        s3Client.putObject(putObjectRequest);
        String url = s3Client.getUrl(bucketName, key).toString();

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
        s3Client.deleteObject(bucketName, IMAGES_DIRECTORY + "/" + findById(id).getName());
        imageRepository.deleteById(id);
    }
}
