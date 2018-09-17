package mr.rowad.web.rest.vm;

import mr.rowad.domain.TeamMember;
import mr.rowad.service.dto.UserDTO;

public class MemberUserVM {
    private TeamMember member;
    private UserDTO account;

    public TeamMember getMember() {
        return member;
    }
    public void setMember(TeamMember member) {
        this.member = member;
    }
    public UserDTO getAccount() {
        return account;
    }
    public void setAccount(UserDTO account) {
        this.account = account;
    }

}
