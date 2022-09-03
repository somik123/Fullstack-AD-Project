package sg.edu.nus.iss.team1.service;

import java.time.LocalDateTime;
import java.util.List;

import sg.edu.nus.iss.team1.domain.StudyGroup;
import sg.edu.nus.iss.team1.domain.User;
import sg.edu.nus.iss.team1.dto.StudyGroupDTO;

public interface StudyGroupService {
    public void saveStudyGroup(StudyGroup studyGroup);

    public StudyGroup findByName(String name);

    public StudyGroup findById(Integer Id);

    public List<StudyGroup> findByCreateDate(LocalDateTime createDate);

    public void deleteById(Integer id);

    public StudyGroupDTO saveStudyGroupDTO(StudyGroupDTO studyGroup);

    public List<StudyGroupDTO> findStudyGroupsByUserId(Integer id);


    public List<StudyGroup> findAllStudyGroup();

    StudyGroupDTO joinGroup(User joinUser, StudyGroup existingGroup);

    boolean checkparticipants(List<User> participants, User joinUser);

    StudyGroup checkindividualgroupExist(User senderUser, User receiverUser);

    StudyGroup searchindividualGroup(User senderUser, User receiverUser);

    List<StudyGroupDTO> getallindividualgroupasParticipant(User user);

}
