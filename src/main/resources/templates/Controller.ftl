package ${pack}.controller;

import ${pack}.bean.${entityName};
import ${pack}.service.${entityName}Service;
import ${pack}.query.${entityName}Query;
import com.zero.boot.core.annotation.RequestAPI;
import com.zero.boot.core.annotation.ZeroRestController;
import com.zero.boot.core.controller.BaseController;

@ZeroRestController(value = "${path}", api = {RequestAPI.DELETE, RequestAPI.UPDATE, RequestAPI.PAGE, RequestAPI.LIST, RequestAPI.INFO})
public class ${entityName}Controller extends BaseController<${entityName}, ${primaryKeyType}, ${entityName}Service> {

}