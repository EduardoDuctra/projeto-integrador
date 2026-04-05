package br.projeto_integrador.aplicativo.backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/teste")
public class CloudinaryController {

    private final Cloudinary cloudinary;

    public CloudinaryController(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @GetMapping("/cloudinary")
    public String testarCloudinary() {
        try {
            Map result = cloudinary.uploader().upload(
                    "https://res.cloudinary.com/demo/image/upload/sample.jpg",
                    ObjectUtils.emptyMap()
            );

            return result.get("secure_url").toString();

        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }
}