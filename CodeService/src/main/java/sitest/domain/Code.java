package sitest.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import sitest.CodeServiceApplication;
import sitest.domain.CodeCreated;
import sitest.domain.CodeUpdated;

@Entity
@Table(name = "Code_table")
@Data
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @PostPersist
    public void onPostPersist() {
        CodeCreated codeCreated = new CodeCreated(this);
        codeCreated.publishAfterCommit();
        CodeUpdated codeUpdated = new CodeUpdated(this);
        codeUpdated.publishAfterCommit();
    }

    @PrePersist
    public void onPrePersist() {}

    public static CodeRepository repository() {
        CodeRepository codeRepository = CodeServiceApplication.applicationContext.getBean(
            CodeRepository.class
        );
        return codeRepository;
    }

    public static void updateCode(ConnectLogCreated connectLogCreated) {
        repository()
            .findById(connectLogCreated.getLogNo())
            .ifPresent(code -> {
                code.setItemCode("A001");
                code.setCodeNo("C001");
                code.setCode("001");
                code.setCodeName("Code 1");
                code.setIsSys("Y");
                code.setIsUse("Y");
                code.setEtc("N/A");
                repository().save(code);
                CodeUpdated codeUpdated = new CodeUpdated(code);
                codeUpdated.publishAfterCommit();
            });
    }
}
