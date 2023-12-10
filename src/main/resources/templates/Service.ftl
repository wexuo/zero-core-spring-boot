package ${pack}.service;

import ${entityPackage};
import ${pack}.repository.${entityName}Repository;
import com.zero.boot.core.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ${entityName}Service extends BaseService<${entityName}, ${primaryKeyType}, ${entityName}Repository> {
}