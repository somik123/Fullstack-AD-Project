package sg.edu.nus.iss.team1.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
    public void init();

    public String save(MultipartFile file);

    public Resource load(String filename);

    public String getMimeType(Resource file);

    public void deleteAll();

    public Stream<Path> loadAll();
}
