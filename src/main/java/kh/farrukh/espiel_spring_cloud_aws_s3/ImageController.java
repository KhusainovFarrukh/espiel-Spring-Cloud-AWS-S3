package kh.farrukh.espiel_spring_cloud_aws_s3;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public Image uploadImage(@RequestParam("image") MultipartFile image) throws IOException {
        return imageService.uploadImage(image);
    }

    @GetMapping
    public List<Image> getAllImages() {
        return imageService.getAllImages();
    }

    @GetMapping("{id}")
    public Image getImage(@PathVariable("id") Long id) {
        return imageService.findById(id);
    }

    @DeleteMapping("{id}")
    public void deleteImage(@PathVariable("id") Long id) {
        imageService.deleteImage(id);
    }
}
