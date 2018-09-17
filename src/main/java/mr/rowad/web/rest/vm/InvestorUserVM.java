package mr.rowad.web.rest.vm;

import mr.rowad.domain.Investor;
import mr.rowad.service.dto.UserDTO;

public class InvestorUserVM {
    private Investor investor;
    private UserDTO account;
    public Investor getInvestor() {
        return investor;
    }
    public void setInvestor(Investor investor) {
        this.investor = investor;
    }
    public UserDTO getAccount() {
        return account;
    }
    public void setAccount(UserDTO account) {
        this.account = account;
    }

}
