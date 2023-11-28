package com.example.web.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "images")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalFileName, contentType, name ;
    private Long size;
    private boolean isPreviewImage;
    @Lob
    private byte[] bytes;


    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", originalFileName='" + originalFileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", isPreviewImage=" + isPreviewImage +
                '}';
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Image image = (Image) o;
//        return isPreviewImage == image.isPreviewImage && Objects.equals(id, image.id) && Objects.equals(originalFileName, image.originalFileName) && Objects.equals(contentType, image.contentType) && Objects.equals(name, image.name) && Objects.equals(size, image.size) && Arrays.equals(bytes, image.bytes) && Objects.equals(immovables, image.immovables);
//    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, originalFileName, contentType, name, size, isPreviewImage);
        result = 31 * result + Arrays.hashCode(bytes);
        return result;
    }
}
