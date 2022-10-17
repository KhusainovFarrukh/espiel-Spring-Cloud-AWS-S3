package kh.farrukh.espiel_spring_cloud_aws_s3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_id_generator")
    @SequenceGenerator(name = "image_id_generator", sequenceName = "image_id_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String url;

    private Float size;

    public Image(String name, String url, Float size) {
        this.name = name;
        this.url = url;
        this.size = size;
    }
}
