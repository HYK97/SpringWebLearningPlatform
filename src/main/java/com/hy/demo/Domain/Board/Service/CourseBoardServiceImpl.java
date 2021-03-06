package com.hy.demo.Domain.Board.Service;

import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Board.Repository.CourseBoardRepository;
import com.hy.demo.Domain.File.Dto.FileDto;
import com.hy.demo.Domain.File.Entity.File;
import com.hy.demo.Domain.File.Repository.FileRepository;
import com.hy.demo.Domain.File.Service.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.hy.demo.Utils.ObjectUtils.isEmpty;
import static java.util.stream.Collectors.*;

/**
 * service 명명 규칙
 * select -> find
 * modifyCourseEvaluation -> modify
 * insert -> add
 * delete -> delete
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseBoardServiceImpl implements CourseBoardService {

    private final CourseBoardRepository courseBoardRepository;

    private final FileRepository fileRepository;

    private final FileServiceImpl fileService;


    @Transactional
    public void updateCourseBoard(Long id, List<MultipartFile> file, String title, String contents) throws Exception {
        List<FileDto> fileDto = new ArrayList<>();
        List<File> deleteFile = new ArrayList<>();
        CourseBoard courseBoard = courseBoardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("엔티티없음"));
        courseBoard.updateCourseBoard(title, contents);
        List<File> findFileList = fileRepository.findByCourseBoardId(id).get();
        if (!isEmpty(file)) { //파일없을때
            for (MultipartFile multipartFile : file) {
                log.debug("Before MultipartFile.getOriginalFilename() = {}", multipartFile.getOriginalFilename());
            }

            if (!isEmpty(findFileList)) { //변경점 확인
                for (File file1 : findFileList) {
                    boolean b = file.stream().map(e -> e.getOriginalFilename()).collect(toList())
                            .containsAll(Collections.singleton(file1.getOrigFileName()))
                            &&
                            file.stream().map(e -> e.getSize()).collect(toList())
                                    .containsAll(Collections.singleton(file1.getFileSize()));
                    if (b) {
                        log.debug("유지 file1.getOrigFileName() = {}", file1.getOrigFileName()); //현재있는 파일 유지
                    } else {

                        log.debug("삭제 file1.getOrigFileName() = {}", file1.getOrigFileName()); //삭제된 파일 삭제
                        deleteFile.add(file1);
                    }
                }

                for (File file1 : findFileList) { //추가된파일
                    file.stream().filter(f ->
                                    f.getOriginalFilename().equals(file1.getOrigFileName()) && f.getSize() == file1.getFileSize())
                            .collect(toList())
                            .forEach(li -> file.remove(li));
                }

                for (MultipartFile multipartFile : file) { //추가된파일
                    log.debug("After MultipartFile.getOriginalFilename() = {}", multipartFile.getOriginalFilename());
                }

                boolean b = fileService.deleteFile(deleteFile);
                if (!b) {
                    throw new Exception("파일 삭제 오류.");
                }
                fileRepository.deleteAll(deleteFile);
                fileDto = fileService.localSaveFile(file);// 파일 추가
                save(courseBoard, fileDto);

            } else { //처음파일없을대
                fileDto = fileService.localSaveFile(file);// 파일 추가
                save(courseBoard, fileDto);
            }


        } else { // 파일 모두 삭제시에
            boolean b = fileService.deleteFileDto(fileDto);
            if (!b) {
                throw new Exception("파일 삭제 오류.");
            }
            Long deleteRow = fileRepository.deleteByCourseBoardId(id);
            if (findFileList.size() != deleteRow.intValue()) {
                throw new Exception("파일의 개수가 일치하지않음.");
            }
            save(courseBoard, fileDto);
        }

    }


    @Transactional
    public List<CourseBoardDto> findCourseBoardList(Long id) {
        List<CourseBoardDto> courseBoardDtoList = courseBoardRepository.findByCourseIdNotContents(id);
        for (CourseBoardDto courseBoardDto : courseBoardDtoList) {
            List<FileDto> findFiles = fileRepository.findFileIdByCourseId(courseBoardDto.getId());
            courseBoardDto.setFiles(findFiles);
        }
        return courseBoardDtoList;
    }

    @Transactional
    public CourseBoardDto findCourseBoardByCourseBoardId(Long courseBoardId) {
        CourseBoard findCourseBoard = courseBoardRepository.findByCourseBoardId(courseBoardId);
        return findCourseBoard.changeDto();
    }

    public void viewPlus(Long id) {
        CourseBoard courseBoard = courseBoardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("찾는엔티티 없음"));
        courseBoard.viewPlus();
        courseBoardRepository.save(courseBoard);
    }

    @Transactional
    public void save(CourseBoard courseBoard, List<FileDto> fileDto) {
        List<File> files = new ArrayList<>();
        CourseBoard findCourseBoard = null;
        if (fileDto.size() == 0) {
            courseBoardRepository.save(courseBoard);
        } else {
            findCourseBoard = courseBoardRepository.save(courseBoard);
            for (FileDto dto : fileDto) {
                File file = File.builder()
                        .courseBoard(findCourseBoard)
                        .filePath(dto.getFilePath())
                        .origFileName(dto.getOrigFileName())
                        .fileSize(dto.getFileSize())
                        .build();
                files.add(file);

            }
            fileRepository.saveAll(files);
        }
    }

    public void deleteBoardAndFiles(Long courseBoardId) throws AccessDeniedException {
        //코스확인
        CourseBoard courseBoard = courseBoardRepository.findById(courseBoardId)
                .orElseThrow(() -> new AccessDeniedException("없는코스"));
        Optional<List<File>> findFiles = fileRepository.findByCourseBoardId(courseBoardId);
        List<File> files = findFiles.orElseGet(null);
        if (!isEmpty(files)) {
            fileService.deleteFile(files);
        }
        courseBoardRepository.delete(courseBoard);
    }


    @Transactional
    public void updateBoard(CourseBoard courseBoard, List<FileDto> fileDto) {
        List<File> files = new ArrayList<>();
        CourseBoard findCourseBoard = null;
        if (fileDto.size() == 0) {
            courseBoardRepository.save(courseBoard);
        } else {
            findCourseBoard = courseBoardRepository.save(courseBoard);
            for (FileDto dto : fileDto) {
                File file = File.builder()
                        .courseBoard(findCourseBoard)
                        .filePath(dto.getFilePath())
                        .origFileName(dto.getOrigFileName())
                        .fileSize(dto.getFileSize())
                        .build();
                files.add(file);

            }
            fileRepository.saveAll(files);
        }
    }

    public Long countAllView(Long courseId) {

        return courseBoardRepository.countViewByCourseId(courseId);
    }

    public List<CourseBoardDto> rankView(Long courseId) {
        return courseBoardRepository.findRankViewByCourseId(courseId);
    }

    public void courseBoardFileDelete(Long courseId) {
        List<CourseBoard> courseBoard = courseBoardRepository.findByCourseId(courseId).orElseThrow(() -> new EntityNotFoundException("회차 없음"));
        for (CourseBoard board : courseBoard) {
            Optional<List<File>> findFiles = fileRepository.findByCourseBoardId(board.getId());
            List<File> files = findFiles.orElseGet(null);
            if (!isEmpty(files)) {
                fileService.deleteFile(files);
            }
        }
    }


}
