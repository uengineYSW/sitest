package sitest.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import sitest.domain.*;
import sitest.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class CodeCreated extends AbstractEvent {

    private Long id;
    private String itemCode;
    private String codeNo;
    private String code;
    private String codeName;
    private String isSys;
    private String isUse;
    private String etc;
    private String etc1;
    private String etc2;
    private String etc3;
    private String etc4;
    private String etc5;

    public CodeCreated(Code aggregate) {
        super(aggregate);
    }

    public CodeCreated() {
        super();
    }
}
//>>> DDD / Domain Event
