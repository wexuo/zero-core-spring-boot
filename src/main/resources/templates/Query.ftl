package ${pack}.query;

import com.zero.boot.core.query.BaseQueryAccess;
import com.zero.boot.core.query.*;
import ${entityPackage};
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Query(${entityName}.class)
<#--@formatter:off-->
<#if likes??>
@QueryLike(value = {<#assign index = 0><#list likes as column><#if index != 0>, </#if>"${ column }"<#assign index = index + 1></#list>})
</#if>
<#if equalQuerys??>
@QueryEqual(value = {<#assign index = 0><#list equalQuerys as column><#if index != 0>, </#if>"${column.prop}"<#assign index = index + 1></#list>})
</#if>
<#if between??>
@QueryBetween(value = "${between}")
</#if>
public class ${entityName}Query extends BaseQueryAccess {
<#if equalQuerys??>
    <#list equalQuerys as column>
    private ${column.type} ${column.prop};
    </#list>
</#if>
}
<#--@formatter:on-->