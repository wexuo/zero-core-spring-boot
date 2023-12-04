<template>
    <cl-crud ref="Crud">
        <cl-row>
            <!-- 刷新按钮 -->
            <cl-refresh-btn/>
            <!-- 新增按钮 -->
            <cl-add-btn/>
            <!-- 删除按钮 -->
            <cl-multi-delete-btn/>
            <cl-flex1/>
            <!-- 关键字搜索 -->
            <cl-search-key/>
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
    import {useCrud, useTable, useUpsert} from "@cool-vue/crud";
    import {useCool} from "/@/cool";

    const {service} = useCool();

    // cl-table
    const Table = useTable({
        columns: [
            {type: "selection"},
            <#if columns??>
            <#list columns as column>
            {
                prop: "${column.property}",
                label: "${column.label}"
            },
            </#list>
            </#if>,
            {type: "op", buttons: ["edit", "info", "delete"]}
        ]
    })

    // cl-upsert
    const Upsert = useUpsert({
        items: [
            <#if items??>
            <#list items as item>
            {
                prop: "${item.property}",
                label: "${item.label}",
                <#if (item.component)?? && (item.component) == "custom">
                component: {
                    name: ""
                },
                <#else>
                component: {
                    name: "${item.component}"
                },
                <#if (item.required)?? && (item.required)>
                required: true
                </#if>
                </#if>
            },
            </#list>
            </#if>
        ]
    })

    // cl-crud
    const Crud = useCrud(
        {
            service: service.${entityCamelCaseName}
        },
        (app) => {
            app.refresh();
        }
    );

</script>