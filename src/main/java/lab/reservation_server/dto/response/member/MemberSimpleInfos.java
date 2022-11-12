package lab.reservation_server.dto.response.member;

import java.util.ArrayList;
import java.util.List;
import lab.reservation_server.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class MemberSimpleInfos {

    private List<MemberSimpleInfo> members = new ArrayList<>();


    public MemberSimpleInfos(List<MemberSimpleInfo> members) {
      this.members = members;
    }

}
