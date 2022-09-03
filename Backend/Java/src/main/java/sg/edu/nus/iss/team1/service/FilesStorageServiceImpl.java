package sg.edu.nus.iss.team1.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        // TODO Auto-generated method stub
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public String save(MultipartFile file) {
        // TODO Auto-generated method stub
        try {
            // String newFileName = UUID.randomUUID().toString();
            String newFileName = LocalDateTime.now().toString().replace('T', '_');
            newFileName = newFileName.replace(':', '-');
            newFileName = newFileName.replace('.', '_');

            System.out.println(newFileName);

            int i = file.getOriginalFilename().lastIndexOf('.');
            if (i > 0) {
                newFileName += "." + file.getOriginalFilename().substring(i + 1);
            }
            Files.copy(file.getInputStream(), this.root.resolve(newFileName));
            return newFileName;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        // TODO Auto-generated method stub
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        // TODO Auto-generated method stub
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    @Override
    public String getMimeType(Resource file) {
        String fileName = file.getFilename();

        String ext = Optional.ofNullable(fileName).filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1)).get();

        String mimeType;

        switch (ext) {
            case "jpg":
                mimeType = "image/jpeg";
                break;
            case "png":
                mimeType = "image/png";
                break;
            case "gif":
                mimeType = "image/gif";
                break;
            case "mp4":
                mimeType = "video/mp4";
                break;
            case "pdf":
                mimeType = "application/pdf";
                break;
            case "txt":
                mimeType = "text/plain";
                break;
            default:
                mimeType = null;

        }

        System.out.println(file.getFilename());
        System.out.println(ext);
        System.out.println(mimeType);

        return mimeType;
    }

}
