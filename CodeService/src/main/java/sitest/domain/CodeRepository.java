package sitest.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import sitest.domain.*;

@RepositoryRestResource(collectionResourceRel = "codes", path = "codes")
public interface CodeRepository
    extends PagingAndSortingRepository<Code, Long> {}
