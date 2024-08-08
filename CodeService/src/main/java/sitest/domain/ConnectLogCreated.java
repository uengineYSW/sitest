package sitest.domain;

import java.util.*;
import lombok.*;
import sitest.domain.*;
import sitest.infra.AbstractEvent;

@Data
@ToString
public class ConnectLogCreated extends AbstractEvent {

    private Long logNo;
    private Long loginDt;
    private String loginId;
    private String loginIdnm;
    private String accessIp;
}
