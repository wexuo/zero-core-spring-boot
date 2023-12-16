package ${pack}.service;

import ${entityPackage};
import ${pack}.repository.${entityName}Repository;
import com.zero.boot.core.service.BaseService;
import org.springframework.stereotype.Service;

<#--@formatter:off-->
@Service
public class ${entityName}Service extends BaseService<${entityName}, ${primaryKeyType}, ${entityName}Repository> {
    public ${entityName}Service(final ${entityName}Repository repository) {
        super(repository);
    }
}
<#--@formatter:on-->