package kh.farrukh.espiel_spring_cloud_aws_s3;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    Image uploadImage(MultipartFile image) throws IOException;

    List<Image> getAllImages();

    Image findById(Long id);

    void deleteImage(Long id);

}
