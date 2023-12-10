<#--@formatter:off-->
<template>
    <cl-crud ref="Crud">
        <cl-row>
            <!-- 刷新按钮 -->
            <cl-refresh-btn/>
            <!-- 新增按钮 -->
            <cl-add-btn/>
            <!-- 删除按钮 -->
            <cl-multi-delete-btn/>
    <#if equalQuerys??>
        <#list equalQuerys as column>
            <cl-filter label="${column.label}">
                <cl-select :options="options.${column.prop}" prop="${column.prop}" :width="120" />
            </cl-filter>
        </#list>
    </#if>

            <cl-flex1/>
            <!-- 关键字搜索 -->
            <cl-search-key field="keyword"/>
        </cl-row>

        <cl-row>
            <cl-table ref="Table"/>
        </cl-row>

        <cl-row>
            <cl-flex1/>
            <cl-pagination/>
        </cl-row>

        <cl-upsert ref="Upsert"/>
    </cl-crud>
</template>
<script lang="ts" name="${entityCamelCaseName}" setup>
    import {reactive} from "vue";
    import {useCrud, useTable, useUpsert} from "@cool-vue/crud";
    import {useCool} from "/@/cool";

    const { service } = useCool();

    <#if equalQuerys??>
    const options = reactive({
        <#list equalQuerys as column>
        ${column.prop}: [
            {
                value: "",
                label: "全部"
            },
        ]
        </#list>
    })
    </#if>

    const Table = useTable({
        columns: [
            { type: "selection" },
            <#if columns??>
            <#list columns as column>
            {
                prop: "${column.prop}",
                label: "${column.label}"
            },
            </#list>
            </#if>
            {type: "op", buttons: ["edit", "info", "delete"]}
        ]
    })

    const Upsert = useUpsert({
        items: [
            <#if items??>
            <#list items as item>
            {
                prop: "${item.prop}",
                label: "${item.label}",
                component: {
                    name: "${item.component}"
                },
                required: ${item.required ? string('true', 'false')}
            },
            </#list>
            </#if>
        ]
    })

    const Crud = useCrud(
        {
            service: service.${entityCamelCaseName}
        },
        (app) => {
            app.refresh();
        }
    );
</script>
<#--@formatter:on-->