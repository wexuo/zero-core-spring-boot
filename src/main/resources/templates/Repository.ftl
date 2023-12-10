package ${pack}.repository;

import ${entityPackage};
import com.zero.boot.core.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ${entityName}Repository extends BaseRepository<${entityName}, ${primaryKeyType}> {
}