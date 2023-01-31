package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    @Value("${application.avatar.store}")
    private String folderForAvatars;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
    }

    public void uploadAvatar(MultipartFile multipartFile) throws IOException {
        byte[] data = multipartFile.getBytes();
        Avatar avatar = create(multipartFile.getSize(), multipartFile.getContentType(), data);

        String extension = Optional.ofNullable(multipartFile.getOriginalFilename())
                .map(s -> s.substring(multipartFile.getOriginalFilename().lastIndexOf('.')))
                .orElse("");
        Path path = Paths.get(folderForAvatars).resolve(avatar.getId() + extension);
        Files.write(path, data);
        avatar.setFilePath(path.toString());
        avatarRepository.save(avatar);
    }

    private Avatar create(long size, String contentType, byte[] data){
        Avatar avatar = new Avatar();
        avatar.setData(data);
        avatar.setFileSize(size);
        avatar.setMediaType(contentType);
        return avatarRepository.save(avatar);
    }

    public Pair<String,byte[]> readAvatarFromDb(long id) {
        Avatar avatar = avatarRepository.findByStudentId(id).orElseThrow(()-> new AvatarNotFoundException(id));
        return Pair.of(avatar.getMediaType(), avatar.getData());
    }

    public Pair<String,byte[]> readAvatarFromFs(long id) throws IOException{
        Avatar avatar = avatarRepository.findByStudentId(id).orElseThrow(()-> new AvatarNotFoundException(id));
        return Pair.of(avatar.getMediaType(), Files.readAllBytes(Paths.get(avatar.getFilePath())));
    }

    public ResponseEntity<Collection<Avatar>> getAll(Integer pageNumber, Integer pageSize){
        PageRequest pageRequest = PageRequest.of(pageNumber -1, pageSize);
        Collection<Avatar> avatarsList = avatarRepository.findAll(pageRequest).getContent();
        if(avatarsList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(avatarsList);
    }
}
