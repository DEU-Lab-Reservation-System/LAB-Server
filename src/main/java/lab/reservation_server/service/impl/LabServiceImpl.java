package lab.reservation_server.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.dto.response.lab.LectureInLab;
import lab.reservation_server.dto.response.lab.LectureInfoForLab;
import lab.reservation_server.dto.response.lecture.LectureInfo;
import lab.reservation_server.exception.BadRequestException;
import lab.reservation_server.repository.LabRepository;
import lab.reservation_server.service.LabService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LabServiceImpl implements LabService {

    private final LabRepository labRepository;

    @Override
    public LectureInLab getLabTimeTable(String roomNumber) {

        // 찾아진 lab에 대해서는 lectures에 대해서 프록시 객체로 가지고 있다.
        Lab lab = labRepository.findByRoomNumber(roomNumber)
            .orElseThrow(() -> new BadRequestException("해당 강의실이 존재하지 않습니다."));

        // get lectures from lab entity, then convert to LectureInfo
        // Lab에서 데이터 하나만 가져오기 때문에 N+1 문제가 발생하지 않는다.
        // 만약 List로 구성된 Lab에서 lectures를 가져오게 된다면 N+1 문제가 발생한다.
        // 만약 여러 Lab에 대한 lectures를 가져온다 하더라도, batch size를 설정해줬기 때문에 N+1 문제가 발생하지 않는다.
        List<LectureInfoForLab> lectures = lab.getLectures().stream()
            .map(lecture -> new LectureInfoForLab(
                lecture.getId(),
                lecture.getTitle(),
                lecture.getProfessor(),
                lecture.getCode(),
                lecture.getDay(),
                lecture.getStartTime(),
                lecture.getEndTime(),
                lecture.getStartDate(),
                lecture.getEndDate()
            )).collect(Collectors.toList());

        return new LectureInLab(roomNumber, lectures);
    }

    @Override
    public List<LectureInfo> getAllLabTimeTable() {
        List<Lab> labs = labRepository.findAll();
        List<LectureInfo> lectureInfos = new ArrayList<>();

        // OneToMany에 의해 N+1문제가 발생할것 같지만, Batch Size로 인해 where in으로 들어가서 한방 쿼리로 조회 가능했다.
        for (Lab lab : labs) {
            lab.getLectures().stream()
                .map(lecture -> new LectureInfo(
                    lecture.getId(),
                    lecture.getTitle(),
                    lecture.getProfessor(),
                    lecture.getCode(),
                    lab.getRoomNumber(),
                    lecture.getDay(),
                    lecture.getStartTime().toString(),
                    lecture.getEndTime().toString(),
                    lecture.getStartDate().toString(),
                    lecture.getEndDate().toString()
                )).forEach(lectureInfos::add);
        }

        return lectureInfos;
    }
}
