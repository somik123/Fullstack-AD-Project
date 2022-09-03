package sg.edu.nus.iss.team1.dto;

import java.util.List;

import sg.edu.nus.iss.team1.domain.Event;
import sg.edu.nus.iss.team1.domain.StudyGroup;

public class DashboardDTO {
    private List<StudyGroupDTO> activeGroupList;
    private int totalMessageSend;
    private int totalGroups;
    private int totalEvents;
    private int totalBookGenre;
    private List<Event> eventList;
    private List<StudyGroup> studyGroupList;
    private List<UserChatActivityDTO> userActivity;
    private List<GroupParticipantRateDTO> participantRate;


    public DashboardDTO() {
    }

    public DashboardDTO(int totalMessageSend, int totalGroups, int totalEvents, int totalBookGenre) {
        this.totalMessageSend = totalMessageSend;
        this.totalGroups = totalGroups;
        this.totalEvents = totalEvents;
        this.totalBookGenre = totalBookGenre;
    }

    public int getTotalMessageSend() {
        return totalMessageSend;
    }

    public int getTotalGroups() {
        return totalGroups;
    }

    public int getTotalEvents() {
        return totalEvents;
    }

    public int getTotalBookGenre() {
        return totalBookGenre;
    }

    public List<StudyGroupDTO> getActiveGroupList() {
        return activeGroupList;
    }

    public void setActiveGroupList(List<StudyGroupDTO> activeGroupList) {
        this.activeGroupList = activeGroupList;
    }

    public List<GroupParticipantRateDTO> getParticipantRate() {
        return participantRate;
    }

    public void setParticipantRate(List<GroupParticipantRateDTO> participantRate) {
        this.participantRate = participantRate;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public List<StudyGroup> getStudyGroupList() {
        return studyGroupList;
    }

    public void setStudyGroupList(List<StudyGroup> studyGroupList) {
        this.studyGroupList = studyGroupList;
    }


    public class UserChatActivityDTO {
        private String userName;
        private List<String> days;
        private List<Integer> messages;
        private int groupId;

    }

    public class GroupParticipantRateDTO {
        private String groupName;
        private int groupId;
        private int participantCount;

        public GroupParticipantRateDTO(String groupName, int participantCount) {
            this.groupName = groupName;
            this.participantCount = participantCount;
        }

        public String getGroupName() {
            return groupName;
        }

        public int getParticipantCount() {
            return participantCount;
        }
    }
}

