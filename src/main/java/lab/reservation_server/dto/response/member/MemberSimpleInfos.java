package lab.reservation_server.dto.response.member;

import java.util.List;
import lab.reservation_server.dto.request.member.MemberUpdate;
import lombok.Getter;

@Getter
public class MemberSimpleInfos {

    private List<MemberUpdate> members;

    public MemberSimpleInfos(List<MemberUpdate> memberUpdates) {
      this.members = memberUpdates;
    }
}
