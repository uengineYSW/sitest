package sitest.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import sitest.domain.*;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "connectLogs",
    path = "connectLogs"
)
public interface ConnectLogRepository
    extends PagingAndSortingRepository<ConnectLog, Long> {}
