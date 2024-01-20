package ${pack}.controller;

import com.zero.boot.core.annotation.Module;
import com.zero.boot.core.annotation.ResponseResult;
import ${entityPackage};
import ${pack}.query.${entityName}Query;
import ${pack}.service.${entityName}Service;
import com.zero.boot.core.data.PageResult;
import com.zero.boot.core.exception.ServiceRuntimeException;
import com.zero.boot.core.query.BaseQueryAccess;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

<#--@formatter:off-->
@RestController
@ResponseResult
@RequestMapping("${path}")
@Module(name = "admin", entity = ${entityName}.class, desc = "${entityName}管理")
public class ${entityName}Controller {

    @Resource
    private ${entityName}Service service;

    /**
     * 新增 ${entityName}
     */
    @PostMapping(value = "/add")
    public ${entityName} add(@Validated @RequestBody final ${entityName} entity) {
        return this.service.save(entity);
    }

    /**
     * 批量删除 ${entityName}
     */
    @PostMapping(value = "/delete")
    public void delete(@RequestBody final List<${primaryKeyType}> ids) throws ServiceRuntimeException {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceRuntimeException(HttpStatus.BAD_REQUEST, "ids is empty");
        }
        this.service.deleteByIds(ids);
    }

    /**
     * 更新 ${entityName}
     */
    @PostMapping(value = "/update")
    public ${entityName} update(@Validated @RequestBody final ${entityName} entity) {
        return this.service.save(entity);
    }

    /**
     * 查询全部 ${entityName}
     */
    @GetMapping(value = "/list")
    public <Q extends BaseQueryAccess> List<${entityName}> list(final ${entityName}Query query) {
        return this.service.list(query);
    }

    /**
     * 条件+分页查询 ${entityName}
     */
    @GetMapping(value = "/page")
    public <Q extends BaseQueryAccess> PageResult<${entityName}> page(final ${entityName}Query query, final Pageable pageable) {
        final Page<${entityName}> page = this.service.page(query, pageable);
        return PageResult.convert(page);
    }

    /**
     * 查询 ${entityName} 详情
     */
    @GetMapping(value = "/info")
    public ${entityName} info(final ${primaryKeyType} id) throws ServiceRuntimeException {
        if (Objects.isNull(id)) {
            throw new ServiceRuntimeException(HttpStatus.BAD_REQUEST, "id is null");
        }
        return service.findById(id);
    }
}
<#--@formatter:on-->