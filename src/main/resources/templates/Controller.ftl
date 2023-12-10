package ${pack}.controller;

import com.zero.boot.core.annotation.ResponseResult;
import com.zero.boot.core.controller.BaseController;
import ${entityPackage};
import ${pack}.query.${entityName}Query;
import ${pack}.service.${entityName}Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseResult
@RequestMapping("${path}")
public class ${entityName}Controller extends BaseController<${entityName}, ${primaryKeyType}, ${entityName}Query, ${entityName}Service> {

}