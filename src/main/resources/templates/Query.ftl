package ${pack}.query;

import com.zero.boot.core.query.DataQuery;
import com.zero.boot.core.query.BaseQueryAccess;
import lombok.Data;

@Data
public class ${entityName}Query extend BaseQueryAccess {
<#if querys??>
    <#list querys as column>
        <#if column.queryType = 'EQUAL'>
            /** 精确 */
            @DataQuery(type = DataQuery.Type.LIKE, property = "${column.property}")
            private ${column.propertyType} ${column.property};
        </#if>
        <#if column.queryType = 'LIKE'>
            /** 模糊 */
            @DataQuery(type = DataQuery.Type.LIKE, property = "${column.property}")
            private ${column.propertyType} ${column.property};
        </#if>
        <#if column.queryType = 'NOT_EQUAL'>
            /** 不等于 */
            @DataQuery(type = DataQuery.Type.NOT_EQUAL)
            private ${column.propertyType} ${column.property};
        </#if>
    </#list>
</#if>
<#if betweens??>
    <#list betweens as column>
        /** BETWEEN */
        @DataQuery(type = DataQuery.Type.BETWEEN)
        private List<${column.propertyType}> ${column.property};
    </#list>
</#if>
}